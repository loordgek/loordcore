package loordgek.loordcore.network;

import io.netty.buffer.ByteBuf;
import loordgek.loordcore.util.LogHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/*
*    PneumaticCraft code. author = MineMaarten
*    https://github.com/MineMaarten/PneumaticCraft
*/
public class NetworkUtils {

    public static <T extends Annotation> List<SyncField> getSyncFields(Object owner, Class<T> searchedAnnotation) {
        List<SyncField> syncedFields = new ArrayList<SyncField>();
        Class examinedClass = owner.getClass();
        while (examinedClass != null) {
            for (Field field : examinedClass.getDeclaredFields()) {
                if (field.getAnnotation(PeekInnerFields.class) != null) {
                    field.setAccessible(true);
                    Object o;
                    try {
                        o = field.get(owner);
                        getSyncFields(o, searchedAnnotation);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    field.setAccessible(false);
                }
                else if (field.getAnnotation(searchedAnnotation) != null) {
                    syncedFields.addAll(getSyncFieldsForField(field, owner, searchedAnnotation));
                }
            }
            examinedClass = examinedClass.getSuperclass();
        }
        return syncedFields;
    }

    private static List<SyncField> getSyncFieldsForField(Field field, Object owner, Class searchedAnnotation) {
        boolean isLazy = field.getAnnotation(LazySync.class) != null;
        List<SyncField> syncedFields = new ArrayList<SyncField>();
        SyncField syncedField = getSyncFieldForField(field, owner);
        if (syncedField != null) {
            syncedFields.add(syncedField.setLazy(isLazy));
            return syncedFields;
        } else {
            Object o;
            try {
                int filteredIndex = field.getAnnotation(FilteredSync.class) != null ? field.getAnnotation(FilteredSync.class).index() : -1;
                field.setAccessible(true);
                o = field.get(owner);
                if (o instanceof int[]) {
                    int[] array = (int[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.Intfieldsync(owner, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < array.length; i++) {
                            syncedFields.add(new SyncField.Intfieldsync(owner, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }
                if (o instanceof float[]) {
                    float[] array = (float[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.floatfieldsync(owner, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < array.length; i++) {
                            syncedFields.add(new SyncField.floatfieldsync(owner, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }
                if (o instanceof double[]) {
                    double[] array = (double[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.doublefieldsync(owner, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < array.length; i++) {
                            syncedFields.add(new SyncField.doublefieldsync(owner, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }
                if (o instanceof long[]) {
                    long[] array = (long[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.longfieldsync(owner, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < array.length; i++) {
                            syncedFields.add(new SyncField.longfieldsync(owner, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }
                if (o instanceof boolean[]) {
                    boolean[] array = (boolean[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.booleanfieldsync(owner, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < array.length; i++) {
                            syncedFields.add(new SyncField.booleanfieldsync(owner, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }
                if (o instanceof short[]) {
                    short[] array = (short[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.shortfieldsync(owner, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < array.length; i++) {
                            syncedFields.add(new SyncField.shortfieldsync(owner, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }

                if (o instanceof String[]) {
                    String[] array = (String[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.Stringfieldsync(owner, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < array.length; i++) {
                            syncedFields.add(new SyncField.Stringfieldsync(owner, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }
                if (o.getClass().isArray() && o.getClass().getComponentType().isEnum()) {
                    Object[] enumArray = (Object[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.bytefieldsync(owner, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < enumArray.length; i++) {
                            syncedFields.add(new SyncField.bytefieldsync(owner, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }
                if (o instanceof ItemStack[]) {
                    ItemStack[] array = (ItemStack[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.ItemStackfieldsync(owner, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < array.length; i++) {
                            syncedFields.add(new SyncField.ItemStackfieldsync(owner, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }
                if (o instanceof FluidTank[]) {
                    FluidTank[] array = (FluidTank[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.FluidStackfieldsync(owner, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < array.length; i++) {
                            syncedFields.add(new SyncField.FluidStackfieldsync(owner, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }
                if (field.getType().isArray()) {
                    Object[] array = (Object[]) o;
                    for (Object obj : array) {
                        syncedFields.addAll(getSyncFields(obj, searchedAnnotation));
                    }
                } else {
                    syncedFields.addAll(getSyncFields(o, searchedAnnotation));
                }
                if (syncedFields.size() > 0) return syncedFields;
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogHelper.warn("Field " + field + " didn't produce any syncable fields!");
            return syncedFields;
        }
    }

    private static SyncField getSyncFieldForField(Field field, Object te) {
        if (int.class.isAssignableFrom(field.getType())) return new SyncField.Intfieldsync(te, field);
        if (float.class.isAssignableFrom(field.getType())) return new SyncField.floatfieldsync(te, field);
        if (double.class.isAssignableFrom(field.getType())) return new SyncField.doublefieldsync(te, field);
        if (boolean.class.isAssignableFrom(field.getType())) return new SyncField.booleanfieldsync(te, field);
        if (String.class.isAssignableFrom(field.getType())) return new SyncField.Stringfieldsync(te, field);
        if (field.getType().isEnum()) return new SyncField.bytefieldsync(te, field);
        if (ItemStack.class.isAssignableFrom(field.getType())) return new SyncField.ItemStackfieldsync(te, field);
        if (FluidTank.class.isAssignableFrom(field.getType())) return new SyncField.FluidStackfieldsync(te, field);
        return null;
    }

    public static byte getType(SyncField syncedField) {
        if (syncedField instanceof SyncField.bytefieldsync) return 0;
        else if (syncedField instanceof SyncField.shortfieldsync) return 1;
        else if (syncedField instanceof SyncField.Intfieldsync) return 2;
        else if (syncedField instanceof SyncField.longfieldsync) return 3;
        else if (syncedField instanceof SyncField.floatfieldsync) return 4;
        else if (syncedField instanceof SyncField.doublefieldsync) return 5;
        else if (syncedField instanceof SyncField.booleanfieldsync) return 6;
        else if (syncedField instanceof SyncField.Stringfieldsync) return 7;
        else if (syncedField instanceof SyncField.ItemStackfieldsync) return 8;
        else if (syncedField instanceof SyncField.FluidStackfieldsync) return 9;
        else {
            throw new IllegalArgumentException("Invalid sync type! " + syncedField);
        }
    }

    public static Object readField(PacketBuffer buf, int type) {
        switch (type) {
            case 0:
                return buf.readByte();
            case 1:
                return buf.readShort();
            case 2:
                return buf.readInt();
            case 3:
                return buf.readLong();
            case 4:
                return buf.readFloat();
            case 5:
                return buf.readDouble();
            case 6:
                return buf.readBoolean();
            case 7:
                return ByteBufUtils.readUTF8String(buf);
            case 8:
                return ByteBufUtils.readItemStack(buf);
            case 9:
                if (!buf.readBoolean()) return null;
                return new FluidStack(FluidRegistry.getFluid(ByteBufUtils.readUTF8String(buf)), buf.readInt(), ByteBufUtils.readTag(buf));

        }
        throw new IllegalArgumentException("Invalid sync type! " + type);
    }

    public static void writeField(PacketBuffer buf, Object value, int type) {
        switch (type) {
            case 0:
                buf.writeByte((Byte) value);
                break;
            case 1:
                buf.writeShort((Short) value);
                break;
            case 2:
                buf.writeInt((Integer) value);
                break;
            case 3:
                buf.writeLong((Long) value);
                break;
            case 4:
                buf.writeFloat((Float) value);
                break;
            case 5:
                buf.writeDouble((Double) value);
                break;
            case 6:
                buf.writeBoolean((Boolean) value);
                break;
            case 7:
                ByteBufUtils.writeUTF8String(buf, (String) value);
                break;
            case 8:
                ByteBufUtils.writeItemStack(buf, (ItemStack) value);
                break;
            case 9:
                buf.writeBoolean(value != null);
                if (value != null) {
                    FluidStack stack = (FluidStack) value;
                    ByteBufUtils.writeUTF8String(buf, stack.getFluid().getName());
                    buf.writeInt(stack.amount);
                    ByteBufUtils.writeTag(buf, stack.tag);
                }
                break;
        }
    }

    public static boolean isTypeThreadSafe(byte Type) {
        switch (Type) {
            case 0:
                return true;
            case 1:
                return true;
            case 2:
                return true;
            case 3:
                return true;
            case 4:
                return true;
            case 5:
                return true;
            case 6:
                return true;
            case 7:
                return true;
            case 8:
                return false;
            case 9:
                return false;
            default:
                return false;
        }
    }

    public static PacketBuffer writeItemStack(ByteBuf buf, ItemStack stack) {
        PacketBuffer buffer = new PacketBuffer(buf);

        buffer.writeBoolean(stack.isEmpty());

        {
            ByteBufUtils.writeRegistryEntry(buffer, stack.getItem());
            buffer.writeInt(stack.getCount());
            buffer.writeShort(stack.getMetadata());
            NBTTagCompound nbttagcompound = null;

            if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
                nbttagcompound = stack.getItem().getNBTShareTag(stack);
            }

            buffer.writeCompoundTag(nbttagcompound);
        }

        return buffer;
    }

    public static ItemStack readItemStack(PacketBuffer buffer) throws IOException {
        if (buffer.readBoolean()) {
            return ItemStack.EMPTY;
        } else {
            int count = buffer.readInt();
            int meta = buffer.readShort();
            ItemStack itemstack = new ItemStack(ByteBufUtils.readRegistryEntry(buffer, ForgeRegistries.ITEMS), count, meta);
            itemstack.setTagCompound(buffer.readCompoundTag());
            return itemstack;
        }
    }
}

