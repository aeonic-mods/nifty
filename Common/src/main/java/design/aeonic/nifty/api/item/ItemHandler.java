package design.aeonic.nifty.api.item;

import design.aeonic.nifty.api.util.Wrappers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.BiPredicate;

/**
 * An aspect that allows providers to contain items which can be inserted and extracted in a specified manner.<br><br>
 * If you're utilizing an ItemHandler object anywhere you want it to be available to other machines/blocks/whatever,
 * you should probably run it through {@link Wrappers#itemHandler(ItemHandler)} to wrap it
 * in an implementation that will work properly with other Forge and Fabric systems.
 */
public interface ItemHandler {

    /**
     * Checks whether the given stack can be inserted into the slot at the given index.
     */
    boolean allowedInSlot(int slot, ItemStack stack);

    /**
     * Same as {@link #insert(int, ItemStack, boolean)}, but inserts to the first available slot, filling empty slots and
     * stacking with whatever it can.
     */
    ItemStack insert(ItemStack stack, boolean simulate);

    /**
     * Attempts to insert an item stac. Returns the remainder of the inserted stack.
     *
     * @param slot     the slot index to insert to
     * @param stack    the stack to insert
     * @param simulate if true, doesn't make any changes to stored contents
     * @return what's left of the inserted item stack - if nothing is inserted, will equal the passed stack
     * @throws IndexOutOfBoundsException if the slot index is invalid.
     */
    ItemStack insert(int slot, ItemStack stack, boolean simulate);

    /**
     * Attempts to extract the given amount from any slot of the given resource.
     *
     * @param itemPredicate a predicate that must be met for a stack to be extracted
     * @param amount        the amount to extract
     * @param simulate      if true, doesn't make any changes to stored contents
     * @return the extracted item stack
     */
    ItemStack extract(BiPredicate<ItemLike, CompoundTag> itemPredicate, int amount, boolean simulate);

    /**
     * Attempts to extract the given amount from the given slot, returning the extracted stack.In most cases,
     * {@link #extract(BiPredicate, int, boolean)} should be used instead for better performance on Forgen't.<br><br>
     *
     * @param slot     the slot index to extract from
     * @param amount   the amount to extract
     * @param simulate if true, doesn't make any changes to stored contents
     * @return the extracted stack; an empty stack if extraction failed or the slot is empty
     * @throws IndexOutOfBoundsException if the slot index is invalid.
     */
    ItemStack extract(int slot, int amount, boolean simulate);

    /**
     * Returns the number of slots.
     */
    int getNumSlots();

    /**
     * Gets the max amount a given slot can hold.
     */
    default int getCapacity(int slot) {
        return 64;
    }

    /**
     * Gets the stack at the given index. Should <b>only</b> be used for viewing. Do not modify the returned stack!
     */
    ItemStack get(int index);

    /**
     * Directly sets the stack at the given index. <b>Must not be used outside of internal compat implementations
     * (ie on Fabric to roll back changes when a transaction is aborted).</b><br><br>
     * If you're implementing your own item handler, you do need to provide a working implementation of this method
     * for those cases where it's needed.
     */
    void set(int slot, ItemStack stack);

    /**
     * Writes this item handler's slot contents to a compound tag.
     */
    CompoundTag serialize();

    /**
     * Reads and reconstructs this item handler's slot contents from a given compound tag.
     */
    void deserialize(CompoundTag tag);

    /**
     * Writes this item handler's slot contents to a network packet.
     */
    void toNetwork(FriendlyByteBuf buf);

    /**
     * Reads and reconstructs this item handler's slot contents from a network packet.
     */
    void fromNetwork(FriendlyByteBuf buf);

    /**
     * Returns a Container wrapping this ItemHandler. Can only be used for internal item handlers; that is, those
     * you've created yourself and not any obtained via the Aspect system.
     */
    default ItemHandlerContainer asContainer() {
        return new ItemHandlerContainer(this);
    }

}
