package design.aeonic.nifty.api.core;

import net.minecraft.network.chat.Component;

public class Translations {
    public static class Generic {
        public static final Component POWER_UNITS = Component.translatable("gui.nifty.generic.power_units." + Services.PLATFORM.getPlatform().getName());
    }
}
