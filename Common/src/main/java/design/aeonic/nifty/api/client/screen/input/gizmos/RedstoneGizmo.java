package design.aeonic.nifty.api.client.screen.input.gizmos;

import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.Texture;
import design.aeonic.nifty.api.client.screen.input.AbstractGizmo;
import design.aeonic.nifty.api.client.screen.input.GizmoScreen;
import design.aeonic.nifty.api.core.Translations;
import design.aeonic.nifty.api.machine.RedstoneControl;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.List;

public class RedstoneGizmo extends AbstractGizmo {
    public static final Texture HIGHLIGHT = new Texture("nifty:textures/gui/gizmos.png", 64, 64, 14, 14, 0, 24);
    public static final Texture ALWAYS = new Texture("nifty:textures/gui/gizmos.png", 64, 64, 12, 12, 0, 50);
    public static final Texture NEVER = new Texture("nifty:textures/gui/gizmos.png", 64, 64, 12, 12, 12, 50);
    public static final Texture HIGH = new Texture("nifty:textures/gui/gizmos.png", 64, 64, 12, 12, 24, 50);
    public static final Texture LOW = new Texture("nifty:textures/gui/gizmos.png", 64, 64, 12, 12, 36, 50);
    public static final Texture PULSE = new Texture("nifty:textures/gui/gizmos.png", 64, 64, 12, 12, 48, 50);

    private RedstoneControl value;

    public RedstoneGizmo(int x, int y, RedstoneControl value) {
        super(x, y);

        this.value = value;
    }

    public void setValue(RedstoneControl value) {
        this.value = value;
    }

    public RedstoneControl getValue() {
        return value;
    }

    @Override
    public boolean mouseDown(GizmoScreen screen, int mouseX, int mouseY, int button) {
        cycleValue(Screen.hasShiftDown() || button == 1);
        playClickSound();
        return true;
    }

    private void cycleValue(boolean isShiftDown) {
        value = RedstoneControl.values()[(value.ordinal() + (isShiftDown ? -1 : 1) + RedstoneControl.values().length) % RedstoneControl.values().length];
        playClickSound();
    }

    @Override
    public void draw(PoseStack stack, GizmoScreen screen, int mouseX, int mouseY, float partialTicks) {
        if (isEnabled() && (isWithinBounds(mouseX, mouseY) || screen.getFocusedWidget() == this)) {
            HIGHLIGHT.draw(stack, getX() - 1, getY() - 1, 200);
        }

        float[] rgba = isEnabled() ? new float[]{1, 1, 1, 1} : new float[]{1f, 1f, 1f, .65f};
        (switch (value) {
            case ALWAYS -> ALWAYS;
            case NEVER  -> NEVER;
            case HIGH   -> HIGH;
            case LOW    -> LOW;
            case PULSE  -> PULSE;
        }).draw(stack, getX(), getY(), 200, rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    @Nullable
    @Override
    public List<Component> getTooltip(GizmoScreen screen, int mouseX, int mouseY) {
        return List.of(Translations.RedstoneControl.translate(value));
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
