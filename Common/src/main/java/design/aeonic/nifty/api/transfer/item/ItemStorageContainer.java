package design.aeonic.nifty.api.transfer.item;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * A Container wrapper for a {@link MutableItemStorage}, which you can use to sync your item storage within a menu.
 * <b>Should not</b> be used for item transfer.
 */
public class ItemStorageContainer implements Container {
    private final MutableItemStorage itemStorage;

    public ItemStorageContainer(MutableItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    @Override
    public int getContainerSize() {
        return itemStorage.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getContainerSize(); i++) {
            if (!getItem(i).isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return itemStorage.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return itemStorage.extract(slot, amount, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return itemStorage.removeItem(slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        itemStorage.setItem(slot, stack);
    }

    @Override
    public void setChanged() {}

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < getContainerSize(); i++) {
            setItem(i, ItemStack.EMPTY);
        }
    }
}
