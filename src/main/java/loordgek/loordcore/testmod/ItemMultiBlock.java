package loordgek.loordcore.testmod;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class ItemMultiBlock extends ItemBlock {
    public ItemMultiBlock(Block block) {
        super(block);
    }



    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return String.format("tile.%s%s%s", getRegistryName().getResourceDomain(), "base", MultiEnum.enumarray[MathHelper.clamp(stack.getItemDamage(), 0, MultiEnum.enumarray.length - 1)]);
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("tile.%s%s", getRegistryName().getResourceDomain() + ":", "base");
    }
}
