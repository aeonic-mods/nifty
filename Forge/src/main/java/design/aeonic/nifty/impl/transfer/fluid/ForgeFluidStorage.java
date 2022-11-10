package design.aeonic.nifty.impl.transfer.fluid;

import design.aeonic.nifty.api.transfer.fluid.FluidStack;
import design.aeonic.nifty.api.transfer.fluid.FluidStorage;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class ForgeFluidStorage implements FluidStorage {
    private final IFluidHandler handler;

    public ForgeFluidStorage(IFluidHandler handler) {
        this.handler = handler;
    }

    @Override
    public int getSlots() {
        return handler.getTanks();
    }

    @Override
    public FluidStack getStackInSlot(int slot) {
        return fromForgeStack(handler.getFluidInTank(slot));
    }

    @Override
    public long getSlotCapacity(int slot) {
        return handler.getTankCapacity(slot);
    }

    @Override
    public boolean isEverValid(int slot, FluidStack stack) {
        return handler.isFluidValid(slot, toForgeStack(stack));
    }

    @Override
    public FluidStack insert(int slot, @NotNull FluidStack stack, boolean simulate) {
        return insert(stack, simulate);
    }

    @Override
    public FluidStack insert(@NotNull FluidStack stack, boolean simulate) {
        FluidStack ret = stack.copy();
        int filledAmount = handler.fill(toForgeStack(stack), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
        ret.shrink(filledAmount);

        return ret;
    }

    @Override
    public FluidStack extract(Predicate<FluidStack> filter, long amount, boolean simulate) {
        Fluid fluid = null;
        for (int i = 0; i < handler.getTanks(); i++) {
            net.minecraftforge.fluids.FluidStack stack = handler.getFluidInTank(i);
            if (stack.isEmpty()) continue;
            if (filter.test(fromForgeStack(stack))) {
                fluid = stack.getFluid();
                break;
            }
        }
        if (fluid == null) return FluidStack.EMPTY_STACK;

        int intAmount = (int) Math.min(amount, Integer.MAX_VALUE);
        return fromForgeStack(handler.drain(new net.minecraftforge.fluids.FluidStack(fluid, intAmount), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE));
    }

    @Override
    public FluidStack extract(int slot, long amount, boolean simulate) {
        int intAmount = (int) Math.min(amount, Integer.MAX_VALUE);

        net.minecraftforge.fluids.FluidStack tankStack = handler.getFluidInTank(slot);
        if (tankStack.isEmpty()) return FluidStack.EMPTY_STACK;

        net.minecraftforge.fluids.FluidStack extracted = handler.drain(new net.minecraftforge.fluids.FluidStack(tankStack.getFluid(), intAmount), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
        return fromForgeStack(extracted);
    }

    public static FluidStack fromForgeStack(net.minecraftforge.fluids.FluidStack stack) {
        return FluidStack.of(stack.getFluid(), stack.getAmount(), stack.getTag());
    }

    public static net.minecraftforge.fluids.FluidStack toForgeStack(FluidStack stack) {
        // Forge fluid tanks can't handle longs
        return new net.minecraftforge.fluids.FluidStack(stack.getFluid(), (int) stack.getAmount(), stack.getTag());
    }
}
