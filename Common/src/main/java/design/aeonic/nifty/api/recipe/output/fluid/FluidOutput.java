package design.aeonic.nifty.api.recipe.output.fluid;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import design.aeonic.nifty.api.core.Constants;
import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import design.aeonic.nifty.api.recipe.output.RecipeOutput;
import design.aeonic.nifty.api.transfer.fluid.FluidStack;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class FluidOutput extends RecipeOutput<FluidStack> {
    public FluidOutput(FluidStack stack) {
        super(stack);
    }

    public FluidOutput(FluidStack stack, float chance) {
        super(stack, chance);
    }

    public static FluidOutput of(FluidStack stack) {
        return of(stack, 1f);
    }

    public static FluidOutput of(FluidStack stack, float chance) {
        return new FluidOutput(stack, chance);
    }

    public static FluidOutput fromNetwork(ExtraFriendlyByteBuf buf) {
        return new FluidOutput(buf.readFluidStack(), buf.readFloat());
    }

    public static FluidOutput fromJson(JsonObject json) {
        FluidStack stack = FluidStack.of(Registry.FLUID.get(new ResourceLocation(json.get("fluid").getAsString())), json.get("amount").getAsInt());
        if (json.has("nbt")) {
            CompoundTag.CODEC.parse(JsonOps.INSTANCE, json.get("nbt")).result().ifPresent(stack::setTag);
        }
        return new FluidOutput(stack, json.has("chance") ? json.get("chance").getAsFloat(): 1f);
    }

    public FluidOutputSet with(FluidStack stack) {
        return with(stack, 1f);
    }

    public FluidOutputSet with(FluidStack stack, float chance) {
        return new FluidOutputSet(List.of(this, new FluidOutput(stack, chance)));
    }

    public FluidOutputSet set() {
        return new FluidOutputSet(List.of(this));
    }

    @Override
    public void stackToNetwork(ExtraFriendlyByteBuf buf, FluidStack stack) {
        buf.writeFluidStack(stack);
    }

    @Override
    public void stackToJson(JsonObject json, FluidStack stack) {
        json.addProperty("fluid", Registry.FLUID.getKey(stack.getFluid()).toString());
        json.addProperty("amount", stack.getAmount());
        if (stack.getTag() != null) {
            CompoundTag.CODEC.encodeStart(JsonOps.INSTANCE, stack.getTag()).resultOrPartial(Constants.LOG::error).ifPresent(tag -> json.add("nbt", tag));
        }
    }
}
