package design.aeonic.nifty.api.client.screen.drawable;

import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.screen.drawable.drawables.StaticDrawable;

import java.util.function.Supplier;

@FunctionalInterface
public interface Drawable<CTX> {

    void draw(PoseStack stack, int x, int y, int zOffset, CTX context);

    default Placed<CTX> at(int x, int y) {
        return at(x, y, 200);
    }

    default Placed<CTX> at(int x, int y, int zOffset) {
        return new SimplePlaced<>(this, x, y, zOffset);
    }

    /**
     * Returns a {@link StaticDrawable} that renders this drawable with the given context.
     */
    default StaticDrawable with(Supplier<CTX> contextSupplier) {
        return new Contextual<>(this, contextSupplier);
    }

    interface Placed<CTX> {
        /**
         * Draw at the stored position, with the passed context.
         */
        void draw(PoseStack stack, CTX context);

        default Placed<CTX> at(int x, int y) {
            moveTo(x, y);
            return this;
        }

        default Placed<CTX> at(int x, int y, int zOffset) {
            moveTo(x, y, zOffset);
            return this;
        }

        default void moveTo(int x, int y) {
            setX(x);
            setY(y);
        }

        default void moveTo(int x, int y, int zOffset) {
            setX(x);
            setY(y);
            setZOffset(zOffset);
        }

        /**
         * Sets the x position of this instance.
         */
        void setX(int x);

        /**
         * Sets the y position of this instance.
         */
        void setY(int y);

        /**
         * Sets the blit offset of this instance.
         */
        void setZOffset(int z);
    }

    class SimplePlaced<CTX> implements Placed<CTX> {
        private final Drawable<CTX> drawable;
        private int x;
        private int y;
        private int zOffset;

        public SimplePlaced(Drawable<CTX> drawable, int x, int y, int zOffset) {
            this.drawable = drawable;
            this.x = x;
            this.y = y;
            this.zOffset = zOffset;
        }

        /**
         * Draw with a null context. Only call if you are sure the given drawable won't throw an error when given a null
         * value for CTX: eg {@link StaticDrawable}.
         */
        public void draw(PoseStack stack) {
            draw(stack, this.zOffset, null);
        }

        /**
         * Draw with a null context. Only call if you are sure the given drawable won't throw an error when given a null
         * value for CTX: eg {@link StaticDrawable}.
         */
        public void draw(PoseStack stack, int zOffset) {
            draw(stack, zOffset, null);
        }

        /**
         * Draw at the stored position with the given context.
         */
        @Override
        public void draw(PoseStack stack, CTX context) {
            draw(stack, this.zOffset, context);
        }

        public void draw(PoseStack stack, int zOffset, CTX context) {
            drawable.draw(stack, x, y, zOffset, context);
        }

        @Override
        public void setX(int x) {
            this.x = x;
        }

        @Override
        public void setY(int y) {
            this.y = y;
        }

        @Override
        public void setZOffset(int z) {
            this.zOffset = z;
        }
    }

    class Contextual<CTX> implements StaticDrawable {
        private final Drawable<CTX> drawable;
        private final Supplier<CTX> contextSupplier;

        public Contextual(Drawable<CTX> drawable, Supplier<CTX> contextSupplier) {
            this.drawable = drawable;
            this.contextSupplier = contextSupplier;
        }

        @Override
        public void draw(PoseStack stack, int x, int y, int zOffset) {
            drawable.draw(stack, x, y, zOffset, contextSupplier.get());
        }
    }
}
