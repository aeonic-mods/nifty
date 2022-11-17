package design.aeonic.nifty.api.recipe.ingredient.item;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import design.aeonic.nifty.api.core.Constants;
import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import design.aeonic.nifty.api.recipe.ingredient.IngredientValue;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;
import java.util.stream.Stream;

public class ItemStackValue implements IngredientValue<ItemStack> {
    private final ItemStack stack;

    public ItemStackValue(ItemStack stack) {
        this.stack = stack;
    }

    public static ItemStackValue fromNetwork(ExtraFriendlyByteBuf buf) {
        return new ItemStackValue(buf.readItem());
    }

    public static ItemStackValue fromJson(JsonObject object) {
        ItemStack stack = new ItemStack(Registry.ITEM.get(new ResourceLocation(object.get("item").getAsString())));
        if (object.has("nbt")) stack.setTag(CompoundTag.CODEC.parse(JsonOps.INSTANCE, object.get("nbt")).getOrThrow(false, Constants.LOG::error));
        return new ItemStackValue(stack);
    }

    @Override
    public boolean test(ItemStack stack) {
        return stack.sameItem(this.stack) && Objects.equals(stack.getTag(), this.stack.getTag());
    }

    @Override
    public Stream<ItemStack> getMatchingStacks() {
        return Stream.of(stack);
    }

    @Override
    public void toNetwork(ExtraFriendlyByteBuf buf) {
        buf.writeEnum(ItemIngredient.Type.STACK);
        buf.writeItem(stack);
    }

    @Override
    public void toJson(JsonObject object) {
        object.addProperty("Item", Registry.ITEM.getKey(stack.getItem()).toString());
        if (stack.getTag() != null) {
            object.add("nbt", CompoundTag.CODEC.encodeStart(JsonOps.INSTANCE, stack.getTag()).getOrThrow(false, Constants.LOG::error));
        }
    }
}
