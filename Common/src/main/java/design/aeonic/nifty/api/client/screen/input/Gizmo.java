package design.aeonic.nifty.api.client.screen.input;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Simplified input widget system used for node rendering and other GUIs.
 */
public interface Gizmo {

    default void onClose(GizmoScreen screen) {}

    default void onLostFocus(GizmoScreen screen) {}

    void draw(PoseStack stack, GizmoScreen screen, int mouseX, int mouseY, float partialTicks);

    @Nullable
    default List<Component> getTooltip(GizmoScreen screen, int mouseX, int mouseY) {
        return null;
    }

    default TooltipStyle getTooltipStyle(GizmoScreen screen, int mouseX, int mouseY) {
        return TooltipStyle.NIFTY;
    }

    /**
     * Called only if {@link #isEnabled()} is true and the mouse is over the widget.
     */
    default boolean mouseDown(GizmoScreen screen, int mouseX, int mouseY, int button) {
        return false;
    }

    /**
     * Called for mouse up anywhere regardless of focus.
     */
    default boolean mouseUp(GizmoScreen screen, int mouseX, int mouseY, int button) {
        return false;
    }

    /**
     * Called only if this widget is focused (that is, this widget is {@link GizmoScreen#getFocusedWidget()}) and
     * {@link #isEnabled()} is true.
     */
    default boolean keyDown(GizmoScreen screen, int keyCode, int scanCode, int modifiers) {
        return false;
    }

    /**
     * Called only if this widget is focused (that is, this widget is {@link GizmoScreen#getFocusedWidget()}) and
     * {@link #isEnabled()} is true.
     */
    default boolean keyUp(GizmoScreen screen, int keyCode, int scanCode, int modifiers) {
        return false;
    }

    /**
     * Called only if this widget is hovered and {@link #isEnabled()} is true.
     */
    default boolean mouseScrolled(GizmoScreen screen, int mouseX, int mouseY, double scrollDelta) {
        return false;
    }

    void setX(int x);

    void setY(int y);

    default boolean isWithinBounds(int x, int y) {
        return isWithinBounds(getX(), getY(), getWidth(), getHeight(), x, y);
    }

    default boolean isWithinBounds(int startX, int startY, int width, int height, int checkX, int checkY) {
        return checkX >= startX && checkX < startX + width && checkY >= startY && checkY < startY + height;
    }

    default int getRight() {
        return getX() + getWidth();
    }

    default int getBottom() {
        return getY() + getHeight();
    }

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    boolean isEnabled();

}
