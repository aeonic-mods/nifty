package design.aeonic.nifty.api.client.screen.input;

import javax.annotation.Nullable;

public interface WidgetScreen {

    /**
     * Widgets should be created with positions relative to the screen; the pose stack will be translated before
     * rendering so you won't need to worry about absolute coordinates.
     */
    <W extends InputWidget> W addWidget(W widget);

    void removeWidget(InputWidget widget);

    @Nullable InputWidget getWidgetAt(int x, int y);

    @Nullable InputWidget getFocusedWidget();

    /**
     * You generally won't need this as the posestack is translated before rendering and mouse positions are relative
     * to the screen's left position; however, if you're calling external rendering methods (such as from an {@link net.minecraft.client.renderer.entity.ItemRenderer})
     * it might be useful.
     * @return
     */
    int getRenderLeftPos();

    /**
     * You generally won't need this as the posestack is translated before rendering and mouse positions are relative
     * to the screen's top position; however, if you're calling external rendering methods (such as from an {@link net.minecraft.client.renderer.entity.ItemRenderer})
     * it might be useful.
     * @return
     */
    int getRenderTopPos();

    default void setFocus(InputWidget widget) {
        if (getFocusedWidget() != null) getFocusedWidget().onLostFocus(this);
    }

    default void clearFocus(InputWidget widget) {
        if (getFocusedWidget() != null) getFocusedWidget().onLostFocus(this);
    }
}
