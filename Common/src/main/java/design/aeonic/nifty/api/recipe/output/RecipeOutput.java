package design.aeonic.nifty.api.recipe.output;

import com.google.gson.JsonObject;
import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import net.minecraft.util.RandomSource;

import java.util.Optional;

/**
 * Describes an output of any type for a {@link design.aeonic.nifty.api.recipe.ModularRecipe}, with a given chance..
 */
public abstract class RecipeOutput<T> {
    private final T stack;
    private final float chance;

    public RecipeOutput(T stack, float chance) {
        this.stack = stack;
        this.chance = chance;
    }

    public RecipeOutput(T stack) {
        this(stack, 1f);
    }

    public abstract void stackToNetwork(ExtraFriendlyByteBuf buf, T stack);

    public abstract void stackToJson(JsonObject json, T stack);

    /**
     * Rolls this output, returning it if the roll succeeds or <code>otherwise</code> if it fails.
     */
    public T roll(RandomSource random, T otherwise) {
        return random.nextFloat() < chance ? stack : otherwise;
    }

    /**
     * Rolls this output.
     */
    public Optional<T> roll(RandomSource random) {
        return random.nextFloat() <= chance ? Optional.of(stack) : Optional.empty();
    }

    /**
     * The plain output stack, disregarding random rolls.
     */
    public T getStack() {
        return stack;
    }

    /**
     * The chance of this output being rolled.
     */
    public float getChance() {
        return chance;
    }

    public void stackToNetwork(ExtraFriendlyByteBuf buf) {
        stackToNetwork(buf, stack);
        buf.writeFloat(chance);
    }

    public void stackToJson(JsonObject json) {
        stackToJson(json, stack);
        if (chance != 1f) json.addProperty("chance", chance);
    }
}
