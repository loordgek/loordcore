/* 
 * Copyright (c) CovertJaguar, 2014 http://railcraft.info
 * 
 * This code is the property of CovertJaguar
 * and may only be used with explicit written
 * permission unless otherwise specified on the
 * license page at http://railcraft.info/wiki/info:license.
 */
package loordgek.loordcore.tile;

import com.google.common.collect.Multimap;
import loordgek.loordcore.util.TileUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class MultiBlockPattern {

    public final char[][][] pattern;
    private final BlockPos masterOffset;
    private final AxisAlignedBB entityCheckBounds;

    public MultiBlockPattern(char[][][] pattern) {
        this(pattern, 1, 1, 1);
    }

    public MultiBlockPattern(char[][][] pattern, int offsetX, int offsetY, int offsetZ) {
        this(pattern, offsetX, offsetY, offsetZ, null);
    }

    public MultiBlockPattern(char[][][] pattern, int offsetX, int offsetY, int offsetZ, AxisAlignedBB entityCheckBounds) {
        this.pattern = pattern;
        this.masterOffset = new BlockPos(offsetX, offsetY, offsetZ);
        this.entityCheckBounds = entityCheckBounds;
    }

    public AxisAlignedBB getEntityCheckBounds(BlockPos masterPos) {
        if (entityCheckBounds == null)
            return null;
        return entityCheckBounds.offset(masterPos);
    }

    public BlockPos findMasterPosInPattern() {
        for (byte px = 0; px < getPatternWidthX(); px++) {
            for (byte py = 0; py < getPatternHeight(); py++) {
                for (byte pz = 0; pz < getPatternWidthZ(); pz++) {
                    if (getPatternMarker(px, py, pz) == 'M')
                        return new BlockPos(px, py, pz);
                }
            }
        }
        return null;
    }

    public char getPatternMarker(int x, int y, int z) {
        return pattern[y][x][z];
    }

    public int getPatternHeight() {
        return pattern.length;
    }

    public int getPatternWidthX() {
        return pattern[0].length;
    }

    public int getPatternWidthZ() {
        return pattern[0][0].length;
    }

    public boolean isValidMultiBlock(TileMultiBlockMaster tileMultiBlockMaster) {
        if (scanDirections() == null) {
            return isValidMultiBlock(tileMultiBlockMaster, getPatternWidthX(), getPatternWidthZ(), getPatternHeight());
        } else {
            for (EnumFacing facing : scanDirections()) {

                int xWidth = getPatternWidthX();
                int zWidth = getPatternWidthZ();
                int yheight = getPatternHeight();
            }
        }

        return false;
    }

    /**
     * @return a Map
     */
    public Multimap<Character, IBlockState> multiBlockMap() {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getModelForPattern() {
        return null;
    }

    private boolean isValidMultiBlock(TileMultiBlockMaster tileMultiBlockMaster, int xWidth, int zWidth, int yheight) {
        List<TileMultiBlock<?>> multiBlock = new ArrayList<>();


        World world = tileMultiBlockMaster.getWorld();

        BlockPos offset = tileMultiBlockMaster.getPos().subtract(findMasterPosInPattern());

        for (byte px = 0; px < xWidth; px++) {
            for (byte py = 0; py < yheight; py++) {
                for (byte pz = 0; pz < zWidth; pz++) {

                    char marker = getPatternMarker(px, py, pz);

                    Collection<IBlockState> blockStates = multiBlockMap().get(marker);
                    if (blockStates == null || blockStates.isEmpty()) {
                        return false;
                    }

                    BlockPos pos = new BlockPos(px, py, pz).add(offset);

                    for (IBlockState blockState : blockStates){
                        if (blockState != world.getBlockState(pos)){
                            return false;
                        }
                        multiBlock.add(TileUtil.getTileEntity(world, pos, TileMultiBlock.class));
                    }
                }
            }
        }
        if (tileMultiBlockMaster.ExtraValidation(this)){
            for (TileMultiBlock<?> tileMultiBlock : multiBlock){
                tileMultiBlock.setMasterBlock(tileMultiBlockMaster);
            }
            return true;
        }
        return false;
    }

    @Nullable
    protected EnumFacing[] scanDirections() {
        return EnumFacing.HORIZONTALS;
    }
}
