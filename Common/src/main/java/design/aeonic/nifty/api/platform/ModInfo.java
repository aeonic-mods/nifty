package design.aeonic.nifty.api.platform;

/**
 * Represents some simplified metadata of a loaded mod. Obtained from {@link PlatformInfo}.
 */
public interface ModInfo {
    /**
     * Gets the mod ID.
     */
    String getModId();

    /**
     * Gets the mod name.
     */
    String getModName();

    /**
     * Gets the mod's description.
     */
    String getModDescription();

    /**
     * Gets the mod version.
     */
    String getModVersion();
}
