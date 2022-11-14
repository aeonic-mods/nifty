package design.aeonic.nifty.api.client.screen.input.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.Texture;
import design.aeonic.nifty.api.client.screen.input.AbstractInputWidget;
import design.aeonic.nifty.api.client.screen.input.WidgetScreen;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.glfw.GLFW;

public class SettingsInputWidget extends AbstractInputWidget {
    public static final Texture HIGHLIGHT = new Texture("nifty:textures/gui/input_widgets_extended.png", 64, 64, 16, 16, 28, 48);

    public static final Texture NORMAL = new Texture("nifty:textures/gui/input_widgets_extended.png", 64, 64, 14, 14, 0, 49);

    public static final Texture CLICKED = new Texture("nifty:textures/gui/input_widgets_extended.png", 64, 64, 14, 14, 14, 49);

    protected final Runnable action;
    protected final Runnable shiftAction;

    protected boolean clicked = false;

    public SettingsInputWidget(int x, int y, Runnable action) {
        this(x, y, action, action);
    }

    public SettingsInputWidget(int x, int y, Runnable action, Runnable shiftAction) {
        super(x, y);
        this.action = action;
        this.shiftAction = shiftAction;
    }

    @Override
    public void onClose(WidgetScreen screen) {
        clicked = false;
    }

    @Override
    public boolean mouseDown(WidgetScreen screen, int mouseX, int mouseY, int button) {
        if (button == 0) {
            clicked = true;
            if (Screen.hasShiftDown()) {
                shiftAction.run();
            } else {
                action.run();
            }
            playClickSound();
            return true;
        }
        return super.mouseDown(screen, mouseX, mouseY, button);
    }

    @Override
    public boolean mouseUp(WidgetScreen screen, int mouseX, int mouseY, int button) {
        if (button == 0 && clicked) {
            clicked = false;
            return true;
        }
        return super.mouseUp(screen, mouseX, mouseY, button);
    }

    @Override
    public boolean keyDown(WidgetScreen screen, int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_SPACE) {
            if (Screen.hasShiftDown()) {
                shiftAction.run();
            } else {
                action.run();
            }
            playClickSound();
            return true;
        }
        return super.keyDown(screen, keyCode, scanCode, modifiers);
    }

    @Override
    public void draw(PoseStack stack, WidgetScreen screen, int mouseX, int mouseY, float partialTicks) {
        if (isEnabled() && (isWithinBounds(mouseX, mouseY) || screen.getFocusedWidget() == this)) {
            HIGHLIGHT.draw(stack, getX() - 1, getY() - 1, 0, getWidth() + 2, getHeight() + 2, 1, 1, 1, 1, false);
        }

        float[] rgba = isEnabled() ? new float[]{1, 1, 1, 1} : new float[]{1f, 1f, 1f, .65f};
        (clicked ? CLICKED : NORMAL).draw(stack, getX(), getY(), 0, getWidth(), getHeight(), rgba[0], rgba[1], rgba[2], rgba[3], false);
    }

    @Override
    public int getWidth() {
        return NORMAL.width();
    }

    @Override
    public int getHeight() {
        return NORMAL.height();
    }
}
