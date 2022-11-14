package design.aeonic.nifty.api.core;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.Function;

public class Translations {
    public static class Generic {
        public static final Component POWER_UNITS = Component.translatable("gui.nifty.generic.power_units." + Services.PLATFORM.getPlatform().getName());
        public static final Component REDSTONE_CONTROL = Component.translatable("gui.nifty.generic.redstone_control");
    }

    public static final class Side {
        public static final Component UP = Component.translatable("gui.nifty.side.up");
        public static final Component DOWN = Component.translatable("gui.nifty.side.down");
        public static final Component NORTH = Component.translatable("gui.nifty.side.north");
        public static final Component SOUTH = Component.translatable("gui.nifty.side.south");
        public static final Component EAST = Component.translatable("gui.nifty.side.east");
        public static final Component WEST = Component.translatable("gui.nifty.side.west");

        public static Component translate(Direction side) {
            return switch (side) {
                case UP -> UP;
                case DOWN -> DOWN;
                case NORTH -> NORTH;
                case SOUTH -> SOUTH;
                case EAST -> EAST;
                case WEST -> WEST;
            };
        }
    }

    public static final class RedstoneControl {
        public static final Component ALWAYS = Component.translatable("gui.nifty.redstone_control.always");
        public static final Component HIGH = Component.translatable("gui.nifty.redstone_control.high");
        public static final Component LOW = Component.translatable("gui.nifty.redstone_control.low");
        public static final Component NEVER = Component.translatable("gui.nifty.redstone_control.never");
        public static final Component PULSE = Component.translatable("gui.nifty.redstone_control.pulse");

        public static Component translate(design.aeonic.nifty.api.machine.RedstoneControl value) {
            return switch (value) {
                case ALWAYS -> ALWAYS;
                case HIGH   -> HIGH;
                case LOW    -> LOW;
                case NEVER  -> NEVER;
                case PULSE  -> PULSE;
            };
        }
    }

    private static DynamicComponent dynamic(String key) {
        return (Object... args) -> Component.translatable(key, args);
    }

    private static DynamicComponent dynamic(String key, Function<MutableComponent, Component> mutator) {
        return (Object... args) -> mutator.apply(Component.translatable(key, args));
    }

    public interface DynamicComponent {
        Component get(Object... args);
    }
}
