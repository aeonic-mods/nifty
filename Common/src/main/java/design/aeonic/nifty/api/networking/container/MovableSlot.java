package design.aeonic.nifty.api.networking.container;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public abstract class MovableSlot extends Slot {

    public MovableSlot(Container container, int index, int x, int y) {
        super(container, index, x, y);
    }

    public abstract void setX(int x);

    public abstract void setY(int y);
}
