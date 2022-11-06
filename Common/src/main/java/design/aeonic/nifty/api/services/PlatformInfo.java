package design.aeonic.nifty.api.services;

import java.util.function.Supplier;

public interface PlatformInfo {

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
        FORGE,
        // Forgen't
        NOT_FORGE;

        public boolean isForge() {
            return this == FORGE;
        }
    }
}
