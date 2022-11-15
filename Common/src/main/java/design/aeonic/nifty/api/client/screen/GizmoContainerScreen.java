package design.aeonic.nifty.api.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.screen.input.Gizmo;
import design.aeonic.nifty.api.client.screen.input.GizmoScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GizmoContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements GizmoScreen {
    private final List<Gizmo> gizmos = new ArrayList<>();
    private Gizmo focusedWidget = null;

    public GizmoContainerScreen(T menu, Inventory playerInventory, Component component) {
        super(menu, playerInventory, component);
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
        stack.translate(leftPos, topPos, 0);
        for (Gizmo widget : gizmos) {
            widget.draw(stack, this, mouseX - leftPos, mouseY - topPos, partialTick);
        }
        stack.popPose();

        Gizmo hovered = getWidgetAt(mouseX - leftPos, mouseY - topPos);
        if (hovered != null) {
            var tooltip = hovered.getTooltip(this, mouseX - leftPos, mouseY - topPos);
            if (tooltip != null) hovered.getTooltipStyle(this, mouseX - leftPos, mouseY - leftPos)
                    .renderTooltip(this, stack, mouseX, mouseY, tooltip);
        }

        renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    public int getRenderLeftPos() {
        return leftPos;
    }

    @Override
    public int getRenderTopPos() {
        return topPos;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Gizmo widget = getWidgetAt((int) mouseX - leftPos, (int) mouseY - topPos);
        if (widget != null && widget.isEnabled()) {
            widget.mouseDown(this, (int) mouseX - leftPos, (int) mouseY - topPos, button);
            return true;
        } else clearFocus(getFocusedWidget());
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (gizmos.stream().anyMatch(widget -> widget.mouseUp(this, (int) mouseX - leftPos, (int) mouseY - topPos, button))) return true;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int mods) {
//        if ((key  == 256 && this.shouldCloseOnEsc()) || Minecraft.getInstance().options.keyInventory.matches(key, scanCode)) {
//            onClose();
//            return true;
//        }
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
        Gizmo widget = getWidgetAt((int) mouseX - leftPos, (int) mouseY - topPos);
        if (widget != null && widget.isEnabled() && widget.mouseScrolled(this, (int) mouseX - leftPos, (int) mouseY - topPos, scrollDelta)) return true;
        else if (super.mouseScrolled(mouseX, mouseY, scrollDelta)) return true;
        else return focusedWidget != null && focusedWidget.mouseScrolled(this, (int) mouseX - leftPos, (int) mouseY - topPos, scrollDelta);
    }

    public void addWidgets(Gizmo... widgets) {
        gizmos.addAll(Arrays.asList(widgets));
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
