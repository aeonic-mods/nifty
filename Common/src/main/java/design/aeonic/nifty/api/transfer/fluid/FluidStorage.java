package design.aeonic.nifty.api.transfer.fluid;

import design.aeonic.nifty.api.transfer.Storage;

public interface FluidStorage extends Storage<FluidStack> {
    /**
     * Gets a storage delegate that only exposes the given slots. Used, for example, to expose output slots only
     * on a given side.
     */
    default FluidStorage getDelegate(int... slots) {
        return new DelegateFluidStorage(this, slots);
    }
}
