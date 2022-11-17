package design.aeonic.nifty.api.recipe.ingredient.item;

import com.google.gson.JsonObject;
import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.stream.Stream;

public class ItemVanillaValue implements ItemIngredientValue {
    private final Ingredient vanillaIngredient;

    public ItemVanillaValue(Ingredient vanillaIngredient) {
        this.vanillaIngredient = vanillaIngredient;
    }

    public static ItemVanillaValue fromNetwork(ExtraFriendlyByteBuf buf) {
        return new ItemVanillaValue(Ingredient.fromNetwork(buf));
    }

    public static ItemVanillaValue fromJson(JsonObject object) {
        return new ItemVanillaValue(Ingredient.fromJson(object.get("item")));
    }

    @Override
    public Ingredient asIngredient() {
        return vanillaIngredient;
    }

    @Override
    public boolean test(ItemStack itemStack) {
        return vanillaIngredient.test(itemStack);
    }

    @Override
    public Stream<ItemStack> getMatchingStacks() {
        return Arrays.stream(vanillaIngredient.getItems());
    }

    @Override
    public void toNetwork(ExtraFriendlyByteBuf buf) {
        buf.writeEnum(ItemIngredient.Type.VANILLA);
        buf.writeVarInt(vanillaIngredient.getItems().length);
        for (ItemStack stack : vanillaIngredient.getItems()) {
            buf.writeItem(stack);
        }
    }

    @Override
    public void toJson(JsonObject object) {
        object.add("item", vanillaIngredient.toJson());
    }
}
