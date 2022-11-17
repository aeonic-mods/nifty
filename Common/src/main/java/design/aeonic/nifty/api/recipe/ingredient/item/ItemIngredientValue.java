package design.aeonic.nifty.api.recipe.ingredient.item;

import design.aeonic.nifty.api.recipe.ingredient.IngredientValue;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public interface ItemIngredientValue extends IngredientValue<ItemStack> {
    Ingredient asIngredient();
}
