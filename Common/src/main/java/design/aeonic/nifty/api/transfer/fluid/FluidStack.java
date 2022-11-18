package design.aeonic.nifty.api.transfer.fluid;

import design.aeonic.nifty.api.core.Services;
import design.aeonic.nifty.api.platform.ModInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

/**
 * Represents fluids in a tank or somethin. Up to you man.<br><br>
 * Similar to Forge's fluid stack, but somewhat simplified and with a few key differences,
 * namely the removal of some optionals for less checks.
 */
public class FluidStack {

    // General fluid amounts, following Forge & tconstruct's conventions
    public static final long BUCKET = 1000;
    public static final long MB = 1;
    public static final long BLOCK = 1296;
    public static final long INGOT = 144;
    public static final long NUGGET = 16;

    public static final FluidStack EMPTY_STACK = FluidStack.of(Fluids.EMPTY, 0);

    private boolean isEmptyCache = false;
    private Fluid fluid;
    private long amount;
    private CompoundTag tag;
    // Only created/updated/queried on the client, and only if it's used at least once - should be zero performance hit
    private FluidTooltipCache tooltip;

    public static FluidStack of(Fluid fluid) {
        return of(fluid, BUCKET);
    }

    public static FluidStack of(Fluid fluid, long amount) {
        return new FluidStack(fluid, amount, new CompoundTag());
    }

    public static FluidStack of(Fluid fluid, long amount, CompoundTag tag) {
        return new FluidStack(fluid, amount, tag == null ? new CompoundTag() : tag.copy());
    }

    protected FluidStack(Fluid fluid, long amount, CompoundTag tag) {
        this.fluid = fluid == null ? Fluids.EMPTY : fluid;
        this.amount = amount;
        this.tag = tag;
        checkEmpty();
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Fluid", Registry.FLUID.getKey(fluid).toString());
        tag.putLong("Amount", amount);
        tag.put("Data", this.tag);
        return tag;
    }

    public static FluidStack fromNbt(CompoundTag tag) {
        return new FluidStack(
                Registry.FLUID.get(new ResourceLocation(tag.getString("Fluid"))),
                tag.getLong("Amount"),
                tag.getCompound("Data"));
    }

    public void toNetwork(FriendlyByteBuf buf) {
        if (isEmpty()) {
            buf.writeBoolean(false);
            return;
        }
        buf.writeBoolean(true);
        buf.writeVarInt(Registry.FLUID.getId(fluid));
        buf.writeVarLong(amount);
        buf.writeNbt(tag);
    }

    public static FluidStack fromNetwork(FriendlyByteBuf buf) {
        if (buf.readBoolean()) {
            return of(Registry.FLUID.byId(buf.readVarInt()),
                    buf.readVarLong(), buf.readNbt());
        }
        return EMPTY_STACK;
    }

    public boolean is(Fluid fluid) {
        return this.fluid.isSame(fluid);
    }

    public boolean canStack(FluidStack other) {
        return isEmpty() || other.isEmpty() || (fluid.isSame(other.fluid) && (tag.isEmpty() ? other.tag.isEmpty() : tag.equals(other.tag)));
    }

    public FluidStack split(long amount) {
        long amt = Math.min(amount, this.amount);
        FluidStack ret = copy();
        ret.setAmount(amt);
        shrink(amt);
        return ret;
    }

    public FluidStack copy() {
        return new FluidStack(fluid, amount, tag.copy());
    }

    public void shrink(long amount) {
        grow(-amount);
    }

    public void grow(long amount) {
        if (amount != 0)
            setAmount(this.amount + amount);
    }

    public void setAmount(long amount) {
        if (amount >= 0) {
            this.amount = amount;
            checkEmpty();
        }
    }

    public void setTag(CompoundTag tag) {
        this.tag = tag;
    }

    public boolean isEmpty() {
        return isEmptyCache;
    }

    public Fluid getFluid() {
        return fluid;
    }

    public long getAmount() {
        return amount;
    }

    public CompoundTag getTag() {
        return tag;
    }

    protected void checkEmpty() {
        if (this == FluidStack.EMPTY_STACK) isEmptyCache = true;
        else if (amount == 0 || fluid.isSame(Fluids.EMPTY)) {
            isEmptyCache = true;
            amount = 0;
            fluid = Fluids.EMPTY;
            if (!tag.isEmpty()) tag = new CompoundTag();
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        FluidStack that = (FluidStack) other;

        if (amount != that.amount) return false;
        if (!fluid.equals(that.fluid)) return false;
        return tag.equals(that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fluid, amount, tag);
    }

    public List<Component> getTooltip() {
        if (tooltip == null) tooltip = new FluidTooltipCache(this);
        return tooltip.get(this);
    }

    public static class FluidTooltipCache {
        private int lastHash;
        private Component fluidName;
        private Component amount;
        private Component modName;
        private Component fluidKey;

        public FluidTooltipCache(FluidStack stack) {
            lastHash = stack.hashCode();
            update(stack);
        }

        public void update(FluidStack stack) {
            ResourceLocation fluid = Registry.FLUID.getKey(stack.getFluid());

            fluidName = Component.translatable("block." + fluid.getNamespace() + "." + fluid.getPath());
            amount = Component.literal(DecimalFormat.getIntegerInstance().format(stack.getAmount()) + " mB");
            modName = Component.literal(Services.PLATFORM.getModInfo(fluid.getNamespace()).map(ModInfo::getModName).orElse("Minecraft")).withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC);
            fluidKey = Component.literal(fluid.toString()).withStyle(ChatFormatting.GRAY);
        }

        public List<Component> get(FluidStack stack) {
            if (lastHash != stack.hashCode()) update(stack);
            return Minecraft.getInstance().options.advancedItemTooltips ? List.of(fluidName, amount, modName, fluidKey) : List.of(fluidName, amount, modName);
        }
    }
}