package design.aeonic.nifty.api.transfer.item;

import design.aeonic.nifty.api.transfer.Storage;
import design.aeonic.nifty.api.transfer.base.DelegateStorage;
import net.minecraft.world.item.ItemStack;

public class DelegateItemStorage extends DelegateStorage<ItemStack> implements ItemStorage {
    public DelegateItemStorage(Storage<ItemStack> parent, int... slots) {
        super(parent, slots);
    }

    @Override
    public long getAmount(ItemStack stack) {
        return stack.getCount();
    }

    @Override
    public ItemStack getEmptyStack() {
        return ItemStack.EMPTY;
    }
}
