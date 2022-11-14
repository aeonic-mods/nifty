package design.aeonic.nifty.api.machine.consumption;

import design.aeonic.nifty.api.core.Services;
import design.aeonic.nifty.api.machine.MachineConsumption;
import design.aeonic.nifty.api.transfer.item.ItemStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

/**
 * Consumes fuel items, furnace style.
 */
public class FuelItemConsumption extends BaseMachineConsumption {
    private final Supplier<ItemStorage> itemHandler;
    private final int slot;

    private int litTime = 0;
    private int litDuration = 0;

    public FuelItemConsumption(Supplier<ItemStorage> itemHandler, int slot, Runnable onSetChanged) {
        super(onSetChanged);
        this.itemHandler = itemHandler;
        this.slot = slot;
    }

    public int getLitTime() {
        return litTime;
    }

    public int getLitDuration() {
        return litDuration;
    }

    @Override
    public boolean canRun() {
        return itemHandler.get() != null && (litTime > 0 || Services.ACCESS.getBurnTime(itemHandler.get().extract(slot, 1, true)) > 0);
    }

    @Override
    public void run() {
        if (litTime > 0) {
            litTime--;
        } else {
            ItemStack stack = itemHandler.get().extract(slot, 1, false);
            if (stack.isEmpty()) return;
            if (stack.getItem().hasCraftingRemainingItem()) {
                itemHandler.get().insert(slot, new ItemStack(stack.getItem().getCraftingRemainingItem()), false);
            }
            litDuration = Services.ACCESS.getBurnTime(stack);
            litTime = litDuration;
        }
        setChanged();
    }

    @Override
    public void serializeTo(CompoundTag tag) {
        tag.putInt("LitTime", litTime);
        tag.putInt("LitDuration", litDuration);
    }

    @Override
    public void deserialize(CompoundTag tag) {
        litTime = tag.getInt("LitTime");
        litDuration = tag.getInt("LitDuration");
    }
}
