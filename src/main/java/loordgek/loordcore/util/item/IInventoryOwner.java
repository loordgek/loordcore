package loordgek.loordcore.util.item;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IInventoryOnwer{
    void OnInventoryChanged(@Nonnull ItemStack stack, int slot, String name, InvFlow flow);
    void markDirty();
}
