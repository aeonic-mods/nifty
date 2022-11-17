package design.aeonic.nifty.api.recipe.ingredient.fluid;

import com.google.gson.JsonObject;
import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import design.aeonic.nifty.api.recipe.ingredient.IngredientValue;
import design.aeonic.nifty.api.transfer.fluid.FluidStack;

import java.util.stream.Stream;

public class FluidEmptyValue implements IngredientValue<FluidStack> {
    public static final FluidEmptyValue INSTANCE = new FluidEmptyValue();

    @Override
    public boolean test(FluidStack stack) {
        return false;
    }

    @Override
    public Stream<FluidStack> getMatchingStacks() {
        return Stream.empty();
    }

    @Override
    public void toNetwork(ExtraFriendlyByteBuf buf) {
        buf.writeEnum(FluidIngredient.Type.EMPTY);
    }

    @Override
    public void toJson(JsonObject object) {}
}
