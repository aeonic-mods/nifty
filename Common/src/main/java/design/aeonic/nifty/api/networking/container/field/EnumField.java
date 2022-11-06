package design.aeonic.nifty.api.networking.container.field;

import design.aeonic.nifty.api.networking.container.DataField;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class EnumField<E extends Enum<E>> extends DataField<E> {
    private final Class<E> enumClass;

    public EnumField(Class<E> enumClass) {
        super();
        this.enumClass = enumClass;
    }

    public EnumField(Class<E> enumClass, @Nullable Supplier<E> getter) {
        super(getter);
        this.enumClass = enumClass;
    }

    @Override
    protected E defaultValue() {
        return enumClass.getEnumConstants()[0];
    }

    @Override
    protected short[] encode(E value) {
        return new short[]{(short) value.ordinal()};
    }

    @Override
    protected E decode(short[] data) {
        return enumClass.getEnumConstants()[data[0]];
    }

    @Override
    public int slots() {
        return 1;
    }
}