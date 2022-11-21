package design.aeonic.nifty.api.transfer.item;

import design.aeonic.nifty.api.transfer.Storage;
import net.minecraft.world.item.ItemStack;

public interface ItemStorage extends Storage<ItemStack> {
    /**
     * Gets a storage delegate that only exposes the given slots. Used, for example, to expose output slots only
     * on a given side.
     */
    default ItemStorage getDelegate(int... slots) {
        return new DelegateItemStorage(this, slots);
    }
}
