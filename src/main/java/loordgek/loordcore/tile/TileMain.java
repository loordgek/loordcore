package loordgek.loordcore.tile;


import loordgek.loordcore.nbt.INBTSaver;
import loordgek.loordcore.nbt.NBTSave;
import loordgek.loordcore.network.DescSync;
import loordgek.loordcore.network.IDescSync;
import loordgek.loordcore.network.NetworkHandler;
import loordgek.loordcore.network.NetworkUtils;
import loordgek.loordcore.network.PacketDescription;
import loordgek.loordcore.network.SyncField;
import loordgek.loordcore.util.JavaUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TileMain extends TileEntity implements ITickable, IDescSync, INBTSaver {

    private List<Field> NBTfieldlist = new ArrayList<>();
    private List<SyncField> descriptionFields;
    private int timer;
    private EnumFacing tileFacing;

    public TileMain() {
        super();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        fromNBT(compound);

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        toNBT(compound);
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
            if (!world.isRemote) {
                update2secSeverSide();
            } else update2secClientSide();
        }
        if (!world.isRemote) {
            updateServerSide();
        } else updateClientSide();
    }

    private void updateClientSide() {}

    private void updateServerSide() {}

    public void update2secSeverSide() {}

    public void update2secClientSide() {}

    public void onBlockPlacedBy(EntityLivingBase placer, @Nonnull ItemStack stack) {
        setTileFacing(placer.getHorizontalFacing());
    }

    @Override
    public Type getSyncType() {
        return Type.TILE_ENTITY;
    }

    @Override
    public List<SyncField> getDescriptionFields() {
        if (descriptionFields == null) {
            descriptionFields = NetworkUtils.getSyncFields(this, DescSync.class);
            for (SyncField field : descriptionFields) {
                field.update();
            }
        }
        return descriptionFields;
    }

    public EnumFacing getTileFacing() {
        return tileFacing;
    }

    public void setTileFacing(EnumFacing tileFacing) {
        this.tileFacing = tileFacing;
    }

    public void onGuiUpdate() {}

    @Override
    public void writeToPacket(NBTTagCompound tag) {}

    @Override
    public void readFromPacket(NBTTagCompound tag) {}

    @Override
    public BlockPos getPosition() {
        return pos;
    }

    @Override
    public void onDescUpdate() {}

    public void onNeighborChange(BlockPos neighbor) {}

    public void onBlockAdded() {}

    public void onBlockRemoval() {}

    protected void sendUpdateToClient() {
        NetworkHandler.sendToAllAround(new PacketDescription(this), world);
    }

    @Override
    public List<Field> getFieldList() {
        return NBTfieldlist;
    }

    @Override
    public void setFieldList(List<Field> fieldList) {
        this.NBTfieldlist = fieldList;
    }
}
