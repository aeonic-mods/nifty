package design.aeonic.nifty.impl.platform;

import design.aeonic.nifty.api.platform.ModInfo;
import net.minecraftforge.fml.ModContainer;

public class ForgeModInfo implements ModInfo {
    private final ModContainer container;

    public ForgeModInfo(ModContainer container) {
        this.container = container;
    }

    @Override
    public String getModId() {
        return container.getModId();
    }

    @Override
    public String getModName() {
        return container.getModInfo().getDisplayName();
    }

    @Override
    public String getModDescription() {
        return container.getModInfo().getDescription();
    }

    @Override
    public String getModVersion() {
        return container.getModInfo().getVersion().toString();
    }
}
