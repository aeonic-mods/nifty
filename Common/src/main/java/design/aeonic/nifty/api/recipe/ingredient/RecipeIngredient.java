package design.aeonic.nifty.api.recipe.ingredient;

import com.google.gson.JsonObject;
import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;

import java.util.List;
import java.util.function.Predicate;

/**
 * A more versatile Ingredient system for use in recipes with any type (fluids, items etc).
 * In general with x-stack types, the amount of the actual stack is ignored and {@link #getRequiredAmount()} is used
 * instead. Probably shoulda just reimplemented Fabric's storage variants.
 */
public interface RecipeIngredient<T> extends Predicate<T> {

    /**
     * Gets all possible matches.
     */
    List<T> getMatchingStacks();

    /**
     * Gets the required amount of a matching ingredient.
     */
    long getRequiredAmount();

    /**
     * Serializes this ingredient to a byte buffer.
     */
    void toNetwork(ExtraFriendlyByteBuf buf);

    /**
     * Serializes this ingredient to the given JSON object.
     */
    void toJson(JsonObject object);
}
