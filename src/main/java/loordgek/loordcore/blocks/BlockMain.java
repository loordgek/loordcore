package loordgek.loordcore.blocks;

import loordgek.loordcore.Registar.IModInfo;
import loordgek.loordcore.Registar.IVariantLookup;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public abstract class BlockMain extends Block implements IVariantLookup, IModInfo {

    public BlockMain(){
        super(Material.ROCK, MapColor.GRAY);
    }
}

