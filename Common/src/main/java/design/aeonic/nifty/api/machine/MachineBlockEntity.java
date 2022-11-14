package design.aeonic.nifty.api.machine;

import design.aeonic.nifty.api.aspect.AspectProviderBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MachineBlockEntity extends BlockEntity implements AspectProviderBlockEntity {
    public MachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract void serverTick(ServerLevel level, BlockPos pos, BlockState state);
}
