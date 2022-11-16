package design.aeonic.nifty.api.platform;

import design.aeonic.nifty.api.core.Services;

import java.util.function.Supplier;

public interface PlatformInfo {
    PlatformInfo INSTANCE = Services.PLATFORM;

    default <T> T map(T onForge, T onFabric) {
        return getPlatform().isForge() ? onForge : onFabric;
    }

    default <T> T map(Supplier<T> onForge, Supplier<T> onFabric) {
        return (getPlatform().isForge() ? onForge : onFabric).get();
    }

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    Platform getPlatform();

    /**
     * Gets the physical side.
     */
    Side getPhysicalSide();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    enum Platform {
        FORGE("forge"),
        // Forgen't
        NOT_FORGE("fabric");

        private final String name;

        Platform(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public boolean isForge() {
            return this == FORGE;
        }
    }

    enum Side {
        CLIENT,
        SERVER;

        public void ifClient(Runnable runnable) {
            if (isClient()) runnable.run();
        }

        public void ifServer(Runnable runnable) {
            if (isServer()) runnable.run();
        }

        public boolean isClient() {
            return this == CLIENT;
        }

        public boolean isServer() {
            return this == SERVER;
        }
    }
}
