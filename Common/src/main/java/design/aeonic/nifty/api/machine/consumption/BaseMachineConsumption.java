package design.aeonic.nifty.api.machine.consumption;

import design.aeonic.nifty.api.machine.MachineConsumption;

public abstract class BaseMachineConsumption implements MachineConsumption {
    private final Runnable onSetChanged;

    public BaseMachineConsumption(Runnable onSetChanged) {
        this.onSetChanged = onSetChanged;
    }

    protected void setChanged() {
        onSetChanged.run();
    }
}
