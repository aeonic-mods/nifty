package design.aeonic.nifty.api.networking.container;

import net.minecraft.world.inventory.ContainerData;
import org.spongepowered.asm.mixin.injection.invoke.arg.ArgumentIndexOutOfBoundsException;

/**
 * Simple implementation of {@link ContainerData} that can be used to store different types (defined by {@link DataField}s)
 * without worrying about packing for the network.<br><br>
 * Stolen from an API I started writing and gave up on lol
 */
public class ContainerFields implements ContainerData {

    private final DataField<?>[] fields;
    private final int count;

    public ContainerFields(DataField<?>... fields) {
        this.fields = fields;

        int i = 0;
        for (DataField<?> field: fields) {
            i += field.slots();
        }
        this.count = i;
    }

    public boolean isClientSide() {
        for (DataField<?> field : fields) {
            if (!field.isClientSide()) return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public <T> T getField(int index) {
        return (T) fields[index].getValue();
    }

    @Override
    public int get(int slot) {
        assert slot < count;
        int i = 0;
        for (DataField<?> field: fields) {
            if (i <= slot && i + field.slots() > slot) {
                return field.read(slot - i);
            }
            i += field.slots();
        }
        throw new ArgumentIndexOutOfBoundsException(slot);
    }

    @Override
    public void set(int slot, int value) {
        assert slot < count;
        int i = 0;
        for (DataField<?> field: fields) {
            if (i <= slot && i + field.slots() > slot) {
                field.write(slot - i, (short) value);
                return;
            }
            i += field.slots();
        }
        throw new ArgumentIndexOutOfBoundsException(slot);
    }

    @Override
    public int getCount() {
        return count;
    }

}
