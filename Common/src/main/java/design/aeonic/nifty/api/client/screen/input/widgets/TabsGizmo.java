package design.aeonic.nifty.api.client.screen.input.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.Texture;
import design.aeonic.nifty.api.client.screen.input.AbstractGizmo;
import design.aeonic.nifty.api.client.screen.input.GizmoScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class TabsGizmo extends AbstractGizmo {

    public static final Texture TAB_LEFT = new Texture("nifty:textures/gui/input_widgets_extended.png", 64, 64, 4, 14, 15, 12);
    public static final Texture TAB_FILL = new Texture("nifty:textures/gui/input_widgets_extended.png", 64, 64, 64, 14, 0, 30);
    public static final Texture TAB_RIGHT = new Texture("nifty:textures/gui/input_widgets_extended.png", 64, 64, 4, 14, 24, 12);
    public static final Texture TAB_HOVERED_LEFT = new Texture("nifty:textures/gui/input_widgets_extended.png", 64, 64, 4, 14, 30, 12);
    public static final Texture TAB_HOVERED_FILL = new Texture("nifty:textures/gui/input_widgets_extended.png", 64, 64, 64, 1, 0, 29);
    public static final Texture TAB_HOVERED_RIGHT = new Texture("nifty:textures/gui/input_widgets_extended.png", 64, 64, 4, 14, 39, 12);
    public static final Texture TAB_SELECTED_LEFT = new Texture("nifty:textures/gui/input_widgets_extended.png", 64, 64, 4, 17, 0, 12);
    public static final Texture TAB_SELECTED_FILL = new Texture("nifty:textures/gui/input_widgets_extended.png", 64, 64, 64, 17, 0, 44);
    public static final Texture TAB_SELECTED_RIGHT = new Texture("nifty:textures/gui/input_widgets_extended.png", 64, 64, 4, 17, 9, 12);

    protected final int padding;
    protected final int spacing;

    protected final Consumer<Integer> onSelect;
    protected final Component[] tabs;
    protected int selected = 0;

    public TabsGizmo(int x, int y, int padding, int spacing, Consumer<Integer> onSelect, Component... tabs) {
        super(x, y);
        this.padding = padding;
        this.spacing = spacing;
        this.onSelect = onSelect;
        this.tabs = tabs;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    @Override
    public boolean mouseDown(GizmoScreen screen, int mouseX, int mouseY, int button) {
        if (button == 0) {
            int hovered = getHoveredTab(mouseX, mouseY);
            if (hovered != -1) {
                selected = hovered;
                onSelect.accept(selected);
                playClickSound();
                return true;
            }
        }
        return false;
    }

    @Override
    public void draw(PoseStack stack, GizmoScreen screen, int mouseX, int mouseY, float partialTicks) {
        int hovered = getHoveredTab(mouseX, mouseY);

        int x = getX();
        for (int i = 0; i < tabs.length; i++) {
            int width = getTabWidth(i);
            if (selected == i) {
                TAB_SELECTED_LEFT.draw(stack, x, getY(), 0);
                TAB_SELECTED_FILL.draw(stack, x + 4, getY(), 0, width - 8 + padding, TAB_SELECTED_FILL.height());
                TAB_SELECTED_RIGHT.draw(stack, x + width - 4, getY(), 0);
            } else {
                TAB_LEFT.draw(stack, x, getY(), 0);
                TAB_FILL.draw(stack, x + 4, getY(), 0, width - 8 + padding, TAB_FILL.height());
                TAB_RIGHT.draw(stack, x + width - 4, getY(), 0);
                if (i == hovered) {
                    TAB_HOVERED_LEFT.draw(stack, x, getY(), 0);
                    TAB_HOVERED_FILL.draw(stack, x + 4, getY(), 0, width - 8 + padding, 1);
                    TAB_HOVERED_RIGHT.draw(stack, x + width - 4, getY(), 0);
                }
            }
            Minecraft.getInstance().font.draw(stack, tabs[i], x + 4 + padding, getY() + 5, 0x404040);
            x += width + spacing;
        }
    }

    @Override
    public int getWidth() {
        int width = spacing * (tabs.length - 1);
        for (int i = 0; i < tabs.length; i++) {
            width += getTabWidth(i);
        }
        return width;
    }

    @Override
    public int getHeight() {
        return 14;
    }

    public int getHoveredTab(int mouseX, int mouseY) {
        if (!isWithinBounds(mouseX, mouseY)) return -1;
        int x = 0;
        for (int i = 0; i < tabs.length; i++) {
            if (isWithinBounds(x, getY(), getTabWidth(i), 14, mouseX, mouseY)) return i;
            x += getTabWidth(i) + spacing;
        }
        return -1;
    }

    public int getTabWidth(int index) {
        return 8 + padding * 2 + Minecraft.getInstance().font.width(tabs[index]);
    }
}
