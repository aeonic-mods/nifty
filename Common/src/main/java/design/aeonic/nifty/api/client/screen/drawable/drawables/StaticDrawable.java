package design.aeonic.nifty.api.client.screen.drawable.drawables;

import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.screen.drawable.Drawable;

@FunctionalInterface
public
interface StaticDrawable extends Drawable<Void> {
    @Override
    default void draw(PoseStack stack, int x, int y, int zOffset, Void context) {
        draw(stack, x, y, zOffset);
    }

    void draw(PoseStack stack, int x, int y, int zOffset);
}
