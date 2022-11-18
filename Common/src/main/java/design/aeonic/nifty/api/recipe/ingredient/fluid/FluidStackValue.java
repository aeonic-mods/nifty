package design.aeonic.nifty.api.recipe.ingredient.fluid;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import design.aeonic.nifty.api.core.Constants;
import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import design.aeonic.nifty.api.recipe.ingredient.IngredientValue;
import design.aeonic.nifty.api.transfer.fluid.FluidStack;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import java.util.stream.Stream;

public class FluidStackValue implements IngredientValue<FluidStack> {
    private final FluidStack stack;

    public FluidStackValue(FluidStack stack) {
        this.stack = stack;
    }

    public static FluidStackValue fromNetwork(ExtraFriendlyByteBuf buf) {
        return new FluidStackValue(buf.readFluidStack());
    }

    public static FluidStackValue fromJson(JsonObject object) {
        Fluid fluid = Registry.FLUID.get(new ResourceLocation(object.get("fluid").getAsString()));
        CompoundTag nbt = object.has("nbt") ? CompoundTag.CODEC.parse(JsonOps.INSTANCE, object.get("nbt")).resultOrPartial(Constants.LOG::error).orElse(null) : null;
        return new FluidStackValue(FluidStack.of(fluid, 1, nbt));
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        return fluidStack.canStack(stack);
    }

    @Override
    public Stream<FluidStack> getMatchingStacks() {
        return Stream.of(stack);
    }

    @Override
    public void toNetwork(ExtraFriendlyByteBuf buf) {
        buf.writeEnum(FluidIngredient.Type.STACK);
        buf.writeFluidStack(stack);
    }

    @Override
    public void toJson(JsonObject object) {
        object.addProperty("fluid", Registry.FLUID.getKey(stack.getFluid()).toString());
        if (stack.getTag() != null) {
            CompoundTag.CODEC.encodeStart(JsonOps.INSTANCE, stack.getTag()).resultOrPartial(Constants.LOG::error).ifPresent(tag -> object.add("nbt", tag));
        }
    }
}
