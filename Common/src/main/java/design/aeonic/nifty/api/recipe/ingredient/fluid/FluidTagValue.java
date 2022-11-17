package design.aeonic.nifty.api.recipe.ingredient.fluid;

import com.google.gson.JsonObject;
import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import design.aeonic.nifty.api.recipe.ingredient.IngredientValue;
import design.aeonic.nifty.api.transfer.fluid.FluidStack;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.stream.Stream;

public class FluidTagValue implements IngredientValue<FluidStack> {
    private final TagKey<Fluid> tagKey;

    public FluidTagValue(TagKey<Fluid> tagKey) {
        this.tagKey = tagKey;
    }

    public static FluidTagValue fromNetwork(ExtraFriendlyByteBuf buf) {
        return new FluidTagValue(buf.readTag(Registry.FLUID_REGISTRY));
    }

    public static FluidTagValue fromJson(JsonObject object) {
        return new FluidTagValue(TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation(object.get("tag").getAsString())));
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        return fluidStack.getFluid().is(tagKey);
    }

    @Override
    public Stream<FluidStack> getMatchingStacks() {
        return Registry.FLUID.getOrCreateTag(tagKey).stream().filter(Holder::isBound).map(Holder::value).map(FluidStack::of);
    }

    @Override
    public void toNetwork(ExtraFriendlyByteBuf buf) {
        buf.writeEnum(FluidIngredient.Type.TAG);
        buf.writeTag(tagKey);
    }

    @Override
    public void toJson(JsonObject object) {
        object.addProperty("tag", tagKey.toString());
    }
}
