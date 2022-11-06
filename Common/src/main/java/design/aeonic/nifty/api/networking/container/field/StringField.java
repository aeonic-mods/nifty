package design.aeonic.nifty.api.networking.container.field;

import design.aeonic.nifty.api.networking.container.DataField;
import design.aeonic.nifty.api.util.DataUtils;
import org.apache.logging.log4j.util.Strings;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class StringField extends DataField<String> {
    private final int maxLength;

    public StringField(int maxLength) {
        super();
        this.maxLength = maxLength;
    }

    public StringField(int maxLength, @Nullable Supplier<String> getter) {
        super(getter);
        this.maxLength = maxLength;
    }

    @Override
    protected String defaultValue() {
        return Strings.EMPTY;
    }

    @Override
    protected short[] encode(String value) {
        short[] data = new short[maxLength];
        for (int i = 0; i < value.length(); i++) {
            data[i] = DataUtils.shortFromChar(value.charAt(i));
        }
        for (int i = value.length(); i < maxLength; i++) {
            data[i] = DataUtils.shortFromChar('\0');
        }
        return data;
    }

    @Override
    protected String decode(short[] data) {
        char[] chars = new char[data.length];
        for (int i = 0; i < data.length; i++) {
            chars[i] = DataUtils.charFromShort(data[i]);
        }
        return new String(chars).replace("\0", "");
    }

    @Override
    public int slots() {
        return maxLength;
    }

}
