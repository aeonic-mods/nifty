package design.aeonic.nifty.api.transfer.item;

import design.aeonic.nifty.api.transfer.Storage;
import design.aeonic.nifty.api.transfer.base.DelegateStorage;
import net.minecraft.world.item.ItemStack;

public interface ItemStorage extends Storage<ItemStack> {
    @Override
    default DelegateStorage<ItemStack> getDelegate(int... slots) {
        return new DelegateItemStorage(this, slots);
    }
}
