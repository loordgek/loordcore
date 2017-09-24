package loordgek.loordcore.nbt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidTank;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class NBTUtil {

    public static NBTTagCompound getFieldswrite(Object te, List<Field> nbtFieldsList) throws IllegalAccessException {
        NBTTagList nbt = new NBTTagList();
        for (Field field : nbtFieldsList) {
            field.setAccessible(true);
            Object object = field.get(te);
            NBTTagCompound nbtcomp = new NBTTagCompound();
            nbtcomp.setString("fieldname", field.getName());

            if (field.getType() == int.class) {
                int Int = (Integer) object;
                nbtcomp.setInteger(field.getName(), Int);
                nbt.appendTag(nbtcomp);
                continue;
            }
            if (object instanceof int[]) {
                int[] array = (int[]) object;
                nbtcomp.setIntArray(field.getName(), array);
                nbt.appendTag(nbtcomp);
                continue;
            }
            if (object instanceof INBTSerializable) {
                INBTSerializable serializable = (INBTSerializable) object;
                nbtcomp.setTag(field.getName(), serializable.serializeNBT());
                nbt.appendTag(nbtcomp);
                continue;
            }
            if (object instanceof FluidTank){
                FluidTank fluidTank = (FluidTank) object;
                nbtcomp.setTag(field.getName(), fluidTank.writeToNBT(nbtcomp));
                nbt.appendTag(nbtcomp);
                continue;
            }
            if (object instanceof UUID){
                UUID uuid = (UUID) object;
                nbtcomp.setUniqueId(field.getName(), uuid);
            }
        }
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setTag("NBTave", nbt);
        return tagCompound;
        //return compound;
    }

    public static void getFieldsread(Object te, NBTTagCompound compound) throws IllegalAccessException, NoSuchFieldException {
        NBTTagList nbttag = compound.getTagList("NBTave", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < nbttag.tagCount(); i++) {
            NBTTagCompound comp = nbttag.getCompoundTagAt(i);
            Class clazz = te.getClass();
            Field field = null;
            if (clazz.getField(comp.getString("fieldname")) == null) {
                clazz = clazz.getSuperclass();
            } else field = clazz.getField(comp.getString("fieldname"));

            if (field == null)
                return;

            field.setAccessible(true);
            Object object = field.get(te);

            if (field.getType() == int.class) {
                field.set(te, comp.getInteger(field.getName()));
                continue;
            }
            if (object instanceof int[]) {
                field.set(te, comp.getIntArray(field.getName()));
                continue;
            }
            if (object instanceof INBTSerializable) {
                INBTSerializable serializable = ((INBTSerializable) object);
                serializable.deserializeNBT(comp.getCompoundTag(field.getName()));
                continue;
            }
            if (object instanceof FluidTank){
                FluidTank fluidTank = (FluidTank) object;
                fluidTank.readFromNBT(comp.getCompoundTag(field.getName()));
            }
            if (object instanceof UUID){
                field.set(te, comp.getUniqueId(field.getName()));

            }
        }
    }
}
