package design.aeonic.nifty.api.recipe.output.item;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import design.aeonic.nifty.api.core.Constants;
import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import design.aeonic.nifty.api.recipe.output.RecipeOutput;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ItemOutput extends RecipeOutput<ItemStack> {
    public ItemOutput(ItemStack stack) {
        super(stack);
    }

    public ItemOutput(ItemStack stack, float chance) {
        super(stack, chance);
    }

    public static ItemOutput of(ItemStack stack) {
        return of(stack, 1f);
    }

    public static ItemOutput of(ItemStack stack, float chance) {
        return new ItemOutput(stack, chance);
    }

    public static ItemOutput fromNetwork(ExtraFriendlyByteBuf buf) {
        return new ItemOutput(buf.readItem(), buf.readFloat());
    }

    public static ItemOutput fromJson(JsonObject json) {
        ItemStack stack = new ItemStack(Registry.ITEM.get(new ResourceLocation(json.get("item").getAsString())), json.get("count").getAsInt());
        if (json.has("nbt")) {
            CompoundTag.CODEC.parse(JsonOps.INSTANCE, json.get("nbt")).result().ifPresent(stack::setTag);
        }
        return new ItemOutput(stack, json.has("chance") ? json.get("chance").getAsFloat(): 1f);
    }

    public ItemOutputSet with(ItemStack stack) {
        return with(stack, 1f);
    }

    public ItemOutputSet with(ItemStack stack, float chance) {
        return new ItemOutputSet(List.of(this, new ItemOutput(stack, chance)));
    }

    public ItemOutputSet set() {
        return new ItemOutputSet(List.of(this));
    }

    @Override
    public void stackToNetwork(ExtraFriendlyByteBuf buf, ItemStack stack) {
        buf.writeItem(stack);
    }

    @Override
    public void stackToJson(JsonObject json, ItemStack stack) {
        json.addProperty("item", Registry.ITEM.getKey(stack.getItem()).toString());
        json.addProperty("count", stack.getCount());
        if (stack.hasTag()) {
            CompoundTag.CODEC.encodeStart(JsonOps.INSTANCE, stack.getTag()).resultOrPartial(Constants.LOG::error).ifPresent(tag -> json.add("nbt", tag));
        }
    }
}
