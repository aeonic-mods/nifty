package design.aeonic.nifty.api.transfer;

/**
 * Represents a storage of any type that contains only a numerical value: for example, energy or mana.
 */
public interface Battery<T extends Number> {

    /**
     * Gets the maximum capacity of the battery.
     */
    T getCapacity();

    /**
     * Gets the current amount of energy stored in the battery.
     */
    T getStored();

    /**
     * Inserts into the battery. If simulate is true, the actual battery's stored value will not be changed.
     */
    T insert(T amount, boolean simulate);

    /**
     * Extracts from the battery. If simulate is true, the actual battery's stored value will not be changed.
     */
    T extract(T amount, boolean simulate);
}
