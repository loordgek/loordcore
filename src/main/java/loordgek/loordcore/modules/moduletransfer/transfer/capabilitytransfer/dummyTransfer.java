package loordgek.loordcore.modules.moduletransfer.transfer.capabilitytransfer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
//DO NOT USE
@Deprecated
public class dummyTransfer implements ITransfer {
    @Override
    public void update() {

    }

    @Override
    public void onRightClick(@Nonnull World world, @Nullable BlockPos pos, @Nullable EnumFacing facing, @Nonnull EntityPlayer player, boolean air) {

    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return false;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }
}
