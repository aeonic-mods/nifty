package design.aeonic.nifty.api.recipe.ingredient.fluid;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import design.aeonic.nifty.api.core.Constants;
import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import design.aeonic.nifty.api.recipe.ingredient.IngredientValue;
import design.aeonic.nifty.api.transfer.fluid.FluidStack;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A fluid ingredient value with multiple valid fluids. Note that it only matches one nbt value; that value is outside
 * of the fluid type.
 */
public class FluidMultiStackValue implements IngredientValue<FluidStack> {
    private final List<FluidStack> stacks;

    public FluidMultiStackValue(List<FluidStack> stacks) {
        this.stacks = stacks;
    }

    public static FluidMultiStackValue fromNetwork(ExtraFriendlyByteBuf buf) {
        return new FluidMultiStackValue(buf.readList(ExtraFriendlyByteBuf::readFluidStack));
    }

    public static FluidMultiStackValue fromJson(JsonObject object) {
        List<FluidStack> stacks = new ArrayList<>();

        CompoundTag nbt = object.has("nbt") ? CompoundTag.CODEC.parse(JsonOps.INSTANCE, object.get("nbt")).getOrThrow(false, Constants.LOG::error) : null;
        JsonArray array = object.getAsJsonArray("fluid");
        array.forEach(element -> stacks.add(FluidStack.of(Registry.FLUID.get(new ResourceLocation(element.getAsString())), 1, nbt)));

        return new FluidMultiStackValue(stacks);
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        return stacks.stream().anyMatch(fluidStack::canStack);
    }

    @Override
    public Stream<FluidStack> getMatchingStacks() {
        return stacks.stream();
    }

    @Override
    public void toNetwork(ExtraFriendlyByteBuf buf) {
        buf.writeEnum(FluidIngredient.Type.MULTI_STACK);
        buf.writeList(stacks, ExtraFriendlyByteBuf::writeFluidStack);
    }

    @Override
    public void toJson(JsonObject object) {
        JsonArray array = new JsonArray();
        stacks.forEach(stack -> array.add(Registry.FLUID.getKey(stack.getFluid()).toString()));
        object.add("fluid", array);
        stacks.stream().filter(stack -> stack.getTag() != null).findFirst().ifPresent(stack -> {
            object.add("nbt", CompoundTag.CODEC.encodeStart(JsonOps.INSTANCE, stack.getTag()).getOrThrow(false, Constants.LOG::error));
        });
    }
}
