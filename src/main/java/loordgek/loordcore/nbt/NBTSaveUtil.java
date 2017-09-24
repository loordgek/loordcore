package loordgek.extragenarators.nbt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidTank;

import java.lang.reflect.Field;
import java.util.List;

public class NBTSaveUtil {

    public static NBTTagCompound getFieldsWrite(Object te, List<Field> nbtFieldsList) throws IllegalAccessException {
        NBTTagList nbtTagList = new NBTTagList();
        for (Field field : nbtFieldsList) {
            field.setAccessible(true);
            Object object = field.get(te);
            NBTTagCompound compound = new NBTTagCompound();
            compound.setString("fieldname", field.getName());

            compound.setString(field.getName(), field.getName());
            if (Number.class.isAssignableFrom(field.getType())) {
                if (byte.class.isAssignableFrom(field.getType())) {
                    byte b = (Byte) object;
                    compound.setByte(field.getName(), b);
                    nbtTagList.appendTag(compound);
                    continue;
                }
                if (short.class.isAssignableFrom(field.getType())) {
                    Short s = (Short) object;
                    compound.setShort(field.getName(), s);
                    nbtTagList.appendTag(compound);
                    continue;
                }
                if (int.class.isAssignableFrom(field.getType())) {
                    int Int = (Integer) object;
                    compound.setInteger(field.getName(), Int);
                    nbtTagList.appendTag(compound);
                    continue;
                }
                if (long.class.isAssignableFrom(field.getType())) {
                    long l = (Long) object;
                    compound.setLong(field.getName(), l);
                    nbtTagList.appendTag(compound);
                    continue;
                }
                if (double.class.isAssignableFrom(field.getType())) {
                    double d = (Double) object;
                    compound.setDouble(field.getName(), d);
                    nbtTagList.appendTag(compound);
                    continue;
                }
                if (float.class.isAssignableFrom(field.getType())) {
                    float f = (Float) object;
                    compound.setFloat(field.getName(), f);
                    nbtTagList.appendTag(compound);
                    continue;
                }
            }
            if (object instanceof INBTSerializable) {
                INBTSerializable serializable = (INBTSerializable) object;
                compound.setTag(field.getName(), serializable.serializeNBT());
                nbtTagList.appendTag(compound);
                continue;
            }
            if (object instanceof FluidTank) {
                FluidTank fluidTank = (FluidTank) object;
                fluidTank.writeToNBT(compound);
                nbtTagList.appendTag(compound);
            }
            field.setAccessible(false);
        }
        NBTTagCompound compound = new NBTTagCompound();
        compound.setTag("NBTave", nbtTagList);
        return compound;
    }


    public static void getFieldsRead(Object te, NBTTagCompound compound) throws IllegalAccessException, NoSuchFieldException {
        NBTTagList nbttag = compound.getTagList("NBTave", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < nbttag.tagCount(); i++) {
            NBTTagCompound comp = nbttag.getCompoundTagAt(i);
            Class clazz = te.getClass();
            Field field = null;
            if (clazz.getField(comp.getString("fieldname")) == null) {
                clazz = clazz.getSuperclass();
            } else field = clazz.getField(comp.getString("fieldname"));

            if (field == null)
                continue;

            field.setAccessible(true);
            Object object = field.get(te);

            if (Number.class.isAssignableFrom(field.getType())) {
                if (byte.class.isAssignableFrom(field.getType())) {
                    field.set(te, comp.getByte(field.getName()));
                    continue;
                }
                if (short.class.isAssignableFrom(field.getType())) {
                    field.set(te, comp.getShort(field.getName()));
                    continue;
                }
                if (int.class.isAssignableFrom(field.getType())) {
                    field.set(te, comp.getInteger(field.getName()));
                    continue;
                }
                if (long.class.isAssignableFrom(field.getType())) {
                    field.set(te, comp.getLong(field.getName()));
                    continue;
                }
                if (double.class.isAssignableFrom(field.getType())) {
                    field.set(te, comp.getDouble(field.getName()));
                    continue;
                }
                if (float.class.isAssignableFrom(field.getType())) {
                    field.set(te, comp.getFloat(field.getName()));
                    continue;
                }
            }

            if (object instanceof INBTSerializable) {
                INBTSerializable serializable = ((INBTSerializable) object);
                serializable.deserializeNBT(comp.getCompoundTag(field.getName()));
                continue;
            }
            if (object instanceof FluidTank) {
                FluidTank fluidTank = (FluidTank) object;
                fluidTank.readFromNBT(comp);
            }
        }
    }

}
