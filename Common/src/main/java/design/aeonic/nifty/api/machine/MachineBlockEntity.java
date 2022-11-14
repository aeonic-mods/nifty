package design.aeonic.nifty.api.machine;

import design.aeonic.nifty.api.aspect.AspectProviderBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class MachineBlockEntity extends BlockEntity implements AspectProviderBlockEntity {
    public MachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /**
     * Returns the machine consumption for this blockentity, which automatically calls its methods before execution
     * and serializes/deserializes it with the blockentity. If null is returned, does nothing and assumes the canRun
     * check would always assume true.
     */
    public abstract @Nullable MachineConsumption getMachineConsumption();

    /**
     * Checks whether the machine can run this tick.
     */
    public boolean canRun() {
        return getMachineConsumption() == null || getMachineConsumption().canRun();
    }

    /**
     * Called every server tick if the checks in {@link #canRun()} pass, and after {@link MachineConsumption#run()} if
     * the machine consumption for this machine is nonnull.
     */
    public abstract void runMachine(ServerLevel level, BlockPos pos, BlockState state);

    /**
     * Called every tick on the server.
     */
    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        if (canRun()) {
            if (getMachineConsumption() != null) getMachineConsumption().run();
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (getMachineConsumption() != null) getMachineConsumption().deserialize(tag, "Consumption");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (getMachineConsumption() != null) getMachineConsumption().serializeTo(tag, "Consumption");
    }
}
