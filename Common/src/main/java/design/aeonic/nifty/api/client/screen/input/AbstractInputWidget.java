package design.aeonic.nifty.api.client.screen.input;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

public abstract class AbstractInputWidget implements InputWidget {
    private int x;
    private int y;
    private boolean enabled;

    private long lastClickSound = 0;

    public AbstractInputWidget() {
        this(0, 0, true);
    }

    public AbstractInputWidget(int x, int y) {
        this(x, y, true);
    }

    public AbstractInputWidget(int x, int y, boolean enabled) {
        this.x = x;
        this.y = y;
        this.enabled = enabled;
    }

    @Override
    public boolean mouseDown(WidgetScreen screen, int mouseX, int mouseY, int button) {
        if (button == 0 && screen.getFocusedWidget() != this) {
            screen.clearFocus(screen.getFocusedWidget());
            screen.setFocus(this);
            playClickSound();
            return true;
        }
        return false;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this. y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    protected void playClickSound() {
        if (System.currentTimeMillis() - lastClickSound > 50) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            lastClickSound = System.currentTimeMillis();
        }
    }
}
