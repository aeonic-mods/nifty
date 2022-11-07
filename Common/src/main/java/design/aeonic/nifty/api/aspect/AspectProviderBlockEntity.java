package design.aeonic.nifty.api.aspect;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * A block entity that can contain and expose aspects for certain faces via {@link #getAspect(AspectType, Object)}.
 */
public interface AspectProviderBlockEntity extends AspectProvider<BlockEntity, Direction> {
}
