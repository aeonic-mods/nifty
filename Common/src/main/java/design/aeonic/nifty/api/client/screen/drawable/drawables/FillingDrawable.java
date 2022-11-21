package design.aeonic.nifty.api.client.screen.drawable.drawables;

import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.screen.drawable.Drawable;
import design.aeonic.nifty.api.client.screen.drawable.Texture;
import design.aeonic.nifty.api.util.Direction2D;

import java.util.function.Supplier;

public class FillingDrawable implements Drawable<Float> {
    private final Texture empty;
    private final Texture fill;
    private final Direction2D direction;

    public FillingDrawable(Texture empty, Texture fill) {
        this(empty, fill, Direction2D.RIGHT);
    }

    public FillingDrawable(Texture empty, Texture fill, Direction2D direction) {
        this.empty = empty;
        this.fill = fill;
        this.direction = direction;
    }

    @Override
    public void draw(PoseStack stack, int x, int y, int zOffset, Float context) {
        assert context >= 0f && context <= 1f;

        empty.draw(stack, x, y, zOffset);

        switch (direction) {
            case RIGHT -> {
                int fillWidth = (int) (empty.width() * context);
                fill.draw(stack, x, y, zOffset, fillWidth, fill.height());
            }
            case LEFT -> {
                int fillWidth = (int) (empty.width() * context);
                fill.drawWithUv(stack, x + (fill.width() - fillWidth), y, zOffset, fillWidth, fill.height(), fill.width() - fillWidth, 0);
            }
            case UP -> {
                int fillHeight = (int) (empty.height() * context);
                fill.drawWithUv(stack, x, y + (fill.height() - fillHeight), zOffset, fill.width(), fillHeight, 0, fill.height() - fillHeight);
            }
            case DOWN -> {
                int fillHeight = (int) (empty.height() * context);
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
