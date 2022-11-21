package design.aeonic.nifty.api.transfer.fluid;

import design.aeonic.nifty.api.transfer.Storage;
import design.aeonic.nifty.api.transfer.base.DelegateStorage;

public class DelegateFluidStorage extends DelegateStorage<FluidStack> implements FluidStorage {
    public DelegateFluidStorage(Storage<FluidStack> parent, int... slots) {
        super(parent, slots);
    }

    @Override
    public long getAmount(FluidStack stack) {
        return stack.getAmount();
    }

    @Override
    public FluidStack getEmptyStack() {
        return FluidStack.EMPTY_STACK;
    }
}
