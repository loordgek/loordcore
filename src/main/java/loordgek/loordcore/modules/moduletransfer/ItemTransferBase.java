package loordgek.extragenarators.items;

import loordgek.loordcore.modules.moduletransfer.transfer.capabilitytransfer.CapabilityTranfer;
import loordgek.loordcore.modules.moduletransfer.transfer.capabilitytransfer.ITransfer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class ItemTransferBase extends ItemMain {

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (playerIn instanceof FakePlayer || !worldIn.isRemote)
            return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
        RayTraceResult rayTraceResult = rayTrace(worldIn, playerIn, true);
        ITransfer transfer = itemStackIn.getCapability(CapabilityTranfer.TRANSFER_CAPABILITY, null);
        if (rayTraceResult.getBlockPos() != null){
            transfer.onRightClick(worldIn, rayTraceResult.getBlockPos(), rayTraceResult.sideHit, playerIn, false);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
        }
        else {
            transfer.onRightClick(worldIn, null, null, playerIn, true);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
        }
    }
}
