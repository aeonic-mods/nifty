package design.aeonic.nifty.api.machine;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineMenu<T extends MachineMenu<T>> extends AbstractContainerMenu {
    protected int containerCount;
    protected final Inventory playerInventory;

    protected MachineMenu(MenuType<T> type, int containerCount, int syncId, Inventory playerInventory) {
        super(type, syncId);
        this.containerCount = containerCount;
        this.playerInventory = playerInventory;
    }

    /**
     * Not actually called automatically; just a convenience method.
     */
    protected void addPlayerSlots() {
        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack movingStack = slot.getItem();
            ret = movingStack.copy();
            if (index >= containerCount) {
                boolean triedMove = false;
                for (int i = 0; i < containerCount; i++) {
                    if (slots.get(i).mayPlace(movingStack)) {
                        if (!moveItemStackTo(movingStack, i, i + 1, false))
                            return ItemStack.EMPTY;
                        triedMove = true;
                    }
                }
                if (!triedMove) {
                    if (index < 27 + containerCount) {
                        if (!moveItemStackTo(movingStack, 31, 40, false))
                            return ItemStack.EMPTY;
                    } else if (index >= 27 + containerCount && index < 36 + containerCount && !moveItemStackTo(movingStack, containerCount, 27 + containerCount, false))
                        return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(movingStack, containerCount, 36 + containerCount, false))
                return ItemStack.EMPTY;

            if (movingStack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();

            if (movingStack.getCount() == ret.getCount()) return ItemStack.EMPTY;

            slot.onTake(player, movingStack);
        }

        return ret;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
