package design.aeonic.nifty.api.aspect;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class wraps a lazy value that may or may not exist, and is returned by aspect providers that
 * want to expose an aspect. This object *must* be cached by the provider and its invalidate methods
 * must be used appropriately when the object that it points to becomes invalid or its access changes.
 * The Aspect must also be created with a supplier to a non-null object; rather than returning
 * null when the object is no longer available, call {@link Aspect#invalidate()} and a null
 * value will be returned to objects consuming this Aspect.<br><br>
 *
 * When the object that an Aspect points to becomes invalid, call the {@link #invalidate()} method.
 * This method should also be called if the Aspect's access changes; for example, given a change in side
 * configuration--if the aspect was available from every block face but is now only available from the
 * north side, the Aspect should be invalidated and a new one created and cached.<br><br>
 *
 * Other game objects that use the Aspect should take care of replacing their references to the invalidated
 * aspect and obtaining a new one if desired.<br><br>
 *
 * This class is much like Forge's LazyOptional, but intended for use on both platforms.
 */
public class Aspect<T> {
    private static final Aspect<?> EMPTY = new Aspect<>(() -> null);

    private final Object lock = new Object();
    private final Supplier<T> supplier;
    private final List<Consumer<Aspect<T>>> listeners = new ArrayList<>();

    private volatile T value;
    private boolean valid = true;

    private Aspect(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <A> Aspect<A> empty() {
        return EMPTY.cast();
    }

    /**
     * Casts this Aspect to the inferred type.
     */
    @SuppressWarnings("unchecked")
    public <A> Aspect<A> cast() {
        return (Aspect<A>) this;
    }

    public <R> Optional<R> map(Function<T, R> function) {
        return get().map(function);
    }

    /**
     * If the Aspect is valid and present, passes it to the given consumer.
     */
    public void ifPresent(Consumer<? super T> consumer) {
        get().ifPresent(consumer);
    }

    /**
     * Checks whether this Aspect is valid.
     */
    public boolean isPresent() {
        return valid;
    }

    public Optional<T> get() {
        return isPresent() ? Optional.of(getNonnull()) : Optional.empty();
    }

    /**
     * Gets the contained value, asserting non-nullness.
     */
    public T getNonnull() {
        T temp = orElseNull();
        if (temp == null) throw new IllegalStateException("Aspect#getNonNull called with empty value!");
        return temp;
    }

    /**
     * Returns the contained value if it is valid and nonnull, otherwise `other`.
     */
    public T orElse(T other) {
        T temp = orElseNull();
        return temp == null ? other : temp;
    }

    /***
     * Gets the contained value, or null if the aspect is invalid.
     */
    public @Nullable T orElseNull() {
        if (!valid || supplier == null) return null;
        if (value == null) {
            synchronized (lock) {
                if (value == null) value = supplier.get();
            }
        }

        return value;
    }

    /**
     * Adds a listener to be triggered when the Aspect is invalidated, or immediately
     * if the Aspect is already invalid.
     */
    public void onInvalid(Consumer<Aspect<T>> listener) {
        synchronized (lock) {
            if (valid) listeners.add(listener);
            else listener.accept(this);
        }
    }

    /**
     * Invalidates this Aspect, making it return a null value to any object consuming it
     * and calling all attached listeners to refresh their caches or simply remove the
     * reference. This method should at the very minimum be called, for instance, in
     * a block entity's remove or unload methods. It should also be called under the
     * conditions described in {@link Aspect}.
     */
    public void invalidate() {
        if (valid) {
            synchronized (lock) {
                valid = false;
                value = null;
                listeners.forEach(c -> c.accept(this));
            }
        }
    }
}
