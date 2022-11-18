package design.aeonic.nifty.api.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.RenderUtils;
import design.aeonic.nifty.api.client.screen.drawable.Texture;
import design.aeonic.nifty.api.client.screen.input.Gizmo;
import design.aeonic.nifty.api.client.screen.input.GizmoScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGizmoScreen extends Screen implements GizmoScreen {
    public static final Texture TOOLTIP = new Texture("nifty:textures/gui/tooltip.png", 16, 16, 16, 16);

    protected final List<Gizmo> gizmos = new ArrayList<>();
    protected Gizmo focusedWidget = null;

    public AbstractGizmoScreen(Component component) {
        super(component);
    }

    @Override
    public void onClose() {
        gizmos.forEach(widget -> widget.onClose(this));
        super.onClose();
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTick) {
        super.render(stack, mouseX, mouseY, partialTick);

        stack.pushPose();
        stack.translate(getRenderLeftPos(), getRenderTopPos(), 0);
        for (Gizmo widget : gizmos) {
            widget.draw(stack, this, mouseX - getRenderLeftPos(), mouseY - getRenderTopPos(), partialTick);
        }
        stack.popPose();

        Gizmo hovered = getHoveredWidget(mouseX, mouseY);
        if (hovered != null) {
            var tooltip = hovered.getTooltip(this, mouseX - getRenderLeftPos(), mouseY - getRenderTopPos());
            if (tooltip != null) hovered.getTooltipStyle(this, mouseX - getRenderLeftPos(), mouseY - getRenderTopPos()).renderTooltip(this, stack, mouseX, mouseY, tooltip);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Gizmo widget = getHoveredWidget((int) mouseX, (int) mouseY);
        if (widget != null && widget.isEnabled()) {
            if (widget.mouseDown(this, (int) mouseX - getRenderLeftPos(), (int) mouseY - getRenderTopPos(), button)) return true;
        } else if (button == 0) clearFocus(getFocusedWidget());
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (gizmos.stream().anyMatch(widget -> widget.mouseUp(this, (int) mouseX - getRenderLeftPos(), (int) mouseY - getRenderTopPos(), button))) return true;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int mods) {
        if (focusedWidget != null && focusedWidget.isEnabled()) {
            if (focusedWidget.keyDown(this, key, scanCode, mods)) return true;
        }
        return super.keyPressed(key, scanCode, mods);
    }

    @Override
    public boolean keyReleased(int key, int scanCode, int mods) {
        if (focusedWidget != null && focusedWidget.isEnabled()) {
            if (focusedWidget.keyUp(this, key, scanCode, mods)) return true;
        }
        return super.keyReleased(key, scanCode, mods);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
        Gizmo widget = getHoveredWidget((int) mouseX, (int) mouseY);
        if (widget != null && widget.isEnabled() && widget.mouseScrolled(this, (int) mouseX - getRenderLeftPos(), (int) mouseY - getRenderTopPos(), scrollDelta)) return true;
        else if (super.mouseScrolled(mouseX, mouseY, scrollDelta)) return true;
        else return focusedWidget != null && focusedWidget.mouseScrolled(this, (int) mouseX - getRenderLeftPos(), (int) mouseY - getRenderTopPos(), scrollDelta);
    }

    public void addWidgets(Gizmo... widgets) {
        for (Gizmo widget : widgets) {
            addWidget(widget);
        }
    }

    @Override
    public  <W extends Gizmo> W addWidget(W widget) {
        // Don't add if the widget is already in the list - sometimes gets duplicated otherwise when screen is resized
        if (!gizmos.contains(widget)) gizmos.add(widget);
        return widget;
    }

    @Override
    public void removeWidget(Gizmo widget) {
        gizmos.remove(widget);
    }

    public Gizmo getHoveredWidget(int mouseX, int mouseY) {
        return getWidgetAt(mouseX - getRenderLeftPos(), mouseY - getRenderTopPos());
    }

    @Nullable
    @Override
    public Gizmo getWidgetAt(int x, int y) {
        return gizmos.stream().filter(widget -> widget.isWithinBounds(x, y)).findFirst().orElse(null);
    }

    @Nullable
    @Override
    public Gizmo getFocusedWidget() {
        return focusedWidget;
    }

    @Override
    public void setFocus(Gizmo widget) {
        GizmoScreen.super.setFocus(widget);
        focusedWidget = widget;
    }

    @Override
    public void clearFocus(Gizmo widget) {
        GizmoScreen.super.clearFocus(widget);
        if (focusedWidget == widget) {
            focusedWidget = null;
        }
    }
}
