package loordgek.loordcore.testmod;

import loordgek.loordcore.blocks.BlockMainMultiBlock;
import loordgek.loordcore.ref.Reference;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlackTest extends BlockMainMultiBlock {
    public static final PropertyEnum<MultiEnum> blockProperty = PropertyEnum.create("MENUM", MultiEnum.class);
    @Override
    public String ModID() {
       return Reference.MODINFO.MOD_ID;
    }

    @Override
    public String ModName() {
        return Reference.MODINFO.MOD_NAME;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        int meta = getMetaFromState(state);
        switch (meta){
            case 0:
                return true;
            case 1:
                return true;
        }
        return false;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        int meta = getMetaFromState(state);
        switch (meta){
            case 0:
                return new TileTestMultiBlockMaster();
            case 1:
                return new TileTestMultiBlock();
        }
        return null;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, blockProperty);
    }

    @Override
    public String name() {
        return "blocktest";
    }
}
