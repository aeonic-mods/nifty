package design.aeonic.nifty.api.client.screen.input.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.Texture;
import design.aeonic.nifty.api.client.screen.input.AbstractInputWidget;
import design.aeonic.nifty.api.client.screen.input.WidgetScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

public class StringInputWidget extends AbstractInputWidget {
    public static final Texture HIGHLIGHT = new Texture("nifty:textures/gui/input_widgets.png", 64, 64, 12, 14, 0, 24);

    public static final Texture BOX_LEFT = new Texture("nifty:textures/gui/input_widgets.png", 64, 64, 1, 12, 0, 0);
    public static final Texture BOX_FILL = new Texture("nifty:textures/gui/input_widgets.png", 64, 64, 64, 12, 0, 38);
    public static final Texture BOX_LAST = new Texture("nifty:textures/gui/input_widgets.png", 64, 64, 1, 12, 10, 0);

    public static final Texture CURSOR = new Texture("nifty:textures/gui/input_widgets_extended.png", 64, 64, 5, 3, 43, 12);

    protected final int maxLength;
    protected final String widthString;
    protected String value;
    protected int cursor;
    protected int selectionStart = -1;
    protected int selectionEnd = -1;
    protected boolean selectionNegative = false;

    public StringInputWidget(int x, int y, int maxLength, String value) {
        super(x, y);
        this.maxLength = maxLength;
        this.widthString = "W".repeat(maxLength);
        this.value = value;
        this.cursor = value.length();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void onClose(WidgetScreen screen) {
        cursor = 0;
        selectionStart = -1;
        selectionEnd = -1;
    }

    @Override
    public boolean mouseDown(WidgetScreen screen, int mouseX, int mouseY, int button) {
        if (screen.getFocusedWidget() != this) {
            cursor = value.length();
        } else {
            int x = mouseX - getX();
            int y = mouseY - getY();
            int width = 0;
            for (int i = 0; i < value.length(); i++) {
                width += Minecraft.getInstance().font.width(value.substring(i, i + 1));
                if (width > x) {
                    cursor = i - 1;
                    break;
                }
            }
            if (width <= x) {
                cursor = value.length();
            }
        }
        selectionStart = -1;
        selectionEnd = -1;
        super.mouseDown(screen, mouseX, mouseY, button);
        return true;
    }

    @Override
    public boolean keyDown(WidgetScreen screen, int keyCode, int scanCode, int modifiers) {
        // TODO: Cleanup, probably move all of this to another reusable input management class
        // Also split into more methods
        // I mean this is like really bad I just kept throwing shit onto it until it had all the functionality I needed
        // It is really not good
//        if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT || keyCode == GLFW.GLFW_KEY_ESCAPE) return false; // Shift pressed on its own, or escape
        cursor = Math.min(cursor, value.length());
        if (selectionStart == -1) selectionNegative = false;
        switch (keyCode) {
//            case GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT, GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_RIGHT_CONTROL, GLFW.GLFW_KEY_ESCAPE -> {
//                return false;
//            }
            case GLFW.GLFW_KEY_ENTER -> {
                screen.clearFocus(this);
                cursor = value.length();
                selectionStart = -1;
                selectionEnd = -1;
                return true;
            }
            case GLFW.GLFW_KEY_BACKSPACE -> {
                if (selectionStart != -1) {
                    value = value.substring(0, selectionStart) + value.substring(selectionEnd);
                    cursor = selectionStart;
                    selectionStart = -1;
                    selectionEnd = -1;
                } else if (cursor > 0) {
                    if (Screen.hasControlDown()) {
                        int i = cursor;
                        while (i > 0 && value.charAt(i - 1) == ' ') {
                            i--;
                        }
                        while (i > 0 && value.charAt(i - 1) != ' ') {
                            i--;
                        }
                        value = value.substring(0, i) + value.substring(cursor);
                        cursor = i;
                    } else {
                        value = value.substring(0, cursor - 1) + value.substring(cursor);
                        cursor--;
                    }
                }
                return true;
            }
            case GLFW.GLFW_KEY_LEFT -> { // Left
                if (Screen.hasShiftDown()) {
                    if (selectionStart == -1) {
                        selectionEnd = cursor;
                        selectionStart = cursor;
                        selectionNegative = true;
                    }
                    if (selectionNegative) {
                        if (Screen.hasControlDown() && cursor > 0) {
                            int i = selectionStart;
                            while (i > 0 && value.charAt(i - 1) == ' ') {
                                i--;
                            }
                            while (i > 0 && value.charAt(i - 1) != ' ') {
                                i--;
                            }
                            selectionStart = i;
                        } else {
                            selectionStart = Math.max(0, selectionStart - 1);
                        }
                    } else {
                        if (Screen.hasControlDown()) {
                            int i = selectionEnd;
                            while (i > 0 && value.charAt(i - 1) == ' ') {
                                i--;
                            }
                            while (i > 0 && value.charAt(i - 1) != ' ') {
                                i--;
                            }
                            selectionEnd = Math.max(selectionStart, i);
                        } else {
                            selectionEnd = Math.max(selectionStart, selectionEnd - 1);
                        }
                        if (selectionStart >= selectionEnd) {
                            cursor = selectionStart;
                            selectionStart = -1;
                            selectionEnd = -1;
                        }
                    }
                    return true;
                }
                if (selectionStart != -1) {
                    cursor = selectionStart;
                    selectionStart = -1;
                    selectionEnd = -1;
                } else if (cursor > 0) {
                    if (Screen.hasControlDown() && cursor > 0) {
                        int i = cursor;
                        while (i > 0 && value.charAt(i - 1) == ' ') {
                            i--;
                        }
                        while (i > 0 && value.charAt(i - 1) != ' ') {
                            i--;
                        }
                        cursor = i;
                    } else {
                        cursor = Math.max(0, cursor - 1);
                    }
                }
                return true;
            }
            case GLFW.GLFW_KEY_RIGHT -> { // Right
                if (Screen.hasShiftDown()) {
                    if (selectionStart == -1) {
                        if (Screen.hasControlDown()) {
                            selectionStart = cursor;
                            int i = cursor;
                            while (i < value.length() && value.charAt(i) == ' ') {
                                i++;
                            }
                            while (i < value.length() && value.charAt(i) != ' ') {
                                i++;
                            }
                            selectionEnd = i;
                            if (selectionStart >= selectionEnd) {
                                selectionStart = -1;
                                selectionEnd = -1;
                            }
                        } else {
                            selectionStart = cursor;
                            selectionEnd = Math.max(0, cursor + 1);
                        }
                    } else if (selectionNegative) {
                        if (Screen.hasControlDown()) {
                            int i = selectionStart;
                            while (i < value.length() && value.charAt(i) == ' ') {
                                i++;
                            }
                            while (i < value.length() && value.charAt(i) != ' ') {
                                i++;
                            }
                            selectionStart = Math.min(selectionEnd, i);
                        } else {
                            selectionStart = Math.min(selectionEnd, selectionStart + 1);
                        }
                        if (selectionStart == selectionEnd) {
                            cursor = selectionStart;
                            selectionStart = -1;
                            selectionEnd = -1;
                        }
                    } else {
                        if (Screen.hasControlDown()) {
                            int i = selectionEnd;
                            while (i < value.length() && value.charAt(i) == ' ') {
                                i++;
                            }
                            while (i < value.length() && value.charAt(i) != ' ') {
                                i++;
                            }
                            selectionEnd = i;
                        } else {
                            selectionEnd = Math.min(value.length(), selectionEnd + 1);
                        }
                    }
                    return true;
                }
                if (selectionStart != -1) {
                    if (Screen.hasControlDown()) {
                        int i = 0;
                        while (i < value.length() && value.charAt(i) == ' ') {
                            i++;
                        }
                        while (i < value.length() && value.charAt(i) != ' ') {
                            i++;
                        }
                        cursor = i;
                    } else {
                        cursor = selectionEnd;
                    }
                    selectionStart = -1;
                    selectionEnd = -1;
                } else if (cursor < value.length()) {
                    if (Screen.hasControlDown()) {
                        int i = cursor;
                        while (i < value.length() && value.charAt(i) == ' ') {
                            i++;
                        }
                        while (i < value.length() && value.charAt(i) != ' ') {
                            i++;
                        }
                        cursor = i;
                    } else {
                        cursor = Math.min(value.length(), cursor + 1);
                    }
                }
                return true;
            }
            case GLFW.GLFW_KEY_SPACE -> {
                if (selectionStart != -1) {
                    value = value.substring(0, selectionStart) + value.substring(selectionEnd);
                    cursor = selectionStart;
                    selectionStart = -1;
                    selectionEnd = -1;
                }
                if (value.length() < maxLength) {
                    value = value.substring(0, cursor) + " " + value.substring(cursor);
                    cursor++;
                }
                return true;
            }
            default -> {
                if (Screen.isSelectAll(keyCode)) {
                    selectionStart = 0;
                    selectionEnd = value.length();
                    cursor = 0;
                    return true;
                } else if (Screen.isCopy(keyCode)) {
                    if (selectionStart != -1) {
                        Minecraft.getInstance().keyboardHandler.setClipboard(value.substring(selectionStart, selectionEnd));
                        return true;
                    }
                } else if (Screen.isPaste(keyCode)) {
                    if (selectionStart != -1) {
                        value = value.substring(0, selectionStart) + value.substring(selectionEnd);
                        cursor = selectionStart;
                        selectionStart = -1;
                        selectionEnd = -1;
                    }
                    String clipboard = Minecraft.getInstance().keyboardHandler.getClipboard();
                    if (value.isEmpty()) {
                        value = clipboard;
                        cursor = value.length();
                    } else {
                        if (clipboard.length() + value.length() <= maxLength) {
                            value = value.substring(0, Math.min(cursor, value.length() - 1)) + clipboard + value.substring(Math.min(cursor, value.length() - 1));
                            cursor = Math.min(cursor + clipboard.length(), value.length());
                        } else {
                            value = value.substring(0, Math.min(cursor, value.length() - 1)) + clipboard.substring(0, maxLength - value.length()) + value.substring(Math.min(cursor, value.length() - 1));
                            cursor = Math.min(cursor + clipboard.length(), value.length());
                        }
                    }
                    return true;
                }

                boolean shift = Screen.hasShiftDown();
                if (value.length() < maxLength) {
                    if (keyCode >= GLFW.GLFW_KEY_0 && keyCode <= GLFW.GLFW_KEY_Z || keyCode == GLFW.GLFW_KEY_MINUS) {
                        if (selectionStart != -1) {
                            value = value.substring(0, selectionStart) + value.substring(selectionEnd);
                            cursor = selectionStart;
                            selectionStart = -1;
                            selectionEnd = -1;
                        }

                        char character = getChar(keyCode, shift);
                        if (cursor == value.length() && cursor < maxLength) {
                            value += character;
                            cursor++;
                        } else {
                            value = value.substring(0, cursor) + character + value.substring(cursor);
                            cursor = Math.min(cursor + 1, value.length());
                        }
                    } else {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    public char getChar(int keyCode, boolean shift) {
        return switch (keyCode) {
            case GLFW.GLFW_KEY_0 -> shift ? ')': '0';
            case GLFW.GLFW_KEY_1 -> shift ? '!': '1';
            case GLFW.GLFW_KEY_2 -> shift ? '@': '2';
            case GLFW.GLFW_KEY_3 -> shift ? '#': '3';
            case GLFW.GLFW_KEY_4 -> shift ? '$': '4';
            case GLFW.GLFW_KEY_5 -> shift ? '%': '5';
            case GLFW.GLFW_KEY_6 -> shift ? '^': '6';
            case GLFW.GLFW_KEY_7 -> shift ? '&': '7';
            case GLFW.GLFW_KEY_8 -> shift ? '*': '8';
            case GLFW.GLFW_KEY_9 -> shift ? '(': '9';
            case GLFW.GLFW_KEY_MINUS -> shift ? '_': '-';
            default -> {
                if (keyCode >= GLFW.GLFW_KEY_A && keyCode <= GLFW.GLFW_KEY_Z) {
                    yield shift ? (char) (keyCode - GLFW.GLFW_KEY_A + 'A') : (char) (keyCode - GLFW.GLFW_KEY_A + 'a');
                }
                yield ' ';
            }
        };
    }

    @Override
    public void draw(PoseStack stack, WidgetScreen screen, int mouseX, int mouseY, float partialTicks) {
        if (selectionStart != -1) {
            selectionStart = Mth.clamp(selectionStart, 0, value.length());
            selectionEnd = Mth.clamp(selectionEnd, 0, value.length());
        }

        if (isEnabled() && (isWithinBounds(mouseX, mouseY) || screen.getFocusedWidget() == this)) {
            HIGHLIGHT.draw(stack, getX() - 1, getY() - 1, 0, getWidth() + 2, getHeight() + 2, 1, 1, 1, 1, false);
        }

        float[] rgba = isEnabled() ? new float[]{1, 1, 1, 1} : new float[]{1f, 1f, 1f, .65f};
        BOX_LEFT.draw(stack, getX(), getY(), 0, rgba[0], rgba[1], rgba[2], rgba[3]);
        BOX_FILL.draw(stack, getX() + 1, getY(), 0, getWidth() - 2, BOX_FILL.height(), rgba[0], rgba[1], rgba[2], rgba[3], false);
        BOX_LAST.draw(stack, getX() + getWidth() - 1, getY(), 0, rgba[0], rgba[1], rgba[2], rgba[3]);

        if (screen.getFocusedWidget() == this) {
            if (selectionStart != -1) {
                HIGHLIGHT.draw(stack, getX() + 3 + Minecraft.getInstance().font.width(value.substring(0, selectionStart)), getY() + 2, 0, Minecraft.getInstance().font.width(value.substring(selectionStart, selectionEnd)), Minecraft.getInstance().font.lineHeight, 1, 1, 1, 1, false);
            } else {
                CURSOR.draw(stack, getX() + Minecraft.getInstance().font.width(value.substring(0, cursor)), getY() + Minecraft.getInstance().font.lineHeight, 0, rgba[0], rgba[1], rgba[2], rgba[3]);
            }
        }
        Minecraft.getInstance().font.draw(stack, value, getX() + 3, getY() + 2, 0x3F3F3F);
    }

    @Override
    public int getWidth() {
        return 6 + Minecraft.getInstance().font.width(widthString);
    }

    @Override
    public int getHeight() {
        return 12;
    }
}
