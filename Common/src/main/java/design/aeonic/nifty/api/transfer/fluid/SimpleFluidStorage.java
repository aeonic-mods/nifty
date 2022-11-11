package design.aeonic.nifty.api.transfer.fluid;

import design.aeonic.nifty.api.transfer.base.SimpleStorage;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

@MethodsReturnNonnullByDefault
public class SimpleFluidStorage extends SimpleStorage<FluidStack> implements FluidStorage {
    public SimpleFluidStorage(int size) {
        super(size);
    }

    @Override
    protected FluidStack getEmptyStack() {
        return FluidStack.EMPTY_STACK;
    }

    @Override
    public long getSlotCapacity(int slot) {
        return FluidStack.BUCKET;
    }

    @Override
    public FluidStack insert(@Nonnull FluidStack stack, boolean simulate) {
        var tempStack = stack.copy();
        for (int i = 0; i < getSlots(); i++) {
            if (tempStack.isEmpty()) return getEmptyStack();
            insertInternal(i, tempStack, simulate);
        }
        if (!simulate) onChange();
        return tempStack;
    }

    @Override
    public FluidStack insert(int slot, @Nonnull FluidStack stack, boolean simulate) {
        var ret = insertInternal(slot, stack.copy(), simulate);
        if (!simulate) onChange();
        return ret;
    }

    protected FluidStack insertInternal(int slot, @Nonnull FluidStack stack, boolean simulate) {
        if (stack.isEmpty()) return getEmptyStack();
        var slotStack = getStackInSlot(slot);

        if (slotStack.isEmpty()) {
            if (!simulate) set(slot, stack.split(getSlotCapacity(slot)));
            else stack.shrink(getSlotCapacity(slot));
        } else if (slotStack.canStack(stack)) {
            var amount = Math.min(getSlotCapacity(slot) - slotStack.getAmount(), stack.getAmount());
            if (!simulate) slotStack.grow(amount);
            stack.shrink(amount);
        }
        return stack;
    }

    @Override
    public FluidStack extract(Predicate<FluidStack> filter, long amount, boolean simulate) {
        FluidStack ret = null;
        for (int i = 0; i < getSlots(); i++) {
            if (filter.test(getStackInSlot(i))) {
                if (ret == null) ret = extract(i, amount, simulate);
                else if (ret.canStack(getStackInSlot(i))) {
                    long amountLeft = amount - ret.getAmount();
                    var temp = extract(i, amountLeft, simulate);
                    if (!temp.isEmpty()) {
                        ret.grow(temp.getAmount());
                    }
                }
            }
        }
        if (!simulate) onChange();
        return ret == null ? getEmptyStack() : ret;
    }

    @Override
    public FluidStack extract(int slot, long amount, boolean simulate) {
        var stack = simulate ? getStackInSlot(slot).copy() : getStackInSlot(slot);
        if (stack.isEmpty()) return getEmptyStack();
        if (amount > stack.getAmount()) amount = stack.getAmount();

        if (!simulate) onChange();
        return stack.split(amount);
    }
}
