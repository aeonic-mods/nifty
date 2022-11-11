package design.aeonic.nifty.impl.aspect;

import design.aeonic.nifty.api.aspect.AspectProviderBlockEntity;
import design.aeonic.nifty.api.aspect.AspectProviderEntity;
import design.aeonic.nifty.api.aspect.AspectProviderItem;
import design.aeonic.nifty.api.aspect.AspectType;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
public class ApiLookup<T> {
    private final BlockApiLookup<T, Direction> blockLookup;
    private final EntityApiLookup<T, Void> entityLookup;
    private final ItemApiLookup<T, ContainerItemContext> itemLookup;

    public ApiLookup(Class<T> apiClass, ResourceLocation key) {
        this(apiClass, key, key, key);
    }

    public ApiLookup(Class<T> apiClass, ResourceLocation blockLookupKey, ResourceLocation entityLookupKey, ResourceLocation itemLookupKey) {
        this.blockLookup = BlockApiLookup.get(blockLookupKey, apiClass, Direction.class);
        this.entityLookup = EntityApiLookup.get(entityLookupKey, apiClass, Void.class);
        this.itemLookup = ItemApiLookup.get(itemLookupKey, apiClass, ContainerItemContext.class);
    }

    public void register(AspectType<T> aspectType) {
        blockLookup.registerFallback((level, pos, state, be, direction) -> (be instanceof AspectProviderBlockEntity provider) ? provider.getAspect(aspectType, direction).orElseNull() : null);
        entityLookup.registerFallback((entity, context) -> (entity instanceof AspectProviderEntity provider) ? provider.getAspect(aspectType, null).orElseNull() : null);
        itemLookup.registerFallback((stack, context) -> (stack.getItem() instanceof AspectProviderItem provider) ? provider.getAspect(aspectType, null).orElseNull() : null);
    }

    public <A> void register(AspectType<A> aspectType, Function<A, T> mapper) {
        blockLookup.registerFallback((level, pos, state, be, direction) -> (be instanceof AspectProviderBlockEntity provider) ? provider.getAspect(aspectType, direction).map(mapper).orElse(null) : null);
        entityLookup.registerFallback((entity, context) -> (entity instanceof AspectProviderEntity provider) ? provider.getAspect(aspectType, null).map(mapper).orElse(null) : null);
        itemLookup.registerFallback((stack, context) -> (stack.getItem() instanceof AspectProviderItem item) ? item.getAspect(aspectType, stack).map(mapper).orElse(null) : null);
    }

    public @Nullable T get(Level level, BlockPos pos, Direction direction) {
        return blockLookup.find(level, pos, direction);
    }

    public @Nullable T get(Entity entity) {
        return entityLookup.find(entity, null);
    }

    public @Nullable T get(ItemStack stack) {
        return itemLookup.find(stack, null);
    }
}
