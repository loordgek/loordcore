package loordgek.loordcore.modules.moduletransfer.transfer;


import loordgek.extragenarators.util.EnumFlow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class EnergyTransfer extends BaseTransfer {

    public EnumFlow flow = EnumFlow.NONE;
    public IEnergyStorage energyStorage;
    public TileEntity selfTile;
    public EnumFacing facing;
    public int transferRate;

    @Override
    protected void pullUpdate() {
        IEnergyStorage targetStorage = selfTile.getWorld().getTileEntity(selfTile.getPos().offset(facing)).getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
        energyStorage.receiveEnergy(targetStorage.extractEnergy(transferRate, false), false);
    }

    @Override
    protected void puchUpdate() {
        IEnergyStorage targetStorage = selfTile.getWorld().getTileEntity(selfTile.getPos().offset(facing)).getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
        energyStorage.extractEnergy(targetStorage.receiveEnergy(transferRate, false), false);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return false;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return null;
    }
}
