package design.aeonic.nifty.impl.transfer.fluid;

import design.aeonic.nifty.api.transfer.fluid.FluidStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class NiftyFluidHandler implements IFluidHandler {
    private final FluidStorage fluidStorage;

    public NiftyFluidHandler(FluidStorage fluidStorage) {
        this.fluidStorage = fluidStorage;
    }

    @Override
    public int getTanks() {
        return fluidStorage.getSlots();
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return ForgeFluidStorage.toForgeStack(fluidStorage.getStackInSlot(tank));
    }

    @Override
    public int getTankCapacity(int tank) {
        return (int) Math.min(fluidStorage.getSlotCapacity(tank), Integer.MAX_VALUE);
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return fluidStorage.isEverValid(tank, ForgeFluidStorage.fromForgeStack(stack));
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        design.aeonic.nifty.api.transfer.fluid.FluidStack stack = fluidStorage.insert(ForgeFluidStorage.fromForgeStack(resource), action.execute());
        return resource.getAmount() - (int) stack.getAmount();
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        return ForgeFluidStorage.toForgeStack(fluidStorage.extract(stack -> stack.getFluid() == resource.getFluid() && stack.getTag().equals(resource.getTag()), resource.getAmount(), action.execute()));
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        return ForgeFluidStorage.toForgeStack(fluidStorage.extract(stack -> true, maxDrain, action.execute()));
    }
}
