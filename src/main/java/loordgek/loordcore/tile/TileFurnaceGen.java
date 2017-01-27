package loordgek.loordcore.tile;

import loordgek.extragenarators.nbt.NBTSave;
import loordgek.extragenarators.util.LogHelper;
import loordgek.extragenarators.util.item.InventorySimpleItemhander;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileFurnaceGen extends TileGenBase implements ITickable {
    @NBTSave
    public InventorySimpleItemhander fuelSlot = new InventorySimpleItemhander(64, 1, "FuelSlot", this);

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(fuelSlot);

        return super.getCapability(capability, facing);
    }

    public ItemStack getStack() {
        return fuelSlot.getStackInSlot(0);
    }

    @Override
    public void update() {
        super.update();
        if (currentburntime < 0)
            currentburntime = 0;
        if (!worldObj.isRemote) {
            if (!IsRunning()) {
                isburing = false;
                if (getStack() != null) {
                    if (HasRoomForEnergy()){
                        if (TileEntityFurnace.isItemFuel(getStack())) {
                            maxmultiplier = fuelSlot.extractItem(0, upgrademultiplier, true).stackSize;
                            runspeed = upgradespeed / maxmultiplier;
                            burntime = (int) (TileEntityFurnace.getItemBurnTime(getStack()) * maxmultiplier);
                            currentburntime = (int) (TileEntityFurnace.getItemBurnTime(getStack()) * maxmultiplier);
                            fuelSlot.extractItem(0, (int) maxmultiplier, false);
                        }
                    }
                }
            } else {
                isburing = true;
                runspeed = upgradespeed / maxmultiplier;
                float minburntime = (float) Math.min(currentburntime, runspeed);
                currentburntime -= minburntime;
                power.floatreceiveEnergy(forgepower * minburntime, false);
                if (currentburntime <= 0) {
                    burntime = 0;
                }
            }
        }
    }

    @Override
    public void update2secClientSide() {
        LogHelper.logEnergyStorage(power);
    }

    @Override
    public void update2secSeverSide() {
        super.update2secSeverSide();
        LogHelper.logEnergyStorage(power);
        float testrunspeed = upgradespeed / upgrademultiplier;
        LogHelper.info(testrunspeed);
    }

    @Override
    public void onLoad() {
        addFields(this);
        ReCalculateUpgrade();
    }

}
