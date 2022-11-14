package design.aeonic.nifty.api.machine.consumption;

import design.aeonic.nifty.api.machine.MachineConsumption;
import net.minecraft.nbt.CompoundTag;

public class CreativeConsumption implements MachineConsumption {
    @Override
    public boolean canRun() {
        return true;
    }

    @Override
    public void run() {}

    @Override
    public void serializeTo(CompoundTag tag) {}

    @Override
    public void deserialize(CompoundTag tag) {}
}
