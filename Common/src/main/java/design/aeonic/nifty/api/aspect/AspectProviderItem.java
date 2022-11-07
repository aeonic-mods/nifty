package design.aeonic.nifty.api.aspect;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * An item that can contain and expose aspects.
 */
public interface AspectProviderItem extends AspectProvider<Item, ItemStack> {
}
