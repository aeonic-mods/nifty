package design.aeonic.nifty.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MachineBlockEntityRedstoneControllable extends MachineBlockEntity {
    private RedstoneControl redstoneControl = RedstoneControl.ALWAYS;
    private boolean pulsed = false;

    public MachineBlockEntityRedstoneControllable(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public RedstoneControl getRedstoneControl() {
        return redstoneControl;
    }

    public void setRedstoneControl(RedstoneControl redstoneControl) {
        this.redstoneControl = redstoneControl;
        setChanged();
    }

    @Override
    public boolean canRun() {
        return switch (redstoneControl) {
            case ALWAYS ->  true;
            case HIGH -> level.hasNeighborSignal(getBlockPos());
            case LOW -> !level.hasNeighborSignal(getBlockPos());
            case NEVER -> false;
            case PULSE -> {
                if (level.hasNeighborSignal(getBlockPos())) {
                    if (!pulsed) {
                        setChanged();
                        yield pulsed = true;
                    }
                    else yield false;
                } else yield pulsed = false;
            }
        } && super.canRun();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        redstoneControl = RedstoneControl.valueOf(tag.getString("RedstoneControl"));
        pulsed = tag.getBoolean("Pulsed");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putString("RedstoneControl", redstoneControl.name());
        tag.putBoolean("Pulsed", pulsed);
    }
}
