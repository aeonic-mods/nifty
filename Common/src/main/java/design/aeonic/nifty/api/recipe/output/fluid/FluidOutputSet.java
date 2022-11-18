package design.aeonic.nifty.api.recipe.output.fluid;

import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import design.aeonic.nifty.api.recipe.output.RecipeOutput;
import design.aeonic.nifty.api.recipe.output.RecipeOutputSet;
import design.aeonic.nifty.api.transfer.fluid.FluidStack;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FluidOutputSet extends RecipeOutputSet<FluidStack> {
    public static final FluidOutputSet EMPTY = new FluidOutputSet(List.of());

    public FluidOutputSet(List<RecipeOutput<FluidStack>> recipeOutputs) {
        super(recipeOutputs);
    }

    public static FluidOutputSet fromNetwork(ExtraFriendlyByteBuf buf) {
        return new FluidOutputSet(buf.readList(FluidOutput::fromNetwork));
    }

    @Override
    protected String getPropertyName() {
        return "fluids";
    }

    @Override
    protected RecipeOutput<FluidStack> createOutput(FluidStack stack, float chance) {
        return new FluidOutput(stack, chance);
    }
}
