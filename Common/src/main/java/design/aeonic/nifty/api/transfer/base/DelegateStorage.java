package design.aeonic.nifty.api.transfer.base;

import design.aeonic.nifty.api.transfer.Storage;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public abstract class DelegateStorage<T> implements Storage<T> {
    private final Storage<T> parent;
    private final int[] slots;

    public DelegateStorage(Storage<T> parent, int... slots) {
        this.parent = parent;
        this.slots = slots;
    }

    public abstract long getAmount(T stack);

    public abstract T getEmptyStack();

    @Override
    public int getSlots() {
        return slots.length;
    }

    @Override
    public T getStackInSlot(int slot) {
        return parent.getStackInSlot(slots[slot]);
    }

    @Override
    public long getSlotCapacity(int slot) {
        return parent.getSlotCapacity(slots[slot]);
    }

    @Override
    public boolean isEverValid(int slot, T stack) {
        return parent.isEverValid(slots[slot], stack);
    }

    @Override
    public T insert(@Nonnull T stack, boolean simulate) {
        for (int slot: slots) {
            var inserted = parent.insert(slot, stack, simulate);
            if (getAmount(inserted) != getAmount(inserted)) return inserted;
        }
        return stack;
    }

    @Override
    public T insert(int slot, @Nonnull T stack, boolean simulate) {
        return parent.insert(slots[slot], stack, simulate);
    }

    @Override
    public T extract(Predicate<T> filter, long amount, boolean simulate) {
        for (int slot: slots) {
            if (filter.test(parent.getStackInSlot(slot))) {
                var stack = parent.extract(slot, amount, simulate);
                if (getAmount(stack) > 0) return stack;
            }
        }
        return getEmptyStack();
    }

    @Override
    public T extract(int slot, long amount, boolean simulate) {
        return parent.extract(slots[slot], amount, simulate);
    }
}
