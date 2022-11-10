package design.aeonic.nifty.api.transfer.energy;

import design.aeonic.nifty.api.transfer.base.SimpleBattery;

public class SimpleEnergyBattery extends SimpleBattery.LongBattery implements EnergyBattery {
    public SimpleEnergyBattery(long capacity) {
        super(capacity);
    }

    public SimpleEnergyBattery(long capacity, long stored) {
        super(capacity, stored);
    }
}
