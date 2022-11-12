package design.aeonic.nifty.api.aspect;

import design.aeonic.nifty.api.core.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public interface Aspects {
    Aspects INSTANCE = Services.ASPECTS;

    /**
     * Gets an aspect of the given type from the block at the given position, or an empty aspect if the provider
     * doesn't have one (or if it can't be accessed from the given side).
     */
    <T> Aspect<T> getAspect(AspectType<T> type, Level level, BlockPos pos, Direction direction);

    /**
     * Gets an aspect of the given type from the given entity, or an empty aspect if it doesn't have one.
     */
    <T> Aspect<T> getAspect(AspectType<T> type, Entity entity);

    /**
     * Gets an aspect of the given type from the given itemstack, or an empty aspect if it doesn't have one.
     */
    <T> Aspect<T> getAspect(AspectType<T> type, ItemStack stack);

    /**
     * Registers the given aspect type. On Forge, this also registers a capability for the given type if one doesn't
     * already exist.
     */
    <T> void registerAspectType(ResourceLocation key, AspectType<T> type);

    /**
     * Returns an aspect type with the given key. If one hasn't been registered,
     * returns a dummy empty aspect type.
     */
    @Nonnull
    <T> AspectType<T> getAspectType(ResourceLocation key);

    /**
     * Runs a given consumer when an aspect type is registered with the given key.
     * If said aspect type is already registered, runs the consumer immediately.
     */
    void onAspectRegistered(ResourceLocation key, Consumer<AspectType<?>> consumer);
}
