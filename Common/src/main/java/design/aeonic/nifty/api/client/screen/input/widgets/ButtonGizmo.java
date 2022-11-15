package design.aeonic.nifty.api.client.screen.input.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.Texture;
import design.aeonic.nifty.api.client.screen.input.AbstractGizmo;
import design.aeonic.nifty.api.client.screen.input.GizmoScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class ButtonGizmo extends AbstractGizmo {
    public static final Texture HIGHLIGHT = new Texture("nifty:textures/gui/input_widgets.png", 64, 64, 12, 14, 0, 24);

    public static final Texture BOX_LEFT = new Texture("nifty:textures/gui/input_widgets.png", 64, 64, 1, 12, 57, 0);
    public static final Texture BOX_FILL = new Texture("nifty:textures/gui/input_widgets.png", 64, 64, 64, 12, 0, 12);
    public static final Texture BOX_LAST = new Texture("nifty:textures/gui/input_widgets.png", 64, 64, 1, 12, 59, 0);

    public static final Texture CLICKED_LEFT = new Texture("nifty:textures/gui/input_widgets.png", 64, 64, 1, 12, 60, 0);
    public static final Texture CLICKED_FILL = new Texture("nifty:textures/gui/input_widgets_extended.png", 64, 64, 64, 12, 0, 0);
    public static final Texture CLICKED_LAST = new Texture("nifty:textures/gui/input_widgets.png", 64, 64, 1, 12, 62, 0);

    protected final Component label;
    protected final Component shiftLabel;
    protected final Runnable action;
    protected final Runnable shiftAction;

    protected boolean clicked = false;

    public ButtonGizmo(int x, int y, Component label, Runnable action) {
        this(x, y, label, label, action, action);
    }

    public ButtonGizmo(int x, int y, Component label, Component shiftLabel, Runnable action, Runnable shiftAction) {
        super(x, y);

        this.label = label;
        this.shiftLabel = shiftLabel;
        this.action = action;
        this.shiftAction = shiftAction;
    }

    @Override
    public void onClose(GizmoScreen screen) {
        clicked = false;
    }

    @Override
    public boolean mouseDown(GizmoScreen screen, int mouseX, int mouseY, int button) {
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
    public boolean mouseUp(GizmoScreen screen, int mouseX, int mouseY, int button) {
        if (button == 0 && clicked) {
            clicked = false;
            return true;
        }
        return super.mouseUp(screen, mouseX, mouseY, button);
    }

    @Override
    public boolean keyDown(GizmoScreen screen, int keyCode, int scanCode, int modifiers) {
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
    public void draw(PoseStack stack, GizmoScreen screen, int mouseX, int mouseY, float partialTicks) {
        if (isEnabled() && (isWithinBounds(mouseX, mouseY) || screen.getFocusedWidget() == this)) {
            HIGHLIGHT.draw(stack, getX() - 1, getY() - 1, 0, getWidth() + 2, getHeight() + 2, 1, 1, 1, 1, false);
        }

        float[] rgba = isEnabled() ? new float[]{1, 1, 1, 1} : new float[]{1f, 1f, 1f, .65f};
        BOX_LEFT.draw(stack, getX(), getY(), 0, 1, getHeight(), rgba[0], rgba[1], rgba[2], rgba[3], false);
        BOX_FILL.draw(stack, getX() + 1, getY(), 0, getWidth() - 2, getHeight(), rgba[0], rgba[1], rgba[2], rgba[3], false);
        BOX_LAST.draw(stack, getX() + getWidth() - 1, getY(), 0, 1, getHeight(), rgba[0], rgba[1], rgba[2], rgba[3], false);

        if (clicked) {
            CLICKED_LEFT.draw(stack, getX(), getY(), 0, 1, getHeight(), rgba[0], rgba[1], rgba[2], rgba[3], false);
            CLICKED_FILL.draw(stack, getX() + 1, getY(), 0, getWidth() - 2, getHeight(), rgba[0], rgba[1], rgba[2], rgba[3], false);
            CLICKED_LAST.draw(stack, getX() + getWidth() - 1, getY(), 0, 1, getHeight(), rgba[0], rgba[1], rgba[2], rgba[3], false);
        }

        stack.pushPose();
        stack.translate(0, 0, 400);
        Minecraft.getInstance().font.draw(stack, Screen.hasShiftDown() ? shiftLabel : label, getX() + 3, getY() + 2, 0xFFFFFF);
        stack.popPose();
    }

    @Override
    public int getWidth() {
        return 6 + Minecraft.getInstance().font.width((Screen.hasShiftDown() ? shiftLabel : label));
    }

    @Override
    public int getHeight() {
        return 12;
    }
}
