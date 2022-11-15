package design.aeonic.nifty.api.networking.container.field;

import design.aeonic.nifty.api.networking.container.DataField;
import design.aeonic.nifty.api.transfer.fluid.FluidStack;
import design.aeonic.nifty.api.util.DataUtils;
import it.unimi.dsi.fastutil.shorts.ShortArrays;
import net.minecraft.core.Registry;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * A field for syncing fluid stacks. Does not sync NBT data, just the fluid type and amount.
 */
public class FluidStackField extends DataField<FluidStack> {
    public FluidStackField() {
        super();
    }

    public FluidStackField(@Nullable Supplier<FluidStack> getter) {
        super(getter);
    }

    @Override
    protected FluidStack defaultValue() {
        return FluidStack.EMPTY_STACK;
    }

    @Override
    protected short[] encode(FluidStack value) {
        short fluid = (short) Registry.FLUID.getId(value.getFluid());
        short[] amount = DataUtils.longToFourShorts(value.getAmount());
        return new short[] {
                fluid,
                amount[0],
                amount[1],
                amount[2],
                amount[3]
        };
    }

    @Override
    protected FluidStack decode(short[] data) {
        if (data.length < 5) return FluidStack.EMPTY_STACK;
        return FluidStack.of(
                Registry.FLUID.byId(data[0]),
                DataUtils.longFromFourShorts(ShortArrays.copy(data, 1, 4))
        );
    }

    @Override
    public int slots() {
        return 5;
    }
}