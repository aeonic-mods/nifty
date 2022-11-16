package design.aeonic.nifty.impl.platform;

import design.aeonic.nifty.api.platform.ModInfo;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

public class FabricModInfo implements ModInfo {
    private final ModMetadata meta;

    public FabricModInfo(ModContainer modContainer) {
        this.meta = modContainer.getMetadata();
    }

    @Override
    public String getModId() {
        return meta.getId();
    }

    @Override
    public String getModName() {
        return meta.getName();
    }

    @Override
    public String getModDescription() {
        return meta.getDescription();
    }

    @Override
    public String getModVersion() {
        return meta.getVersion().getFriendlyString();
    }
}
