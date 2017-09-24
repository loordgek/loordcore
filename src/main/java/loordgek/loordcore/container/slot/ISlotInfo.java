package loordgek.loordcore.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public interface ISlotInfo {

    boolean canPutInSlot(ItemStack stack);

    void onSlotClickedItemStack(Slot slot, ItemStack stack, EntityPlayer player);
}
