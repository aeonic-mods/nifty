package design.aeonic.nifty.api.transfer;

import design.aeonic.nifty.api.aspect.AspectType;
import design.aeonic.nifty.api.transfer.energy.EnergyBattery;
import design.aeonic.nifty.api.transfer.fluid.FluidStorage;
import design.aeonic.nifty.api.transfer.item.ItemStorage;
import net.minecraft.resources.ResourceLocation;

public class Transfer {
    public static final ResourceLocation ITEM = new ResourceLocation("nifty", "item");
    public static final ResourceLocation FLUID = new ResourceLocation("nifty", "fluid");
    public static final ResourceLocation ENERGY = new ResourceLocation("nifty", "energy");

    public static final AspectType<ItemStorage> ITEM_ASPECT = new AspectType<>(ItemStorage.class);
    public static final AspectType<FluidStorage> FLUID_ASPECT = new AspectType<>(FluidStorage.class);
    public static final AspectType<EnergyBattery> ENERGY_ASPECT = new AspectType<>(EnergyBattery.class);
}
