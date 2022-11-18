package design.aeonic.nifty.api.recipe.output.item;

import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import design.aeonic.nifty.api.recipe.output.RecipeOutput;
import design.aeonic.nifty.api.recipe.output.RecipeOutputSet;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ItemOutputSet extends RecipeOutputSet<ItemStack> {
    public static final ItemOutputSet EMPTY = new ItemOutputSet(List.of());

    public ItemOutputSet(List<RecipeOutput<ItemStack>> recipeOutputs) {
        super(recipeOutputs);
    }

    public static ItemOutputSet fromNetwork(ExtraFriendlyByteBuf buf) {
        return new ItemOutputSet(buf.readList(ItemOutput::fromNetwork));
    }

    @Override
    protected String getPropertyName() {
        return "items";
    }

    @Override
    protected RecipeOutput<ItemStack> createOutput(ItemStack stack, float chance) {
        return new ItemOutput(stack, chance);
    }
}
