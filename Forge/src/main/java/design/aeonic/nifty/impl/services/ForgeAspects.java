package design.aeonic.nifty.impl.services;

import design.aeonic.nifty.api.aspect.*;
import design.aeonic.nifty.api.aspect.Aspects;
import design.aeonic.nifty.impl.aspect.ForgeAspect;
import design.aeonic.nifty.impl.mixin.access.CapabilityManagerAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import org.objectweb.asm.Type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class ForgeAspects implements Aspects {
    // TODO: Too many god damn maps this is a mess

    private final ConcurrentMap<ResourceLocation, AspectType<?>> aspectTypes = new ConcurrentHashMap<>();
    private final Map<AspectType<?>, Capability<?>> aspectCapabilities = new ConcurrentHashMap<>();
    private final Map<Capability<?>, AspectType<?>> capabilityAspects = new ConcurrentHashMap<>();
    private final Map<ResourceLocation, List<Consumer<AspectType<?>>>> registryListeners = new HashMap<>();

    private final ConcurrentMap<AspectType<?>, AspectCapability<?, ?>> mappedAspectTypes = new ConcurrentHashMap<>();
    private final ConcurrentMap<Capability<?>, AspectCapability<?, ?>> mappedAspectCapabilities = new ConcurrentHashMap<>();

    /**
     * Registers an aspect type that maps to an existing capability and vice versa. As an example, this is used to register
     * common item handlers that can be mapped to Forge's existing item handlers
     */
    public <T, F> void registerMapped(ResourceLocation key, AspectType<T> type, Capability<F> capability, Function<T, F> commonToForge, Function<F, T> forgeToCommon) {
        aspectTypes.put(key, type);
        mappedAspectTypes.put(type, new AspectCapability<>(type, capability, commonToForge, forgeToCommon));
        mappedAspectCapabilities.put(capability, new AspectCapability<>(type, capability, commonToForge, forgeToCommon));
        if (registryListeners.containsKey(key)) registryListeners.remove(key).forEach(c -> c.accept(type));
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        aspectTypes.values().forEach(type -> registerCapability(event, type));
    }

    private <T> void registerCapability(RegisterCapabilitiesEvent event, AspectType<T> type) {
        // FIXME: Probably not safe
        Capability<T> cap = ((CapabilityManagerAccess) (Object) CapabilityManager.INSTANCE).callGet(Type.getInternalName(type.getType()), false);
        aspectCapabilities.put(type, cap);
        capabilityAspects.put(cap, type);
        if (!cap.isRegistered()) event.register(type.getType());
    }

    /**
     * If the provider has an aspect of the given type, returns it as a capability. Unfortunately with the current
     * implementation this bypasses laziness of Aspects returned from providers. // FIXME: aspect laziness
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T, CTX> LazyOptional<T> getAsCap(AspectProvider<?, CTX> provider, Capability<T> cap, CTX context) {
        if (capabilityAspects.containsKey(cap)) {
            AspectType<T> type = (AspectType<T>) capabilityAspects.get(cap);
            Aspect<T> aspect = provider.getAspect(type, context);
            if (aspect.isPresent()) {
                LazyOptional<T> opt = LazyOptional.of(aspect::getNonnull);
                aspect.onInvalid($ -> opt.invalidate());
                return opt;
            }
        } else if (mappedAspectCapabilities.containsKey(cap)) {
            AspectCapability<T, ?> aspectCap = (AspectCapability<T, ?>) mappedAspectCapabilities.get(cap);
            Aspect<T> aspect = provider.getAspect(aspectCap.aspectType(), context);
            if (aspect.isPresent()) {
                LazyOptional<T> opt = LazyOptional.of(() -> (T) aspectCap.commonToForge().apply(aspect.getNonnull()));
                aspect.onInvalid($ -> opt.invalidate());
                return opt;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Aspect<T> getAspect(AspectType<T> type, Level level, BlockPos pos, Direction direction) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) return Aspect.empty();

        if (blockEntity instanceof AspectProviderBlockEntity provider) return provider.getAspect(type, direction);
        else if (mappedAspectTypes.containsKey(type)) return getMappedAspect(blockEntity, mappedAspectTypes.get(type), direction);
        else return new ForgeAspect<>(blockEntity.getCapability((Capability<T>) aspectCapabilities.get(type), direction));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Aspect<T> getAspect(AspectType<T> type, Entity entity) {
        if (entity == null) return Aspect.empty();

        if (entity instanceof AspectProviderEntity provider) return provider.getAspect(type, null);
        else if (mappedAspectTypes.containsKey(type)) return getMappedAspect(entity, mappedAspectTypes.get(type), null);
        else return new ForgeAspect<>((LazyOptional<T>) entity.getCapability(aspectCapabilities.get(type)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Aspect<T> getAspect(AspectType<T> type, ItemStack stack) {
        if (stack == null || stack.isEmpty()) return Aspect.empty();

        if (stack.getItem() instanceof AspectProviderItem provider) return provider.getAspect(type, stack);
        else if (mappedAspectTypes.containsKey(type)) return getMappedAspect(stack, mappedAspectTypes.get(type), null);
        else return new ForgeAspect<>((LazyOptional<T>) stack.getCapability(aspectCapabilities.get(type)));
    }

    @SuppressWarnings("unchecked")
    private <T, A, C> Aspect<A> getMappedAspect(CapabilityProvider<?> provider, AspectCapability<?, ?> aspectCapability, Direction side) {
        AspectCapability<A, C> aspectCap = (AspectCapability<A, C>) aspectCapability;
        return new ForgeAspect<>(provider.getCapability(aspectCap.capability(), side).lazyMap(aspectCap.forgeToCommon()::apply));
    }

    @Override
    public <T> void registerAspectType(ResourceLocation key, AspectType<T> type) {
        aspectTypes.put(key, type);
        if (registryListeners.containsKey(key)) registryListeners.remove(key).forEach(c -> c.accept(type));
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public <T> AspectType<T> getAspectType(ResourceLocation key) {
        return (AspectType<T>) aspectTypes.getOrDefault(key, AspectType.empty());
    }

    @Override
    public void onAspectRegistered(ResourceLocation key, Consumer<AspectType<?>> consumer) {
        if (aspectTypes.containsKey(key)) consumer.accept(aspectTypes.get(key));
        else registryListeners.computeIfAbsent(key, k -> new ArrayList<>()).add(consumer);
    }

    public record AspectCapability<A, T>(AspectType<A> aspectType, Capability<T> capability, Function<A, T> commonToForge, Function<T, A> forgeToCommon) {}
}
