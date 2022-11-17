package design.aeonic.nifty.api.recipe.ingredient.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import design.aeonic.nifty.api.core.Constants;
import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import design.aeonic.nifty.api.recipe.ingredient.IngredientValue;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A item ingredient value with multiple valid items. Note that it only matches one nbt value; that value is outside
 * of the item type.
 */
public class ItemMultiStackValue implements ItemIngredientValue {
    private final List<ItemStack> stacks;

    public ItemMultiStackValue(List<ItemStack> stacks) {
        this.stacks = stacks;
    }

    public static ItemMultiStackValue fromNetwork(ExtraFriendlyByteBuf buf) {
        return new ItemMultiStackValue(buf.readList(ExtraFriendlyByteBuf::readItem));
    }

    public static ItemMultiStackValue fromJson(JsonObject object) {
        List<ItemStack> stacks = new ArrayList<>();

        CompoundTag nbt = object.has("nbt") ? CompoundTag.CODEC.parse(JsonOps.INSTANCE, object.get("nbt")).getOrThrow(false, Constants.LOG::error) : null;
        JsonArray array = object.getAsJsonArray("item");
        array.forEach(element -> {
            ItemStack stack = new ItemStack(Registry.ITEM.get(new ResourceLocation(element.getAsString())), 1);
            if (nbt != null) stack.setTag(nbt);
            stacks.add(stack);
        });

        return new ItemMultiStackValue(stacks);
    }

    @Override
    public boolean test(ItemStack itemStack) {
        return stacks.stream().anyMatch(stack -> stack.sameItem(itemStack) && Objects.equals(stack.getTag(), itemStack.getTag()));
    }

    @Override
    public Stream<ItemStack> getMatchingStacks() {
        return stacks.stream();
    }

    @Override
    public Ingredient asIngredient() {
        return Ingredient.of(stacks.toArray(new ItemStack[0]));
    }

    @Override
    public void toNetwork(ExtraFriendlyByteBuf buf) {
        buf.writeEnum(ItemIngredient.Type.MULTI_STACK);
        buf.writeList(stacks, ExtraFriendlyByteBuf::writeItem);
    }

    @Override
    public void toJson(JsonObject object) {
        JsonArray array = new JsonArray();
        stacks.forEach(stack -> array.add(Registry.ITEM.getKey(stack.getItem()).toString()));
        object.add("item", array);
        stacks.stream().filter(stack -> stack.getTag() != null).findFirst().ifPresent(stack -> {
            object.add("nbt", CompoundTag.CODEC.encodeStart(JsonOps.INSTANCE, stack.getTag()).getOrThrow(false, Constants.LOG::error));
        });
    }
}
