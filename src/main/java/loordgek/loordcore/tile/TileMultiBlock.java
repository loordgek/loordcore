/* 
 * Copyright (c) CovertJaguar, 2014 http://railcraft.info
 * 
 * This code is the property of CovertJaguar
 * and may only be used with explicit written
 * permission unless otherwise specified on the
 * license page at http://railcraft.info/wiki/info:license.
 */
package loordgek.loordcore.tile;

import loordgek.loordcore.network.DescSync;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class TileMultiBlock<M extends TileMultiBlockMaster> extends TileMain {

    @DescSync
    private boolean hasMaster;

    private M masterBlock;

    public void setMaster(M master) {
        this.masterBlock = master;
    }

    public M getMaster() {
        return masterBlock;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return getMaster().hasMasterCapability(capability, facing, this);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return (T) getMaster().getMasterCapability(capability, facing, this);
    }

    @Override
    public void onChunkUnload() {
        if (hasMaster)
            masterBlock.inValidateMultiBlock();
    }

    @Override
    public void onBlockRemoval() {
        if (hasMaster)
            masterBlock.inValidateMultiBlock();
    }

    public boolean isHasMaster() {
        return hasMaster;
    }

    public void setHasMaster(boolean hasMaster) {
        this.hasMaster = hasMaster;
    }

    public M getMasterBlock() {
        return masterBlock;
    }

    public<MB extends M> void setMasterBlock(MB masterBlock) {
        this.masterBlock = masterBlock;
    }

    public TileMultiBlockMaster<?> findMaster(List<TileMultiBlock> blockList){
        if (this instanceof TileMultiBlockMaster<?>)
            return (TileMultiBlockMaster<?>)this;
        else if (!blockList.contains(this)) {
            for (EnumFacing enumFacing : EnumFacing.VALUES){
                TileEntity tileEntity = world.getTileEntity(pos.offset(enumFacing));
                if (tileEntity != null && tileEntity instanceof TileMultiBlock){
                    TileMultiBlock tileMultiBlock = (TileMultiBlock)tileEntity;
                    blockList.add(this);
                    if (!tileMultiBlock.isHasMaster()){
                        tileMultiBlock.findMaster(blockList);
                    }
                    if (tileMultiBlock instanceof TileMultiBlockMaster)
                        return (TileMultiBlockMaster<?>)tileEntity;
                }
            }
        }
        return null;
    }


}
