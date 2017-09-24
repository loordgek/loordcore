package loordgek.loordcore.util.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public interface IExtendedItemhander extends IItemHandler {

    void putStack(@Nonnull ItemStack oldstack, int slot);

    NonNullList<ItemStack> getInv();


}
