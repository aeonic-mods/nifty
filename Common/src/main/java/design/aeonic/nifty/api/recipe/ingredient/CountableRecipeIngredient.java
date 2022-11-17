package design.aeonic.nifty.api.recipe.ingredient;

/**
 * In general with x-stack types, the amount of the actual stack is ignored and {@link #getRequiredAmount()} is used
 * instead. Probably shoulda just reimplemented Fabric's storage variants.
 */
public interface CountableRecipeIngredient<T> extends RecipeIngredient<T> {

    /**
     * Gets the required amount of a matching ingredient.
     */
    long getRequiredAmount();
}
