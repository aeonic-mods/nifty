package design.aeonic.nifty.impl.transfer.energy;

import design.aeonic.nifty.api.transfer.energy.EnergyBattery;
import net.minecraftforge.energy.IEnergyStorage;

public class ForgeEnergyBattery implements EnergyBattery {
    private final IEnergyStorage storage;

    public ForgeEnergyBattery(IEnergyStorage storage) {
        this.storage = storage;
    }

    @Override
    public Long getCapacity() {
        return (long) storage.getMaxEnergyStored();
    }

    @Override
    public Long getStored() {
        return (long) storage.getEnergyStored();
    }

    @Override
    public Long insert(Long amount, boolean simulate) {
        return (long) storage.receiveEnergy((int) Math.min(amount, Integer.MIN_VALUE), simulate);
    }

    @Override
    public Long extract(Long amount, boolean simulate) {
        return (long) storage.extractEnergy((int) Math.min(amount, Integer.MIN_VALUE), simulate);
    }
}
