package design.aeonic.nifty.api.recipe;

import design.aeonic.nifty.api.recipe.ingredient.fluid.FluidIngredient;
import design.aeonic.nifty.api.recipe.ingredient.item.ItemIngredient;
import design.aeonic.nifty.api.recipe.output.fluid.FluidOutputSet;
import design.aeonic.nifty.api.recipe.output.item.ItemOutputSet;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface ModularRecipe<C extends Container> extends Recipe<C> {
    /**
     * Whether this recipe can require an energy cost. If true, {@link #getEnergyCost()} can still return 0; it can be
     * specified by recipe instances if desired.
     */
    default boolean canConsumeEnergy() {
        return false;
    }

    /**
     * The energy cost of this recipe, in units total (not per tick).
     */
    default long getEnergyCost() {
        return 0;
    }

    /**
     * Whether this recipe takes time to complete. If true, {@link #getProcessingTime()} can still return 0; it can be
     * specified by recipe instances if desired.
     */
    default boolean hasProcessingTime() {
        return false;
    }

    /**
     * The time it takes to complete this recipe, in ticks.
     */
    default int getProcessingTime() {
        return 0;
    }

    /**
     * The maximum number of fluid input ingredients for this recipe type. If greater than 0, {@link #getFluidIngredients()}
     * is expected not to return null. However, the number of ingredients for a given recipe instance can be less than
     * this value.
     */
    default int getMaxFluidInputs() {
        return 0;
    }

    /**
     * The maximum number of fluid outputs for this recipe type (think machine output slots).
     */
    default int getMaxFluidOutputs() {
        return 0;
    }

    /**
     * The maximum number of item input ingredients for this recipe type. If greater than 0, {@link #getItemIngredients()}
     * is expected not to return null. However, the number of ingredients for a given recipe instance can be less than
     * this value.
     */
    default int getMaxItemInputs() {
        return 0;
    }

    /**
     * The maximum number of item outputs for this recipe type (think machine output slots).
     */
    default int getMaxItemOutputs() {
        return 0;
    }

    /**
     * A list of this recipe's fluid ingredients. If null, the recipe does not use fluids.
     */
    @Nullable
    default List<FluidIngredient> getFluidIngredients() {
        return null;
    }

    /**
     * A list of this recipe's item ingredients. If null, the recipe has no item ingredients.
     */
    @Nullable
    default List<ItemIngredient> getItemIngredients() {
        return null;
    }

    /**
     * This recipe's possible fluid outputs, in the form of a {@link FluidOutputSet}.
     */
    @Nonnull
    default FluidOutputSet getFluidOutputs() {
        return FluidOutputSet.EMPTY;
    }

    /**
     * This recipe's possible item outputs, in the form of an {@link ItemOutputSet}.
     */
    @Nonnull
    default ItemOutputSet getItemOutputs() {
        return ItemOutputSet.EMPTY;
    }

    // Vanilla things

    @Override
    default NonNullList<Ingredient> getIngredients() {
        return getItemIngredients().stream().map(ItemIngredient::asIngredient).collect(NonNullList::create, NonNullList::add, NonNullList::addAll);
    }

    @Nonnull
    @Override
    default ItemStack assemble(@Nonnull C container) {
        return getItemOutputs().get(0).getStack();
    }

    @Nonnull
    @Override
    default ItemStack getResultItem() {
        return getItemOutputs().isEmpty() ? ItemStack.EMPTY : getItemOutputs().get(0).getStack();
    }

    @Override
    default boolean isSpecial() {
        return true;
    }
}
