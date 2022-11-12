package design.aeonic.nifty.api.aspect;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * Defines a type of {@link Aspect} that might be provided by an {@link AspectProvider}.
 * Used for type-safe access to aspects, and for lookup registration on Fabric.
 * This is the object that's actually registered, via {@link Aspects#registerAspectType(AspectType)}.<br><br>
 *
 * Other mods can use an AspectType for soft dependencies; calling {@link Aspects#getAspectType(ResourceLocation)}
 * will return the aspect you've registered if it exists, or an empty type that will do nothing in most
 * of its methods rather than... doing something.
 */
public class AspectType<T> {
    private static final AspectType<?> EMPTY = new AspectType<>();

    private final @Nonnull Class<T> type;
    private final boolean empty;

    public AspectType(Class<T> type) {
        this.type = type;
        this.empty = false;
    }

    private AspectType() {
        this.type = null;
        this.empty = true;
    }

    public Class<T> getType() {
        return type;
    }

    public <A> boolean is(AspectType<A> type) {
        return this == type;
    }

    public boolean isFor(Class<?> type) {
        if (!isRegistered()) return false;
        return type.isAssignableFrom(this.type);
    }

    public void ifRegistered(Consumer<AspectType<T>> consumer) {
        if (isRegistered()) consumer.accept(this);
    }

    public final boolean isRegistered() {
        return !empty;
    }

    @SuppressWarnings("unchecked")
    public static <A> AspectType<A> empty() {
        return (AspectType<A>) EMPTY;
    }
}
