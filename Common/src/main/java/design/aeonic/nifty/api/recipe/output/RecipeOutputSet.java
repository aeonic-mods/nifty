package design.aeonic.nifty.api.recipe.output;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class RecipeOutputSet<T>  {
    private final List<RecipeOutput<T>> outputs = new ArrayList<>();

    public RecipeOutputSet(List<RecipeOutput<T>> outputs) {
        // We need a mutable list so we copy this one
        this.outputs.addAll(outputs);
    }

    protected abstract String getPropertyName();

    protected abstract RecipeOutput<T> createOutput(T stack, float chance);

    public List<T> roll(RandomSource random) {
        List<T> rolled = new ArrayList<>();
        outputs.stream().map(out -> out.roll(random)).forEach(opt -> opt.ifPresent(rolled::add));
        return rolled;
    }

    public List<T> roll(RandomSource random, Function<RecipeOutput<T>, T> otherwise) {
        List<T> rolled = new ArrayList<>();
        outputs.stream().map(out -> out.roll(random, otherwise.apply(out))).forEach(rolled::add);
        return rolled;
    }

    public RecipeOutput<T> get(int index) {
        return outputs.get(index);
    }

    public final boolean isEmpty() {
        return outputs.isEmpty();
    }

    public final int size() {
        return outputs.size();
    }

    public final List<RecipeOutput<T>> getOutputs() {
        return outputs;
    }


    public RecipeOutputSet<T> with(T stack) {
        return with(stack, 1f);
    }

    public RecipeOutputSet<T> with(T stack, float chance) {
        return with(createOutput(stack, chance));
    }

    public RecipeOutputSet<T> with(RecipeOutput<T> output) {
        add(output);
        return this;
    }

    public void add(T stack) {
        add(stack, 1f);
    }

    public void add(T stack, float chance) {
        outputs.add(createOutput(stack, chance));
    }

    public void add(RecipeOutput<T> output) {
        outputs.add(output);
    }

    public void toNetwork(ExtraFriendlyByteBuf buf) {
        buf.writeVarInt(outputs.size());
        for (RecipeOutput<T> output : outputs) {
            output.stackToNetwork(buf);
        }
    }

    public void toJson(JsonObject json) {
        JsonArray array = new JsonArray();
        for (RecipeOutput<T> output : outputs) {
            JsonObject object = new JsonObject();
            output.stackToJson(object);
            array.add(object);
        }
        json.add(getPropertyName(), array);
    }
}
