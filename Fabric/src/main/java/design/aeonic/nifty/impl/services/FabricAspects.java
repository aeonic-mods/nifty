package design.aeonic.nifty.impl.services;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;
import design.aeonic.nifty.api.aspect.*;
import design.aeonic.nifty.api.services.Aspects;
import design.aeonic.nifty.impl.aspect.ApiLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class FabricAspects implements Aspects {
    private final BiMap<ResourceLocation, AspectType<?>> aspectTypes = HashBiMap.create();
    private final Map<AspectType<?>, AspectMapper<?, ?>> aspectTypeMappers = new HashMap<>();
    private final Multimap<ResourceLocation, Consumer<AspectType<?>>> registryListeners = ArrayListMultimap.create();

    private final Map<AspectType<?>, ApiLookup<?>> apiLookups = new HashMap<>();
    private final Map<AspectType<?>, ApiLookup<?>> mappedApiLookups = new HashMap<>();

    public <A, T> void registerMapped(ResourceLocation key, AspectType<A> aspectType, Class<T> apiClass, Function<A, T> commonToFabric, Function<T, A> fabricToCommon) {
        aspectTypes.put(key, aspectType);
        aspectTypeMappers.put(aspectType, new AspectMapper<>(aspectType, apiClass, commonToFabric, fabricToCommon));

        ApiLookup<T> apiLookup = new ApiLookup<>(key, apiClass);
        mappedApiLookups.put(aspectType, apiLookup);

        for (Consumer<AspectType<?>> listener : registryListeners.get(key)) {
            listener.accept(aspectType);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Aspect<T> getAspect(AspectType<T> type, Level level, BlockPos pos, Direction direction) {
        if (level.getBlockEntity(pos) instanceof AspectProviderBlockEntity provider) return provider.getAspect(type, direction);

        if (apiLookups.containsKey(type)) {
            T obj = ((ApiLookup<T>) apiLookups.get(type)).get(level, pos, direction);
            if (obj != null) return Aspect.of(() -> obj);
        }
        else if (aspectTypeMappers.containsKey(type)) {
            T obj = getMapped((AspectMapper<T, ?>) aspectTypeMappers.get(type), mappedApiLookups.get(type), level, pos, direction);
            if (obj != null) return Aspect.of(() -> obj);
        }

        return Aspect.empty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Aspect<T> getAspect(AspectType<T> type, Entity entity) {
        if (entity instanceof AspectProviderEntity provider) return provider.getAspect(type, null);

        if (apiLookups.containsKey(type)) {
            T obj = ((ApiLookup<T>) apiLookups.get(type)).get(entity);
            if (obj != null) return Aspect.of(() -> obj);
        }
        else if (aspectTypeMappers.containsKey(type)) {
            T obj = getMapped((AspectMapper<T, ?>) aspectTypeMappers.get(type), mappedApiLookups.get(type), entity);
            if (obj != null) return Aspect.of(() -> obj);
        }

        return Aspect.empty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Aspect<T> getAspect(AspectType<T> type, ItemStack stack) {
        if (stack.getItem() instanceof AspectProviderItem provider) return provider.getAspect(type, stack);

        if (apiLookups.containsKey(type)) {
            T obj = ((ApiLookup<T>) apiLookups.get(type)).get(stack);
            if (obj != null) return Aspect.of(() -> obj);
        }
        else if (aspectTypeMappers.containsKey(type)) {
            T obj = getMapped((AspectMapper<T, ?>) aspectTypeMappers.get(type), mappedApiLookups.get(type), stack);
            if (obj != null) return Aspect.of(() -> obj);
        }

        return Aspect.empty();
    }

    @SuppressWarnings("unchecked")
    private <A, T> A getMapped(AspectMapper<A, ?> mapper, ApiLookup<T> apiLookup, Level level, BlockPos pos, Direction direction) {
        T obj = apiLookup.get(level, pos, direction);
        if (obj != null) return ((AspectMapper<A, T>) mapper).fabricToCommon().apply(obj);
        return null;
    }

    @SuppressWarnings("unchecked")
    private <A, T> A getMapped(AspectMapper<A, ?> mapper, ApiLookup<T> apiLookup, Entity entity) {
        T obj = apiLookup.get(entity);
        if (obj != null) return ((AspectMapper<A, T>) mapper).fabricToCommon().apply(obj);
        return null;
    }

    @SuppressWarnings("unchecked")
    private <A, T> A getMapped(AspectMapper<A, ?> mapper, ApiLookup<T> apiLookup, ItemStack stack) {
        T obj = apiLookup.get(stack);
        if (obj != null) return ((AspectMapper<A, T>) mapper).fabricToCommon().apply(obj);
        return null;
    }

    @Override
    public <T> void registerAspectType(ResourceLocation key, AspectType<T> type) {
        aspectTypes.put(key, type);
        apiLookups.put(type, new ApiLookup<>(key, type.getType()));

        if (registryListeners.containsKey(key)) {
            for (Consumer<AspectType<?>> listener : registryListeners.removeAll(key)) {
                listener.accept(type);
            }
        }
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <T> AspectType<T> getAspectType(ResourceLocation key) {
        return (AspectType<T>) aspectTypes.get(key);
    }

    @Override
    public void onAspectRegistered(ResourceLocation key, Consumer<AspectType<?>> consumer) {
        registryListeners.put(key, consumer);
    }

    private record AspectMapper<A, T>(AspectType<A> aspectType, Class<T> apiClass, Function<A, T> commonToFabric, Function<T, A> fabricToCommon) {}
}
