package design.aeonic.nifty.api.transfer;

import design.aeonic.nifty.api.transfer.base.DelegateStorage;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault

/**
 * Describes a storage aspect for any type, ie itemstacks or {@link design.aeonic.nifty.api.transfer.fluid.FluidStack}.
 */
public interface Storage<T> {
    /**
     * The number of slots in this storage.
     */
    int getSlots();

    /**
     * Gets the stack in the given slot.
     */
    T getStackInSlot(int slot);

    /**
     * Gets the max stack size for the given slot.
     */
    long getSlotCapacity(int slot);

    /**
     * Checks whether the given stack is *ever* valid for the given slot, should be used as a preliminary check
     * instead of simulated insertion.
     */
    boolean isEverValid(int slot, T stack);

    /**
     * Inserts the given stack and returns the remainder. The stack passed to this method must not be modified.
     * If simulate is true, doesn't modify the actual storage contents.
     */
    T insert(@Nonnull T stack, boolean simulate);

    /**
     * Inserts the given stack to the given slot and returns the remainder. The stack passed to this method must not be modified.
     * If simulate is true, doesn't modify the actual storage contents.
     */
    T insert(int slot, @Nonnull T stack, boolean simulate);

    T extract(Predicate<T> filter, long amount, boolean simulate);

    /**
     * Extracts the given amount from the given slot.
     * If simulate is true, doesn't modify the actual storage contents.
     */
    T extract(int slot, long amount, boolean simulate);

}
