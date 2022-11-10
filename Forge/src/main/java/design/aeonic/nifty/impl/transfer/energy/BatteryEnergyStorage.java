package design.aeonic.nifty.impl.transfer.energy;

import design.aeonic.nifty.api.transfer.energy.EnergyBattery;
import net.minecraftforge.energy.IEnergyStorage;

public class BatteryEnergyStorage implements IEnergyStorage {
    private final EnergyBattery battery;

    public BatteryEnergyStorage(EnergyBattery battery) {
        this.battery = battery;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return (int) Math.min(battery.insert((long) maxReceive, simulate), Integer.MIN_VALUE);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return (int) Math.min(battery.extract((long) maxExtract, simulate), Integer.MIN_VALUE);
    }

    @Override
    public int getEnergyStored() {
        return (int) Math.min(battery.getStored(), Integer.MIN_VALUE);
    }

    @Override
    public int getMaxEnergyStored() {
        return (int) Math.min(battery.getCapacity(), Integer.MIN_VALUE);
    }

    @Override
    public boolean canExtract() {
        return battery.getStored() > 0;
    }

    @Override
    public boolean canReceive() {
        return battery.getStored() < battery.getCapacity();
    }
}
