package loordgek.loordcore.tile;

import loordgek.extragenarators.container.container.IContainerGuiSync;
import loordgek.extragenarators.nbt.NBTSave;
import loordgek.extragenarators.nbt.NBTUtil;
import loordgek.extragenarators.network.DescSync;
import loordgek.extragenarators.network.IDescSync;
import loordgek.extragenarators.network.NetworkUtils;
import loordgek.extragenarators.network.SyncField;
import loordgek.extragenarators.util.JavaUtil;
import loordgek.extragenarators.util.LogHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TileMain extends TileEntity implements ITickable, IDescSync, IContainerGuiSync {
    private List<Field> NBTfieldlist = new ArrayList<Field>();
    private List<SyncField> descriptionFields;
    private int timer;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        try {
            NBTUtil.getFieldsread(this, compound.getCompoundTag("fieldNBTsafe"));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        LogHelper.info(compound);

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        try {
            if (NBTUtil.getFieldswrite(this, NBTfieldlist) != null) {
                try {
                    compound.setTag("fieldNBTsafe", NBTUtil.getFieldswrite(this, NBTfieldlist));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return compound;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return compound;
    }

    public void addFields(Object annotatedObject) {
        this.NBTfieldlist = JavaUtil.GetFields(annotatedObject, NBTSave.class);
    }

    @Override
    public void update() {
        timer++;
        if (timer == 40) {
            timer = 0;
            if (!worldObj.isRemote){
                update2secSeverSide();
            }
            else update2secClientSide();
        }
        if (!worldObj.isRemote) {
            updateServerSide();
        }
        else updateClientSide();
    }
    private void updateClientSide(){}

    private void updateServerSide(){}

    public void update2secSeverSide() {}

    public void update2secClientSide() {}

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){}

    @Override
    public Type getSyncType() {
        return Type.TILE_ENTITY;
    }

    @Override
    public List<SyncField> getDescriptionFields() {
        if(descriptionFields == null) {
            descriptionFields = NetworkUtils.getSyncFields(this, DescSync.class);
            for(SyncField field : descriptionFields) {
                field.update();
            }
        }
        return descriptionFields;
    }

    public void onGuiUpdate() {}

    @Override
    public void writeToPacket(NBTTagCompound tag) {}

    @Override
    public void readFromPacket(NBTTagCompound tag) {}

    @Override
    public int getX() {
        return pos.getX();
    }

    @Override
    public int getY() {
        return pos.getY();
    }

    @Override
    public int getZ() {
        return pos.getZ();
    }

    @Override
    public void onDescUpdate() {}
}
