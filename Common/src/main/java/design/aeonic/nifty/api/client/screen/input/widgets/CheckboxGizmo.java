package design.aeonic.nifty.api.client.screen.input.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.Texture;
import design.aeonic.nifty.api.client.screen.input.AbstractGizmo;
import design.aeonic.nifty.api.client.screen.input.GizmoScreen;

public class CheckboxGizmo extends AbstractGizmo {
    public static final Texture HIGHLIGHT = new Texture("nifty:textures/gui/input_widgets.png", 64, 64, 14, 14, 0, 24);
    public static final Texture EMPTY = new Texture("nifty:textures/gui/input_widgets.png", 64, 64, 12, 12, 19, 0);
    public static final Texture CHECKED = new Texture("nifty:textures/gui/input_widgets.png", 64, 64, 12, 12, 31, 0);

    private boolean value;


    public CheckboxGizmo(int x, int y, boolean value) {
        super(x, y);

        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public boolean mouseDown(GizmoScreen screen, int mouseX, int mouseY, int button) {
        if (button != 0) return false;
        value = !value;
        playClickSound();
        return true;
    }

    @Override
    public void draw(PoseStack stack, GizmoScreen screen, int mouseX, int mouseY, float partialTicks) {
        if (isEnabled() && (isWithinBounds(mouseX, mouseY) || screen.getFocusedWidget() == this)) {
            HIGHLIGHT.draw(stack, getX() - 1, getY() - 1, 0);
        }

        float[] rgba = isEnabled() ? new float[]{1, 1, 1, 1} : new float[]{1f, 1f, 1f, .65f};
        (value ? CHECKED : EMPTY).draw(stack, getX(), getY(), 0, rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    @Override
    public int getWidth() {
        return 12;
    }

    @Override
    public int getHeight() {
        return 12;
    }
}
