package design.aeonic.nifty.impl.aspect;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import design.aeonic.nifty.api.aspect.Aspect;
import design.aeonic.nifty.api.aspect.AspectProvider;
import design.aeonic.nifty.api.aspect.Aspects;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class FabricAspects implements Aspects {
    // TODO: Cleanup

    private final Map<Class<?>, BlockApiLookup<?, Direction>> blockLookupMap = new ConcurrentHashMap<>();
    private final Map<Class<?>, EntityApiLookup<?, Direction>> entityLookupMap = new ConcurrentHashMap<>();
    private final Map<Class<?>, ItemApiLookup<?, Direction>> itemLookupMap = new ConcurrentHashMap<>();

    private final Multimap<Class<?>, BlockEntityAspectLookup<?>> externalBlockLookups = LinkedHashMultimap.create();
    private final Multimap<Class<?>, EntityAspectLookup<?>> externalEntityLookups = LinkedHashMultimap.create();
    private final Multimap<Class<?>, ItemStackAspectLookup<?>> externalItemLookups = LinkedHashMultimap.create();

    private final Multimap<Class<?>, Runnable> aspectLookupQueue = LinkedHashMultimap.create();

    @Override
    public boolean exists(Class<?> aspectClass) {
        return blockLookupMap.containsKey(aspectClass);
    }

    @Override
    public <T> void registerAspect(ResourceLocation id, Class<T> aspectClass) {
        blockLookupMap.computeIfAbsent(aspectClass, clazz -> BlockApiLookup.get(id, clazz, Direction.class));
        entityLookupMap.computeIfAbsent(aspectClass, clazz -> EntityApiLookup.get(id, clazz, Direction.class));
        itemLookupMap.computeIfAbsent(aspectClass, clazz -> ItemApiLookup.get(id, clazz, Direction.class));
        // Run the queued aspect lookup methods
        aspectLookupQueue.removeAll(aspectClass).forEach(Runnable::run);
    }

    @Override
    public <T> Aspect<T> query(Class<T> aspectClass, @Nullable BlockEntity be, @Nullable Direction direction) {
        Supplier<T> internal = queryInternal(aspectClass, be, direction);
        if (internal != null) {
            Aspect<T> aspect = Aspect.of(() -> be != null && be.isRemoved() ? null : internal.get());
            AspectProvider.cast(be).addRefreshCallback(aspect::refresh);
            return aspect;
        }

        Supplier<T> ext = queryExternal(aspectClass, be, direction);
        if (ext != null) {
            Aspect<T> aspect = Aspect.of(() -> be != null && be.isRemoved() ? null : ext.get());
            AspectProvider.cast(be).addRefreshCallback(aspect::refresh);
            return aspect;
        }

        return Aspect.empty();
    }

    @Override
    public <T> Aspect<T> query(Class<T> aspectClass, Entity entity) {
        Supplier<T> internal = queryInternal(aspectClass, entity);
        if (internal != null) {
            Aspect<T> aspect = Aspect.of(() -> entity.isRemoved() ? null : internal.get());
            AspectProvider.cast(entity).addRefreshCallback(aspect::refresh);
            return aspect;
        }

        Supplier<T> ext = queryExternal(aspectClass, entity);
        if (ext != null) {
            Aspect<T> aspect = Aspect.of(() -> entity.isRemoved() ? null : ext.get());
            AspectProvider.cast(entity).addRefreshCallback(aspect::refresh);
            return aspect;
        }

        return Aspect.empty();
    }

    @Override
    public <T> Aspect<T> query(Class<T> aspectClass, ItemStack stack) {
        Supplier<T> internal = queryInternal(aspectClass, stack);
        if (internal != null) {
            Aspect<T> aspect = Aspect.of(internal);
            AspectProvider.cast(stack).addRefreshCallback(aspect::refresh);
            return aspect;
        }

        Supplier<T> ext = queryExternal(aspectClass, stack);
        if (ext != null) {
            Aspect<T> aspect = Aspect.of(ext);
            AspectProvider.cast(stack).addRefreshCallback(aspect::refresh);
            return aspect;
        }

        return Aspect.empty();
    }

    // We don't need to cache these lookups, since the actual Aspect object does the caching for us if used properly
    @SuppressWarnings("unchecked")
    public @Nullable
    <T> Supplier<T> queryInternal(Class<T> aspectClass, @Nullable BlockEntity be, @Nullable Direction direction) {
        BlockApiLookup<T, Direction> lookup = (BlockApiLookup<T, Direction>) blockLookupMap.get(aspectClass);
        if (lookup != null && be != null) {
            return () -> lookup.find(be.getLevel(), be.getBlockPos(), be.getBlockState(), be, direction);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public @Nullable
    <T> Supplier<T> queryInternal(Class<T> aspectClass, Entity entity) {
        EntityApiLookup<T, Direction> lookup = (EntityApiLookup<T, Direction>) entityLookupMap.get(aspectClass);
        if (lookup != null) {
            return () -> lookup.find(entity, null);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public @Nullable
    <T> Supplier<T> queryInternal(Class<T> aspectClass, ItemStack stack) {
        ItemApiLookup<T, Direction> lookup = (ItemApiLookup<T, Direction>) itemLookupMap.get(aspectClass);
        if (lookup != null) {
            return () -> lookup.find(stack, null);
        }
        return null;
    }

    // These casts are not actually redundant, your IDE spews slander!

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T> void registerLookup(Class<T> aspectClass, BlockEntityAspectLookup<T> callback) {
        if (!exists(aspectClass))
            aspectLookupQueue.put(aspectClass, () -> registerLookup(aspectClass, callback));
        else
            ((BlockApiLookup<T, Direction>) blockLookupMap.get(aspectClass)).registerFallback(((world, pos, state, be, dir) -> be == null || be.isRemoved() ? null : callback.find(be, dir)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T> void registerLookup(Class<T> aspectClass, EntityAspectLookup<T> callback) {
        if (!exists(aspectClass))
            aspectLookupQueue.put(aspectClass, () -> registerLookup(aspectClass, callback));
        else
            ((EntityApiLookup<T, Direction>) entityLookupMap.get(aspectClass)).registerFallback((entity, $) -> entity == null || entity.isRemoved() ? null : callback.find(entity));
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T> void registerLookup(Class<T> aspectClass, ItemStackAspectLookup<T> callback) {
        if (!exists(aspectClass))
            aspectLookupQueue.put(aspectClass, () -> registerLookup(aspectClass, callback));
        else
            ((ItemApiLookup<T, Direction>) itemLookupMap.get(aspectClass)).registerFallback((stack, $) -> stack == null || stack.isEmpty() ? null : callback.find(stack));
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T> void registerNarrowLookup(Class<T> aspectClass, BlockEntityAspectLookup<T> callback, BlockEntityType<?>... blockEntityTypes) {
        if (!exists(aspectClass))
            aspectLookupQueue.put(aspectClass, () -> registerNarrowLookup(aspectClass, callback, blockEntityTypes));
        else
            ((BlockApiLookup<T, Direction>) blockLookupMap.get(aspectClass)).registerForBlockEntities((be, dir) -> be == null || be.isRemoved() ? null : callback.find(be, dir), blockEntityTypes);
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T> void registerNarrowLookup(Class<T> aspectClass, EntityAspectLookup<T> callback, EntityType<?>... entityTypes) {
        if (!exists(aspectClass))
            aspectLookupQueue.put(aspectClass, () -> registerNarrowLookup(aspectClass, callback, entityTypes));
        else
            ((EntityApiLookup<T, Direction>) entityLookupMap.get(aspectClass)).registerForTypes((entity, $) -> entity == null || entity.isRemoved() ? null : callback.find(entity), entityTypes);
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T> void registerNarrowLookup(Class<T> aspectClass, ItemStackAspectLookup<T> callback, ItemLike... items) {
        if (!exists(aspectClass))
            aspectLookupQueue.put(aspectClass, () -> registerNarrowLookup(aspectClass, callback, items));
        else
            ((ItemApiLookup<T, Direction>) itemLookupMap.get(aspectClass)).registerForItems((stack, $) -> stack == null || stack.isEmpty() ? null : callback.find(stack), items);
    }

    @SuppressWarnings("unchecked")
    public @Nullable
    <T> Supplier<T> queryExternal(Class<T> aspectClass, BlockEntity be, Direction direction) {
        Collection<BlockEntityAspectLookup<?>> lookups = externalBlockLookups.get(aspectClass);
        for (BlockEntityAspectLookup<?> lookup : lookups) {
            T t = ((BlockEntityAspectLookup<T>) lookup).find(be, direction);
            if (t != null) return () -> ((BlockEntityAspectLookup<T>) lookup).find(be, direction);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public @Nullable
    <T> Supplier<T> queryExternal(Class<T> aspectClass, Entity entity) {
        Collection<EntityAspectLookup<?>> lookups = externalEntityLookups.get(aspectClass);
        for (EntityAspectLookup<?> lookup : lookups) {
            T t = ((EntityAspectLookup<T>) lookup).find(entity);
            if (t != null) return () -> ((EntityAspectLookup<T>) lookup).find(entity);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public @Nullable
    <T> Supplier<T> queryExternal(Class<T> aspectClass, ItemStack stack) {
        Collection<ItemStackAspectLookup<?>> lookups = externalItemLookups.get(aspectClass);
        for (ItemStackAspectLookup<?> lookup : lookups) {
            T t = ((ItemStackAspectLookup<T>) lookup).find(stack);
            if (t != null) return () -> ((ItemStackAspectLookup<T>) lookup).find(stack);
        }
        return null;
    }

    public synchronized <T> void registerExternalLookup(Class<T> aspectClass, BlockEntityAspectLookup<T> callback) {
        externalBlockLookups.put(aspectClass, callback);
    }

    public synchronized <T> void registerExternalLookup(Class<T> aspectClass, EntityAspectLookup<T> callback) {
        externalEntityLookups.put(aspectClass, callback);
    }

    public synchronized <T> void registerExternalLookup(Class<T> aspectClass, ItemStackAspectLookup<T> callback) {
        externalItemLookups.put(aspectClass, callback);
    }

}
