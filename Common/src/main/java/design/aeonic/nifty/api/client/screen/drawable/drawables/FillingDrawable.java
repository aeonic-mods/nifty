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

    private float fillRed;
    private float fillGreen;
    private float fillBlue;
    private float fillAlpha;

    public FillingDrawable(Texture empty, Texture fill) {
        this(empty, fill, Direction2D.RIGHT);
    }

    public FillingDrawable(Texture empty, Texture fill, Direction2D direction) {
        this(empty, fill, direction, 1, 1, 1, 1);
    }

    public FillingDrawable(Texture empty, Texture fill, Direction2D direction, float fillRed, float fillGreen, float fillBlue, float fillAlpha) {
        this.empty = empty;
        this.fill = fill;
        this.direction = direction;
        this.fillRed = fillRed;
        this.fillGreen = fillGreen;
        this.fillBlue = fillBlue;
        this.fillAlpha = fillAlpha;
    }

    public FillingDrawable copyWithColor(float red, float green, float blue, float alpha) {
        return new FillingDrawable(empty, fill, direction, red, green, blue, alpha);
    }

    public Texture getEmptyTexture() {
        return empty;
    }

    public Texture getFillTexture() {
        return fill;
    }

    @Override
    public void draw(PoseStack stack, int x, int y, int zOffset, Float context) {
        assert context >= 0f && context <= 1f;

        empty.draw(stack, x, y, zOffset);

        switch (direction) {
            case RIGHT -> {
                int fillWidth = (int) (empty.width() * context);
                fill.draw(stack, x, y, zOffset, fillWidth, fill.height(), fillRed, fillGreen, fillBlue, fillAlpha, fillAlpha != 1);
            }
            case LEFT -> {
                int fillWidth = (int) (empty.width() * context);
                fill.drawWithUv(stack, x + (fill.width() - fillWidth), y, zOffset, fillWidth, fill.height(), fill.width() - fillWidth, 0, fillRed, fillGreen, fillBlue, fillAlpha, fillAlpha != 1);
            }
            case UP -> {
                int fillHeight = (int) (empty.height() * context);
                fill.drawWithUv(stack, x, y + (fill.height() - fillHeight), zOffset, fill.width(), fillHeight, 0, fill.height() - fillHeight, fillRed, fillGreen, fillBlue, fillAlpha, fillAlpha != 1);
            }
            case DOWN -> {
                int fillHeight = (int) (empty.height() * context);
                fill.draw(stack, x, y, zOffset, fill.width(), fillHeight, fillRed, fillGreen, fillBlue, fillAlpha, fillAlpha != 1);
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
