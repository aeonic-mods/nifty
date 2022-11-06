package design.aeonic.nifty.api.networking.container;

import net.minecraft.world.inventory.ContainerData;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public abstract class DataField<T> {

    private short[] shortCache;
    private final @Nullable Supplier<T> getter;

    public DataField() {
        this(null);
    }

    public DataField(@Nullable Supplier<T> getter) {
        this.getter = getter;
    }

    /**
     * The field's initial value.
     */
    protected abstract T defaultValue();

    /**
     * @param value the input value
     * @return a short array of length equal to {@link #slots()}
     */
    protected abstract short[] encode(T value);

    /**
     * @param data the input short array of length equal to {@link #slots()}
     * @return the decoded value
     */
    protected abstract T decode(short[] data);

    /**
     * The number of short slots needed to represent this field in a {@link ContainerData} object.
     */
    public abstract int slots();

    public boolean isClientSide() {
        return getter == null;
    }

    /**
     * Gets the value of the contained field.
     */
    public T getValue() {
        if (shortCache == null) {
            return decode(shortCache = encode(defaultValue()));
        }
        return decode(shortCache);
    }

    /**
     * Sets the value of the contained field.
     */
    public void setValue(T value) {
        shortCache = encode(value);
    }

    /**
     * Writes a single short value to the field's cached data.
     * @param data the data slot's value
     * @param segment the index of the data slot, must be less than {@link #slots()}
     */
    public void write(int segment, short data) {
        if (shortCache == null) shortCache = encode(defaultValue());
        if (getter != null) {
            shortCache = encode(getter.get());
        }
        shortCache[segment] = data;
    }

    /**
     * Reads a single short value from the field's cached data. If the field has a getter (usually on a server instance), it first updates the cache from it.
     * @param segment the index of the data slot, must be less than {@link #slots()}
     * @return the value of the data slot at the given index
     */
    public short read(int segment) {
        if (shortCache == null) shortCache = encode(defaultValue());
        if (getter != null) {
            shortCache = encode(getter.get());
        }
        return shortCache[segment];
    }

}
