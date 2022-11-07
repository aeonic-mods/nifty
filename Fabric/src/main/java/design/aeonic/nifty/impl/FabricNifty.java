package design.aeonic.nifty.impl;

import design.aeonic.nifty.api.core.Constants;
import design.aeonic.nifty.api.util.Registrar;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;

public class FabricNifty implements ModInitializer {

    public static void onInitializeClient() {
        ClientNifty.clientInit(Minecraft.getInstance()::submit);
//        LogicNetworks.clientInit(Minecraft.getInstance()::submit);
//        NetworkKeybinds.register(($, mapping) -> KeyBindingHelper.registerKeyBinding(mapping));
//        Minecraft.getInstance().submit(() -> {
//            NetworkBlockEntityRenderers.register(BlockEntityRendererRegistry::register);
//            NetworkItemProperties.register(ItemProperties::register);
//        });
    }

    @Override
    public void onInitialize() {
        Nifty.init();
    }

    <T> Registrar<T> registrar(Registry<T> registry) {
        return Registrar.of(Constants.MOD_ID, (key, value) -> Registry.register(registry, key, value));
    }
}
