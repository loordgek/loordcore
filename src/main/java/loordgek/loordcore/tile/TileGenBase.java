package loordgek.loordcore.tile;

import loordgek.extragenarators.enums.EnumInvFlow;
import loordgek.extragenarators.nbt.NBTSave;
import loordgek.extragenarators.network.GuiSync;
import loordgek.extragenarators.util.ForgePower;
import loordgek.extragenarators.util.IFire;
import loordgek.extragenarators.util.LogHelper;
import loordgek.extragenarators.util.UpgradeUtil;
import loordgek.extragenarators.util.item.IInventoryOnwer;
import loordgek.extragenarators.util.item.InventoryUtil;
import loordgek.extragenarators.util.item.UpgradeInv;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

public class TileGenBase extends TileMain implements IInventoryOnwer, IFire {
    @GuiSync public int upgradepowercapacity;
    @GuiSync public int upgrademultiplier;
    @GuiSync public float maxmultiplier;
    @GuiSync public float runspeed;
    @GuiSync public float upgradespeed;
    @NBTSave @GuiSync public double burntime;
    @NBTSave @GuiSync public double currentburntime;
    @GuiSync public boolean isburing;
    public int forgepower = 10;
    @NBTSave @GuiSync public ForgePower power = new ForgePower(50000);

    @NBTSave public UpgradeInv upgradeinv = new UpgradeInv(64, 8, "upgrade", this);

    protected void ReCalculateUpgrade() {
        upgrademultiplier = UpgradeUtil.getMultiplierBoost(InventoryUtil.getStacks(upgradeinv));
        power.Increasecapacity(UpgradeUtil.getPowerstoreBoost(InventoryUtil.getStacks(upgradeinv)));
        upgradespeed = UpgradeUtil.getSpeedBoost(InventoryUtil.getStacks(upgradeinv));
    }

    public boolean HasRoomForEnergy() {
        return power.getEnergyStored() < power.getMaxEnergyStored();
    }

    public boolean IsRunning() {
        return currentburntime > 0;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(power);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void update2secSeverSide() {
        LogHelper.info("speed " + upgradespeed);
        LogHelper.info("multiplier " + upgrademultiplier);
        LogHelper.info("burntime " + burntime);
        LogHelper.info("currentburntime " + currentburntime);
        LogHelper.info("upgradepowercapacity " + upgradepowercapacity);
        LogHelper.info("runspeed " + runspeed);
        LogHelper.info("maxmultiplier " + maxmultiplier);
    }

    @Override
    public void OnInventoryChanged(ItemStack stack, int slot, String name, EnumInvFlow flow) {}

    @Override
    public void updateItemHandler() {
        if (!worldObj.isRemote) {
            LogHelper.info("hello");
            ReCalculateUpgrade();
        }
    }

    @Nullable
    @Override
    public World getWord() {
        return worldObj;
    }

    @Nullable
    @Override
    public BlockPos getBlockPos() {
        return getPos();
    }

    @Override
    public int FireCurrent() {
        return (int) currentburntime;
    }

    @Override
    public int FireMax() {
        return (int) burntime;
    }
}

