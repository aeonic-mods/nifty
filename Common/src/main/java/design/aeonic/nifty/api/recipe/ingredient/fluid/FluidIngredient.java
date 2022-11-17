package design.aeonic.nifty.api.recipe.ingredient.fluid;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import design.aeonic.nifty.api.recipe.ingredient.IngredientValue;
import design.aeonic.nifty.api.recipe.ingredient.ModularIngredient;
import design.aeonic.nifty.api.transfer.fluid.FluidStack;

public class FluidIngredient extends ModularIngredient<FluidStack> {
    public FluidIngredient(IngredientValue<FluidStack> value, long requiredAmount) {
        super(value, requiredAmount);
    }

    public static FluidIngredient fromNetwork(ExtraFriendlyByteBuf buf) {
        return new FluidIngredient(valueFromNetwork(buf), buf.readVarLong());
    }

    public static IngredientValue<FluidStack> valueFromNetwork(ExtraFriendlyByteBuf buf) {
        return switch (buf.readEnum(Type.class)) {
            case STACK -> FluidStackValue.fromNetwork(buf);
            case MULTI_STACK -> FluidMultiStackValue.fromNetwork(buf);
            case TAG -> FluidTagValue.fromNetwork(buf);
            case EMPTY -> FluidEmptyValue.INSTANCE;
        };
    }

    public static FluidIngredient fromJson(JsonObject json) {
        return new FluidIngredient(valueFromJson(json), json.get("amount").getAsLong());
    }

    public static IngredientValue<FluidStack> valueFromJson(JsonObject object) {
        if (object.has("tag")) return FluidTagValue.fromJson(object);
        if (object.has("fluid")) {
            JsonElement fluid = object.get("fluid");
            if (fluid.isJsonArray()) return FluidMultiStackValue.fromJson(object);
            else return FluidStackValue.fromJson(object);
        }
        return FluidEmptyValue.INSTANCE;
    }

    @Override
    public long getAmount(FluidStack stack) {
        return stack.getAmount();
    }

    @Override
    public FluidStack setAmount(FluidStack stack, long amount) {
        stack.setAmount(amount);
        return stack;
    }

    public enum Type {
        STACK,
        MULTI_STACK,
        TAG,
        EMPTY
    }
}
