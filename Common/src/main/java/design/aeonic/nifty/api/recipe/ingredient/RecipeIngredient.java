package design.aeonic.nifty.api.recipe.ingredient;

import com.google.gson.JsonObject;
import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;

import java.util.List;
import java.util.function.Predicate;

/**
 * A more versatile Ingredient system for use in recipes with any type (fluids, items etc).
 * This is the top-level ingredient interface; most normal (item and fluid, etc) ingredients will extends {@link CountableRecipeIngredient}
 * to check for the amount of the ingredient.
 */
public interface RecipeIngredient<T> extends Predicate<T> {

    /**
     * Gets all possible matches.
     */
    List<T> getMatchingStacks();

    /**
     * Serializes this ingredient to a byte buffer.
     */
    void toNetwork(ExtraFriendlyByteBuf buf);

    /**
     * Serializes this ingredient to the given JSON object.
     */
    void toJson(JsonObject object);
}
