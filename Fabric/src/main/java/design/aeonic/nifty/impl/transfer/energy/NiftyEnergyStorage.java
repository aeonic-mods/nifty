package design.aeonic.nifty.impl.transfer.energy;

import design.aeonic.nifty.api.transfer.energy.EnergyBattery;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public class NiftyEnergyStorage implements EnergyStorage {
    private final EnergyBattery battery;

    public NiftyEnergyStorage(EnergyBattery battery) {
        this.battery = battery;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        long inserted = battery.insert(maxAmount, true);
        if (inserted > 0) transaction.addCloseCallback((ctx, result) -> {
            if (result.wasCommitted()) battery.insert(inserted, false);
        });
        return inserted;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        long extracted = battery.extract(maxAmount, true);
        if (extracted > 0) transaction.addCloseCallback((ctx, result) -> {
            if (result.wasCommitted()) battery.extract(extracted, false);
        });
        return extracted;
    }

    @Override
    public long getAmount() {
        return battery.getStored();
    }

    @Override
    public long getCapacity() {
        return battery.getCapacity();
    }
}
