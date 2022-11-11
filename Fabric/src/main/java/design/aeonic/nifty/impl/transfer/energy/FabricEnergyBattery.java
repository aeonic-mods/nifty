package design.aeonic.nifty.impl.transfer.energy;

import design.aeonic.nifty.api.transfer.energy.EnergyBattery;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public class FabricEnergyBattery implements EnergyBattery {
    private final EnergyStorage energyStorage;

    public FabricEnergyBattery(EnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    @Override
    public Long getCapacity() {
        return energyStorage.getCapacity();
    }

    @Override
    public Long getStored() {
        return energyStorage.getAmount();
    }

    @Override
    public Long insert(Long amount, boolean simulate) {
        if (!energyStorage.supportsInsertion()) return 0L;
        try (Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe())) {
            long inserted = energyStorage.insert(amount, transaction);
            if (simulate) transaction.abort();
            else transaction.commit();
            return inserted;
        }
    }

    @Override
    public Long extract(Long amount, boolean simulate) {
        if (!energyStorage.supportsExtraction()) return 0L;
        try (Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe())) {
            long extracted = energyStorage.extract(amount, transaction);
            if (simulate) transaction.abort();
            else transaction.commit();
            return extracted;
        }
    }
}
