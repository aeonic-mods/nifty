package design.aeonic.nifty.api.client.screen.input.gizmos;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.Texture;
import design.aeonic.nifty.api.client.screen.input.AbstractGizmo;
import design.aeonic.nifty.api.client.screen.input.GizmoScreen;
import design.aeonic.nifty.api.client.screen.input.SimpleMonoText;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

public class IntGizmo extends AbstractGizmo {
    public static final Texture HIGHLIGHT = new Texture("nifty:textures/gui/input_widgets.png", 64, 64, 12, 14, 0, 24);

    public static final Texture BOX_LEFT = new Texture("nifty:textures/gui/input_widgets.png", 64, 64, 10, 12, 0, 0);
    public static final Texture BOX_FILL = new Texture("nifty:textures/gui/input_widgets.png", 64, 64, 64, 12, 0, 38);
    public static final Texture BOX_LAST = new Texture("nifty:textures/gui/input_widgets.png", 64, 64, 1, 12, 10, 0);

    public static final Texture ARROW_UP = new Texture("nifty:textures/gui/input_widgets.png", 64, 64, 8, 6, 11, 0);
    public static final Texture ARROW_DOWN = new Texture("nifty:textures/gui/input_widgets.png", 64, 64, 8, 6, 11, 6);

    protected final int minValue;
    protected final int maxValue;
    protected final int maxDigits;
    protected final boolean alignRight;
    protected final int shiftChangeAmount;

    protected String stringValue;
    protected boolean edited = false;

    public IntGizmo(int x, int y, int minValue, int maxValue, int maxDigits, boolean alignRight, int shiftChangeAmount, int value) {
        super(x, y);

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.maxDigits = maxDigits;
        this.alignRight = alignRight;
        this.shiftChangeAmount = shiftChangeAmount;
        this.stringValue = Integer.toString(value);
    }

    public int getValue() {
        return Integer.parseInt(stringValue);
    }

    public void setValue(int value) {
        this.stringValue = Integer.toString(value);
    }

    @Override
    public void onClose(GizmoScreen screen) {
        edited = false;
    }

    @Override
    public boolean mouseDown(GizmoScreen screen, int mouseX, int mouseY, int button) {
        if (button == 0) {
            if (isWithinBounds(getX() + 1, getY() + 1, 7, 5, mouseX, mouseY)) {
                if (minValue == 1 && stringValue.equals("1") && Screen.hasShiftDown()) stringValue = Integer.toString(Mth.clamp(shiftChangeAmount, minValue, maxValue));
                else stringValue = Integer.toString(Math.min(maxValue, Integer.parseInt(stringValue) + (Screen.hasShiftDown() ? shiftChangeAmount : 1)));
                playClickSound();
                return true;
            } else if (isWithinBounds(getX() + 1, getY() + 6, 7, 5, mouseX, mouseY)) {
                if (minValue == 1 && stringValue.equals("1") && Screen.hasShiftDown()) stringValue = Integer.toString(Mth.clamp(-shiftChangeAmount, minValue, maxValue));
                else stringValue = Integer.toString(Math.max(minValue, Integer.parseInt(stringValue) - (Screen.hasShiftDown() ? shiftChangeAmount : 1)));
                playClickSound();
                return true;
            }
        }
        return super.mouseDown(screen, mouseX, mouseY, button);
    }

