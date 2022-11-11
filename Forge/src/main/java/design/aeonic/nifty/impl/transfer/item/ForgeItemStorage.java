package design.aeonic.nifty.impl.transfer.item;

import design.aeonic.nifty.api.transfer.item.ItemStorage;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class ForgeItemStorage implements ItemStorage {
    private final IItemHandler handler;

    public ForgeItemStorage(IItemHandler handler) {
        this.handler = handler;
    }

    @Override
    public int getSlots() {
        return handler.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return handler.getStackInSlot(slot);
    }

    @Override
    public long getSlotCapacity(int slot) {
        return handler.getSlotLimit(slot);
    }

    @Override
    public boolean isEverValid(int slot, ItemStack stack) {
        return handler.isItemValid(slot, stack);
    }

    @Override
    public ItemStack insert(@Nonnull ItemStack stack, boolean simulate) {
        var tempStack = stack.copy();
        for (int i = 0; i < getSlots(); i++) {
            if (tempStack.isEmpty()) return ItemStack.EMPTY;
            tempStack = insert(i, tempStack, simulate);
        }
        return tempStack;
    }

    @Override
    public ItemStack insert(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return handler.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extract(Predicate<ItemStack> filter, long amount, boolean simulate) {
        for (int i = 0; i < handler.getSlots(); i++) {
            if (filter.test(handler.getStackInSlot(i))) return extract(i, amount, simulate);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack extract(int slot, long amount, boolean simulate) {
        return handler.extractItem(slot, (int) amount, simulate);
    }
}
