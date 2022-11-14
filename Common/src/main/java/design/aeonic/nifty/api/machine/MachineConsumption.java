package design.aeonic.nifty.api.machine;

import net.minecraft.nbt.CompoundTag;

/**
 * Describes a machine's consumption, be it power, items etc, with serialization and deserialization methods
 * for required data.<br><br>
 */
public interface MachineConsumption {

    /**
     * Whether the machine can run this tick.
     */
    boolean canRun();

    /**
     * Consumes whatever necessary for the machine to run this tick. Only called if {@link #canRun()} is true.
     */
    void run();

    default CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        serializeTo(tag);
        return tag;
    }

    default void serializeTo(CompoundTag tag, String key) {
        CompoundTag myTag = tag.getCompound(key);
        serializeTo(myTag);
        tag.put(key, myTag);
    }

    void serializeTo(CompoundTag tag);

    default void deserialize(CompoundTag tag, String key) {
        deserialize(tag.getCompound(key));
    }

    void deserialize(CompoundTag tag);
}
