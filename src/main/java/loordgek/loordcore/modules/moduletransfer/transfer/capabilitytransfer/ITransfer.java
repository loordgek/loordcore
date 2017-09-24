package loordgek.loordcore.modules.moduletransfer.transfer.capabilitytransfer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ITransfer extends ICapabilitySerializable<NBTTagCompound> {

    void update();

    void onRightClick(@Nonnull World world, @Nullable BlockPos pos, @Nullable EnumFacing facing,@Nonnull EntityPlayer player, boolean air);

}
