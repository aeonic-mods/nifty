package design.aeonic.nifty.api.transfer.fluid;

import design.aeonic.nifty.api.transfer.Storage;
import design.aeonic.nifty.api.transfer.base.DelegateStorage;
import design.aeonic.nifty.api.transfer.item.DelegateItemStorage;
import net.minecraft.world.item.ItemStack;

public interface FluidStorage extends Storage<FluidStack> {
    @Override
    default DelegateStorage<FluidStack> getDelegate(int... slots) {
        return new DelegateFluidStorage(this, slots);
    }
}
