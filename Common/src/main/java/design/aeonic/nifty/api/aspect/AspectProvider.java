package design.aeonic.nifty.api.aspect;

import java.util.Optional;

/**
 * Represents a game object that can contain and expose aspects; for instance, a blockentity.
 * @param <T> the type of the implementing class; ie {@link net.minecraft.world.level.block.entity.BlockEntity}
 * @param <CTX> the context type: passed when obtaining an aspect; ie for blockentities this is {@link net.minecraft.core.Direction}
 */
public interface AspectProvider<T, CTX> {
    @SuppressWarnings("unchecked")
    default T self() {
        return (T) this;
    }

    /**
     * If the given object is an aspect provider, returns it as such; otherwise returns an empty optional.
     */
    @SuppressWarnings("unchecked")
    static <P extends AspectProvider<? super P, ?>> Optional<P> as(Class<P> type, Object object) {
        return type.isAssignableFrom(object.getClass()) ? Optional.of((P) object) : Optional.empty();
    }

    /**
     * Gets an aspect of the given type, or an empty aspect if the provider doesn't have one or if it
     * can't be accessed with the given context.
     */
    default <A> Aspect<A> getAspect(AspectType<T> type, CTX context) {
        return Aspect.empty();
    }
}
