package loordgek.extragenarators.nbt;

import loordgek.extragenarators.util.JavaUtil;
import loordgek.extragenarators.util.LogHelper;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.Field;
import java.util.List;

public interface INBTSaver {
    String NBTKey = "fieldNBTsafe";

    List<Field> getFieldList();

    void setFieldList(List<Field> fieldList);

    default NBTTagCompound toNBT(NBTTagCompound compound){

        if (getFieldList() == null){
            setFieldList(JavaUtil.GetFields(getNBTClass(), NBTSave.class));
        }
        try {
            compound.setTag(NBTKey, NBTSaveUtil.getFieldsWrite(getNBTClass(), getFieldList()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return compound;
    }
    default void fromNBT(NBTTagCompound compound){
        if (getFieldList() == null){
            setFieldList(JavaUtil.GetFields(getNBTClass(), NBTSave.class));
        }
        try {
            NBTSaveUtil.getFieldsRead(getNBTClass(), compound.getCompoundTag(NBTKey));
        } catch (IllegalAccessException e) {
            LogHelper.fatal("IllegalAccessException");
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            LogHelper.fatal("NoSuchFieldException");
            e.printStackTrace();
        }
    }

    default Object getNBTClass(){
        return this;
    }
}
