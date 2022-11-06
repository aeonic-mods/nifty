package design.aeonic.nifty.api.networking.container.field;

import design.aeonic.nifty.api.networking.container.DataField;
import design.aeonic.nifty.api.util.DataUtils;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class BlockPosField extends DataField<BlockPos> {

    public BlockPosField() {
        super();
    }

    public BlockPosField(@Nullable Supplier<BlockPos> getter) {
        super(getter);
    }

    @Override
    protected BlockPos defaultValue() {
        return BlockPos.ZERO;
    }

    @Override
    protected short[] encode(BlockPos value) {
        return DataUtils.longToFourShorts(value.asLong());
    }

    @Override
    protected BlockPos decode(short[] data) {
        return BlockPos.of(DataUtils.longFromFourShorts(data));
    }

    @Override
    public int slots() {
        return 4;
    }

}
