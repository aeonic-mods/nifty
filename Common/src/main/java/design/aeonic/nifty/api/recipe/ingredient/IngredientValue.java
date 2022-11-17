package design.aeonic.nifty.api.recipe.ingredient;

import com.google.gson.JsonObject;
import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * The inner part of a {@link ModularIngredient}, analogous to the Vanilla {@link net.minecraft.world.item.crafting.Ingredient.Value}.
 * Essentially an ingredient predicate that disregards required amounts.
 */
public interface IngredientValue<T> extends Predicate<T> {

    /**
     * Checks if the given stack matches this value, <b>disregarding its current amount.</b>
     */
    @Override
    boolean test(T t);

    /**
     * Gets all possible matches.
     */
    Stream<T> getMatchingStacks();

    /**
     * Serializes this ingredient value to a byte buffer.
     */
    void toNetwork(ExtraFriendlyByteBuf buf);

    /**
     * Serializes this ingredient value to the given JSON object.
     */
    void toJson(JsonObject object);

}
