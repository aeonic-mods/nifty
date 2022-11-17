package design.aeonic.nifty.api.recipe.ingredient.item;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import design.aeonic.nifty.api.recipe.ingredient.IngredientValue;
import design.aeonic.nifty.api.recipe.ingredient.ModularIngredient;
import net.minecraft.world.item.ItemStack;

public class ItemIngredient extends ModularIngredient<ItemStack> {
    public ItemIngredient(IngredientValue<ItemStack> value, long requiredAmount) {
        super(value, requiredAmount);
    }

    public static ItemIngredient fromNetwork(ExtraFriendlyByteBuf buf) {
        return new ItemIngredient(valueFromNetwork(buf), buf.readVarLong());
    }

    public static IngredientValue<ItemStack> valueFromNetwork(ExtraFriendlyByteBuf buf) {
        return switch (buf.readEnum(Type.class)) {
            case STACK -> ItemStackValue.fromNetwork(buf);
            case MULTI_STACK -> ItemMultiStackValue.fromNetwork(buf);
            case TAG -> ItemTagValue.fromNetwork(buf);
            case EMPTY -> ItemEmptyValue.INSTANCE;
        };
    }

    public static ItemIngredient fromJson(JsonObject json) {
        return new ItemIngredient(valueFromJson(json), json.get("amount").getAsLong());
    }

    public static IngredientValue<ItemStack> valueFromJson(JsonObject object) {
        if (object.has("tag")) return ItemTagValue.fromJson(object);
        if (object.has("item")) {
            JsonElement item = object.get("item");
            if (item.isJsonArray()) return ItemMultiStackValue.fromJson(object);
            else return ItemStackValue.fromJson(object);
        }
        return ItemEmptyValue.INSTANCE;
    }

    @Override
    public long getAmount(ItemStack stack) {
        return stack.getCount();
    }

    @Override
    public ItemStack setAmount(ItemStack stack, long amount) {
        stack.setCount((int) amount);
        return stack;
    }

    public enum Type {
        STACK,
        MULTI_STACK,
        TAG,
        EMPTY
    }
}
