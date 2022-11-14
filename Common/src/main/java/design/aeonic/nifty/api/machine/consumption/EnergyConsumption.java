package design.aeonic.nifty.api.machine.consumption;

import design.aeonic.nifty.api.machine.MachineConsumption;
import design.aeonic.nifty.api.transfer.energy.EnergyBattery;
import net.minecraft.nbt.CompoundTag;

import java.util.function.Supplier;

/**
 * Consumes a mutable amount of energy per tick.
 */
public class EnergyConsumption extends BaseMachineConsumption {
    private final Supplier<EnergyBattery> battery;
    private long energyPerTick;

    public EnergyConsumption(Supplier<EnergyBattery> battery, long energyPerTick, Runnable onSetChanged) {
        super(onSetChanged);
        this.battery = battery;
        this.energyPerTick = energyPerTick;
    }

    public long getEnergyPerTick() {
        return energyPerTick;
    }

    public void setEnergyPerTick(long energyPerTick) {
        this.energyPerTick = energyPerTick;
    }

    @Override
    public boolean canRun() {
        return battery.get().extract(energyPerTick, true) == energyPerTick;
    }

    @Override
    public void run() {
        battery.get().extract(energyPerTick, false);
        setChanged();
    }

    @Override
    public void serializeTo(CompoundTag tag) {
        tag.putLong("EnergyPerTick", energyPerTick);
    }

    @Override
    public void deserialize(CompoundTag tag) {
        energyPerTick = tag.getLong("EnergyPerTick");
    }
}
