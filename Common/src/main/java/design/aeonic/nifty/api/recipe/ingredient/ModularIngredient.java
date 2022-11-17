package design.aeonic.nifty.api.recipe.ingredient;

import com.google.gson.JsonObject;
import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;

import java.util.List;

public abstract class ModularIngredient<T> implements RecipeIngredient<T> {
    protected final IngredientValue<T> value;
    protected final long requiredAmount;

    public ModularIngredient(IngredientValue<T> value, long requiredAmount) {
        this.value = value;
        this.requiredAmount = requiredAmount;
    }

    public abstract long getAmount(T stack);

    public abstract T setAmount(T stack, long amount);

    @Override
    public boolean test(T t) {
        return value.test(t) && getAmount(t) >= getRequiredAmount();
    }

    @Override
    public List<T> getMatchingStacks() {
        return value.getMatchingStacks().map(t -> setAmount(t, getRequiredAmount())).toList();
    }

    @Override
    public long getRequiredAmount() {
        return requiredAmount;
    }

    @Override
    public void toNetwork(ExtraFriendlyByteBuf buf) {
        value.toNetwork(buf);
        buf.writeVarLong(getRequiredAmount());
    }

    @Override
    public void toJson(JsonObject object) {
        value.toJson(object);
        object.addProperty("amount", getRequiredAmount());
    }
}
