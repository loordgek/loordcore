package loordgek.loordcore.util.item;

import loordgek.extragenarators.enums.EnumInvFlow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IInventoryOnwer{
    void OnInventoryChanged(ItemStack stack, int slot, String name, EnumInvFlow flow);
    void markDirty();
    void updateItemHandler();
    @Nullable
    World getWord();

    @Nullable
    BlockPos getBlockPos();
}
