package design.aeonic.nifty.api.transfer.item;

import design.aeonic.nifty.api.transfer.base.SimpleStorage;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class SimpleItemStorage extends SimpleStorage<ItemStack> implements ItemStorage {
    public SimpleItemStorage(int size) {
        super(size);
    }

    @Override
    protected ItemStack getEmptyStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public long getSlotCapacity(int slot) {
        return 64;
    }

    @Override
    public ItemStack insert(@Nonnull ItemStack stack, boolean simulate) {
        var tempStack = stack.copy();
        for (int i = 0; i < getSlots(); i++) {
            if (tempStack.isEmpty()) return getEmptyStack();
            insertInternal(i, tempStack, simulate);
        }
        return tempStack;
    }

    @Override
    public final ItemStack insert(int slot, @NotNull ItemStack stack, boolean simulate) {
        return insertInternal(slot, stack.copy(), simulate);
    }

    protected ItemStack insertInternal(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return getEmptyStack();
        var slotStack = getStackInSlot(slot);

        if (slotStack.isEmpty()) {
            if (!simulate) set(slot, stack.split((int) getSlotCapacity(slot)));
            else stack.shrink((int) getSlotCapacity(slot));
        } else if (canStack(slotStack, stack)) {
            int amount = Math.min((int) Math.min(getSlotCapacity(slot), slotStack.getMaxStackSize()) - slotStack.getCount(), stack.getCount());
            if (!simulate) slotStack.grow(amount);
            stack.shrink(amount);
        }
        return stack;
    }

    private boolean canStack(ItemStack first, ItemStack second) {
        if (first.hasTag() != second.hasTag()) return false;
        return first.sameItem(second) && ItemStack.tagMatches(first, second);
    }

    @Override
    public ItemStack extract(int slot, long amount, boolean simulate) {
        var stack = simulate ? getStackInSlot(slot).copy() : getStackInSlot(slot);
        if (stack.isEmpty()) return getEmptyStack();
        if (amount > stack.getCount()) amount = stack.getCount();

        return stack.split((int) amount);
    }
}
