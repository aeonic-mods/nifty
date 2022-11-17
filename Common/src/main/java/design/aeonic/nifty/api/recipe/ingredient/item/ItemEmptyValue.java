package design.aeonic.nifty.api.recipe.ingredient.item;

import com.google.gson.JsonObject;
import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import design.aeonic.nifty.api.recipe.ingredient.IngredientValue;
import design.aeonic.nifty.api.recipe.ingredient.fluid.FluidIngredient;
import design.aeonic.nifty.api.transfer.fluid.FluidStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.stream.Stream;

public class ItemEmptyValue implements IngredientValue<ItemStack> {
    public static final ItemEmptyValue INSTANCE = new ItemEmptyValue();

    @Override
    public boolean test(ItemStack stack) {
        return false;
    }

    @Override
    public Stream<ItemStack> getMatchingStacks() {
        return Stream.empty();
    }

    @Override
    public void toNetwork(ExtraFriendlyByteBuf buf) {
        buf.writeEnum(ItemIngredient.Type.EMPTY);
    }

    @Override
    public void toJson(JsonObject object) {}
}
