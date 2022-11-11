package design.aeonic.nifty.impl.transfer.item;

import design.aeonic.nifty.api.transfer.item.ItemStorage;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class NiftyItemHandler implements IItemHandler {
    private final ItemStorage itemStorage;

    public NiftyItemHandler(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    @Override
    public int getSlots() {
        return itemStorage.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return itemStorage.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return itemStorage.insert(slot, stack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return itemStorage.extract(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return (int) itemStorage.getSlotCapacity(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return itemStorage.isEverValid(slot, stack);
    }
}
