package design.aeonic.nifty.api.recipe.ingredient.item;

import com.google.gson.JsonObject;
import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import design.aeonic.nifty.api.recipe.ingredient.IngredientValue;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.stream.Stream;

public class ItemTagValue implements ItemIngredientValue {
    private final TagKey<Item> tagKey;

    public ItemTagValue(TagKey<Item> tagKey) {
        this.tagKey = tagKey;
    }

    public static ItemTagValue fromNetwork(ExtraFriendlyByteBuf buf) {
        return new ItemTagValue(buf.readTag(Registry.ITEM_REGISTRY));
    }

    public static ItemTagValue fromJson(JsonObject object) {
        return new ItemTagValue(TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(object.get("tag").getAsString())));
    }

    @Override
    public boolean test(ItemStack itemStack) {
        return itemStack.is(tagKey);
    }

    @Override
    public Stream<ItemStack> getMatchingStacks() {
        return Registry.ITEM.getOrCreateTag(tagKey).stream().filter(Holder::isBound).map(Holder::value).map(ItemStack::new);
    }

    @Override
    public Ingredient asIngredient() {
        return Ingredient.of(tagKey);
    }

    @Override
    public void toNetwork(ExtraFriendlyByteBuf buf) {
        buf.writeEnum(ItemIngredient.Type.TAG);
        buf.writeTag(tagKey);
    }

    @Override
    public void toJson(JsonObject object) {
        object.addProperty("tag", tagKey.toString());
    }
}