    @Override
    public boolean keyDown(GizmoScreen screen, int keyCode, int scanCode, int modifiers) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_MINUS -> {
                if (minValue >= 0) break;

                if (stringValue.isEmpty()) stringValue = "-";
                else if (stringValue.equals("0")) stringValue = "-0";
                else if (stringValue.charAt(0) == '-') stringValue = stringValue.substring(1);
                else if (stringValue.length() + 1 <= maxDigits) stringValue = Math.max(minValue, Integer.parseInt(stringValue) * -1) + "";

                return true;
            }
            case GLFW.GLFW_KEY_BACKSPACE -> {
                if (stringValue.length() > 1) {
                    if (stringValue.charAt(0) == '-') stringValue = stringValue.substring(1);
                    else stringValue = Mth.clamp(Integer.parseInt(stringValue.substring(0, stringValue.length() - 1)), minValue, maxValue) + "";
                }
                else if (stringValue.length() == 1) {
                    edited = false;
                    stringValue = Mth.clamp(0, minValue, maxValue) + "";
                }
                return true;
            }
            case GLFW.GLFW_KEY_ENTER -> {
                screen.clearFocus(this);
                return true;
            }
            default -> {
                if (keyCode >= GLFW.GLFW_KEY_0 && keyCode <= GLFW.GLFW_KEY_9) {
                    if (stringValue.equals(Integer.toString(minValue)) && !edited) {
                        edited = true;
                        stringValue = Mth.clamp(keyCode - GLFW.GLFW_KEY_0, minValue, maxValue) + "";
                    }
                    else if (stringValue.equals("-0")) stringValue = Mth.clamp(Integer.parseInt("-" + (keyCode - GLFW.GLFW_KEY_0)), minValue, maxValue) + "";
                    else if (stringValue.length() + 1 <= maxDigits) {
                        if (stringValue.isEmpty()) stringValue = keyCode - GLFW.GLFW_KEY_0 + "";
                        else {
                            if (maxDigits >= 10) {
                                stringValue = Mth.clamp(Math.min(Long.parseLong(stringValue + (keyCode - GLFW.GLFW_KEY_0)), Integer.MAX_VALUE), minValue, maxValue) + "";
                            }
                            else stringValue = Mth.clamp(Integer.parseInt(stringValue + (keyCode - GLFW.GLFW_KEY_0)), minValue, maxValue) + "";
                        }
                    }
                    return true;
                }
            }
        }
        return super.keyDown(screen, keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(GizmoScreen screen, int mouseX, int mouseY, double scrollDelta) {
        if (minValue == 1 && stringValue.equals("1") && Screen.hasShiftDown()) stringValue = Integer.toString(Mth.clamp(shiftChangeAmount * (scrollDelta > 0 ? 1 : -1), minValue, maxValue));
        else stringValue = Integer.toString(Math.min(maxValue, Math.max(minValue, Integer.parseInt(stringValue) + (Screen.hasShiftDown() ? shiftChangeAmount : 1) * (scrollDelta > 0 ? 1 : -1))));
        playClickSound();
        return true;
    }

    @Override
    public void draw(PoseStack stack, GizmoScreen screen, int mouseX, int mouseY, float partialTicks) {
        if (isEnabled() && (isWithinBounds(mouseX, mouseY) || screen.getFocusedWidget() == this)) {
            HIGHLIGHT.draw(stack, getX() - 1, getY() - 1, 0, getWidth() + 2, getHeight() + 2, 1, 1, 1, 1, false);
        }

        float[] rgba = isEnabled() ? new float[]{1, 1, 1, 1} : new float[]{1f, 1f, 1f, .65f};
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        BOX_LEFT.draw(stack, getX(), getY(), 0, rgba[0], rgba[1], rgba[2], rgba[3]);
        BOX_FILL.draw(stack, getX() + 10, getY(), 0, getWidth() - 11, BOX_FILL.height(), rgba[0], rgba[1], rgba[2], rgba[3], false);
        BOX_LAST.draw(stack, getX() + getWidth() - 1, getY(), 0, rgba[0], rgba[1], rgba[2], rgba[3]);
        RenderSystem.disableBlend();

        if (isEnabled()) {
            if (isWithinBounds(getX() + 1, getY() + 1, 7, 5, mouseX, mouseY))
                ARROW_UP.draw(stack, getX(), getY(), 0, rgba[0], rgba[1], rgba[2], rgba[3]);
            else if (isWithinBounds(getX() + 1, getY() + 6, 7, 5, mouseX, mouseY))
                ARROW_DOWN.draw(stack, getX(), getY() + 5, 0, rgba[0], rgba[1], rgba[2], rgba[3]);
        }

        drawDigits(stack, stringValue, getX() + 10, getY() + 3, 0xFFFFFF, 0x404040);
    }

    private void drawDigits(PoseStack stack, String digits, int x, int y, int color, Integer shadowColor) {
        if (alignRight) SimpleMonoText.drawRightAlign(stack, digits, x, y, 250, 2, maxDigits, color, shadowColor);
        else SimpleMonoText.draw(stack, digits, x, y, 250, 2, color, shadowColor);
    }

    @Override
    public int getWidth() {
        return 11 + 5 * maxDigits;
    }

    @Override
    public int getHeight() {
        return 12;
    }
}
