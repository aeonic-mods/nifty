package design.aeonic.nifty.impl;

import design.aeonic.nifty.api.core.Constants;
import design.aeonic.nifty.api.util.Registrar;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class ForgeNifty {
    
    public ForgeNifty() {
        Nifty.init();
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener((FMLClientSetupEvent event) -> {
            ClientNifty.clientInit(event::enqueueWork);
        });

//        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
//        modBus.addListener((RegisterEvent event) -> {
//            NetworkBlocks.register(registrar(event, ForgeRegistries.Keys.BLOCKS));
//            NetworkItems.register(registrar(event, ForgeRegistries.Keys.ITEMS));
//            NetworkBlockEntities.register(registrar(event, ForgeRegistries.Keys.BLOCK_ENTITY_TYPES));
//            NetworkMenus.register(registrar(event, ForgeRegistries.Keys.MENU_TYPES));
//        });
    }

    <T> Registrar<T> registrar(RegisterEvent event, ResourceKey<? extends Registry<T>> registry) {
        return Registrar.of(Constants.MOD_ID, (key, value) -> event.register(registry, key, () -> value));
    }
}