package design.aeonic.nifty.api.transfer.item;

import net.minecraft.world.item.ItemStack;

/**
 * A mutable {@link ItemStorage}. Item handlers you obtain from other mods or blocks will most likely not implement this!
 * <b>Use it only to wrap your item storage as an {@link ItemStorageContainer}</b>.
 */
public interface MutableItemStorage extends ItemStorage {

    /**
     * Sets the itemstack in the given slot with no checks.
     */
    void setItem(int slot, ItemStack stack);

    /**
     * Removes the item from the given slot with no checks, and returns it.
     */
    ItemStack removeItem(int slot);

    /**
     * Wrap this item storage as an {@link ItemStorageContainer}.
     */
    default ItemStorageContainer asContainer() {
        return new ItemStorageContainer(this);
    }
}
