package design.aeonic.nifty.api.transfer.item;

import design.aeonic.nifty.api.transfer.base.SimpleStorage;
import design.aeonic.nifty.api.transfer.fluid.FluidStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class SimpleItemStorage extends SimpleStorage<ItemStack> implements MutableItemStorage {
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
        if (!simulate) onChange();
        return tempStack;
    }

    @Override
    public final ItemStack insert(int slot, @Nonnull ItemStack stack, boolean simulate) {
        var ret = insertInternal(slot, stack.copy(), simulate);
        if (!simulate) onChange();
        return ret;
    }

    protected ItemStack insertInternal(int slot, @Nonnull ItemStack stack, boolean simulate) {
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

    @Override
    public ItemStack extract(Predicate<ItemStack> filter, long amount, boolean simulate) {
        ItemStack ret = null;
        for (int i = 0; i < getSlots(); i++) {
            if (filter.test(getStackInSlot(i))) {
                if (ret == null) ret = extract(i, amount, simulate);
                else if (canStack(ret, getStackInSlot(i))) {
                    long amountLeft = Math.min(amount, ret.getMaxStackSize()) - ret.getCount();
                    var temp = extract(i, amountLeft, simulate);
                    if (!temp.isEmpty()) {
                        ret.grow(temp.getCount());
                    }
                }
            }
        }
        if (!simulate) onChange();
        return ret == null ? getEmptyStack() : ret;
    }

    @Override
    public ItemStack extract(int slot, long amount, boolean simulate) {
        var stack = simulate ? getStackInSlot(slot).copy() : getStackInSlot(slot);
        if (stack.isEmpty()) return getEmptyStack();
        if (amount > stack.getCount()) amount = stack.getCount();

        if (!simulate) onChange();
        return stack.split((int) amount);
    }

    private boolean canStack(ItemStack first, ItemStack second) {
        if (first.hasTag() != second.hasTag()) return false;
        return first.sameItem(second) && ItemStack.tagMatches(first, second);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        set(slot, stack);
    }

    @Override
    public ItemStack removeItem(int slot) {
        ItemStack ret = getStackInSlot(slot);
        set(slot, getEmptyStack());
        return ret;
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        serializeTo(tag);
        return tag;
    }

    public void serializeTo(CompoundTag tag, String key) {
        CompoundTag myTag = tag.getCompound(key);
        serializeTo(myTag);
        tag.put(key, myTag);
    }

    public void serializeTo(CompoundTag tag) {
        for (int i = 0; i < getSlots(); i++) {
            var stack = getStackInSlot(i);
            if (!stack.isEmpty()) {
                CompoundTag stackTag = new CompoundTag();
                stack.save(stackTag);
                tag.put(String.valueOf(i), stackTag);
            }
        }
    }

    public void deserialize(CompoundTag tag, String key) {
        deserialize(tag.getCompound(key));
    }

    public void deserialize(CompoundTag tag) {
        for (int i = 0; i < getSlots(); i++) {
            if (tag.contains(String.valueOf(i))) {
                set(i, ItemStack.of(tag.getCompound(String.valueOf(i))));
            }
        }
    }
}
