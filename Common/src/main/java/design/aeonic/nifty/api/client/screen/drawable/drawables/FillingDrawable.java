package design.aeonic.nifty.api.client.screen.drawable.drawables;

import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.screen.drawable.Drawable;
import design.aeonic.nifty.api.client.screen.drawable.Texture;
import design.aeonic.nifty.api.util.Direction2D;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

public class FillingDrawable implements Drawable<Float> {
    private final Texture empty;
    private final Texture fill;
    private final Direction2D direction;
    private final @Nullable Function<Float, Float> processor;

    public FillingDrawable(Texture empty, Texture fill) {
        this(empty, fill, Direction2D.RIGHT);
    }

    public FillingDrawable(Texture empty, Texture fill, Direction2D direction) {
        this(empty, fill, direction, null);
    }

    public FillingDrawable(Texture empty, Texture fill, Direction2D direction, Function<Float, Float> processor) {
        this.empty = empty;
        this.fill = fill;
        this.direction = direction;
        this.processor = processor;
    }

    @Override
    public void draw(PoseStack stack, int x, int y, int zOffset, Float context) {
        assert context >= 0f && context <= 1f;

        empty.draw(stack, x, y, zOffset);

        float progress = processor == null ? context : processor.apply(context);
        switch (direction) {
            case RIGHT -> {
                int fillWidth = (int) (empty.width() / progress);
                fill.draw(stack, x, y, zOffset, fillWidth, fill.height());
            }
            case LEFT -> {
                int fillWidth = (int) (empty.width() / progress);
                fill.draw(stack, x + (fill.width() - fillWidth), y, zOffset, fillWidth, fill.height());
            }
            case UP -> {
                int fillHeight = (int) (empty.height() / progress);
                fill.draw(stack, x, y + (fill.height() - fillHeight), zOffset, fill.width(), fillHeight);
            }
            case DOWN -> {
                int fillHeight = (int) (empty.height() / progress);
                fill.draw(stack, x, y, zOffset, fill.width(), fillHeight);
            }
        }
    }

    /**
     * Returns a {@link StaticDrawable} that renders this drawable with the given context.
     * For convenient float context definitions, see {@link design.aeonic.nifty.api.util.Progress}.
     */
    @Override
    public StaticDrawable with(Supplier<Float> contextSupplier) {
        return Drawable.super.with(contextSupplier);
    }
}
