package loordgek.loordcore.modules.moduletransfer.transfer;

import loordgek.loordcore.modules.moduletransfer.transfer.capabilitytransfer.CapabilityTranfer;
import loordgek.loordcore.modules.moduletransfer.transfer.capabilitytransfer.ITransfer;
import loordgek.extragenarators.util.EnumFlow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BaseTransfer implements ITransfer {
    public EnumFlow flow = EnumFlow.NONE;
    public TileEntity selfTile;
    public EnumFacing facing;
    public int transferRate;

    @Override
    public void update() {
        if (selfTile.getWorld() == null || selfTile.getWorld().getTileEntity(selfTile.getPos().offset(facing)) == null)
            return;
        if (flow == EnumFlow.PULL && canPull())
            pullUpdate();
        else if (flow == EnumFlow.PUCH && canPuch())
            puchUpdate();

    }

    protected abstract void pullUpdate();

    protected abstract void puchUpdate();

    public boolean canPull() {
        return true;
    }

    public boolean canPuch() {
        return true;
    }


    @Override
    public void onRightClick(@Nonnull World world, @Nullable BlockPos pos, @Nullable EnumFacing facing, @Nonnull EntityPlayer player, boolean air) {
        if (air) {
            flow = flow.nextFlow();
        } else this.facing = facing;

    }


    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityTranfer.TRANSFER_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityTranfer.TRANSFER_CAPABILITY ? CapabilityTranfer.TRANSFER_CAPABILITY.cast(this) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return extendSerializeNBT(new NBTTagCompound());
    }

    protected NBTTagCompound extendSerializeNBT(NBTTagCompound compound) {
        if (flow != null) compound.setString("flow", flow.name());
        if (facing != null) compound.setString("facing", facing.name());

        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        if (compound.hasKey("flow")) {
            flow = EnumFlow.valueOf(compound.getString("flow"));
        }
        if (compound.hasKey("facing")) {
            facing = EnumFacing.valueOf(compound.getString("facing"));
        }
    }
}
