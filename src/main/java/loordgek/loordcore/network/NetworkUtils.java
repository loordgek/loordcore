package loordgek.loordcore.network;

import loordgek.extragenarators.util.ForgePower;
import loordgek.extragenarators.util.LogHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidTank;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/*
*    PneumaticCraft code. author = MineMaarten
*    https://github.com/MineMaarten/PneumaticCraft
*/
public class NetworkUtils {

    /*private static Map<Class, List<Field>> extraMarkedFields = new HashMap<Class, List<Field>>();
    public static void addExtraMarkedField(Class clazz, Field field){
        List<Field> markedFields = extraMarkedFields.get(clazz);
        if(markedFields == null) {
            markedFields = new ArrayList<Field>();
            extraMarkedFields.put(clazz, markedFields);
        }
        markedFields.add(field);
    }*/

    public static List<SyncField> getSyncFields(Object te, Class searchedAnnotation) {
        List<SyncField> syncedFields = new ArrayList<SyncField>();
        Class examinedClass = te.getClass();
        while (examinedClass != null) {
            for (Field field : examinedClass.getDeclaredFields()) {
                if (field.getAnnotation(searchedAnnotation) != null) {
                    syncedFields.addAll(getSyncFieldsForField(field, te, searchedAnnotation));
                }
            }
            examinedClass = examinedClass.getSuperclass();
        }
        return syncedFields;
    }

    private static List<SyncField> getSyncFieldsForField(Field field, Object te, Class searchedAnnotation) {
        boolean isLazy = field.getAnnotation(LazySync.class) != null;
        List<SyncField> syncedFields = new ArrayList<SyncField>();
        SyncField syncedField = getSyncFieldForField(field, te);
        if (syncedField != null) {
            syncedFields.add(syncedField.setLazy(isLazy));
            return syncedFields;
        } else {
            Object o;
            try {
                int filteredIndex = field.getAnnotation(FilteredSync.class) != null ? field.getAnnotation(FilteredSync.class).index() : -1;
                field.setAccessible(true);
                o = field.get(te);
                if (o instanceof int[]) {
                    int[] array = (int[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.Intfieldsync(te, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < array.length; i++) {
                            syncedFields.add(new SyncField.Intfieldsync(te, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }
                if (o instanceof float[]) {
                    float[] array = (float[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.floatfieldsync(te, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < array.length; i++) {
                            syncedFields.add(new SyncField.floatfieldsync(te, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }
                if (o instanceof double[]) {
                    double[] array = (double[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.doublefieldsync(te, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < array.length; i++) {
                            syncedFields.add(new SyncField.doublefieldsync(te, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }
                if (o instanceof long[]) {
                    long[] array = (long[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.longfieldsync(te, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < array.length; i++) {
                            syncedFields.add(new SyncField.longfieldsync(te, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }
                if (o instanceof boolean[]) {
                    boolean[] array = (boolean[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.booleanfieldsync(te, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < array.length; i++) {
                            syncedFields.add(new SyncField.booleanfieldsync(te, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }
                if (o instanceof short[]) {
                    short[] array = (short[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.shortfieldsync(te, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < array.length; i++) {
                            syncedFields.add(new SyncField.shortfieldsync(te, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }

                if (o instanceof String[]) {
                    String[] array = (String[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.Stringfieldsync(te, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < array.length; i++) {
                            syncedFields.add(new SyncField.Stringfieldsync(te, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }
                if (o.getClass().isArray() && o.getClass().getComponentType().isEnum()) {
                    Object[] enumArray = (Object[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.bytefieldsync(te, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < enumArray.length; i++) {
                            syncedFields.add(new SyncField.bytefieldsync(te, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }
                if (o instanceof ItemStack[]) {
                    ItemStack[] array = (ItemStack[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.ItemStackfieldsync(te, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < array.length; i++) {
                            syncedFields.add(new SyncField.ItemStackfieldsync(te, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }
                if (o instanceof FluidTank[]) {
                    FluidTank[] array = (FluidTank[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.FluidStackfieldsync(te, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < array.length; i++) {
                            syncedFields.add(new SyncField.FluidStackfieldsync(te, field).setArrayIndex(i).setLazy(isLazy));
                        }
                    }
                    return syncedFields;
                }
                if (o instanceof ForgePower[]) {
                    ForgePower[] array = (ForgePower[]) o;
                    if (filteredIndex >= 0) {
                        syncedFields.add(new SyncField.Energyfiedsync(te, field).setArrayIndex(filteredIndex).setLazy(isLazy));
                    } else {
                        for (int i = 0; i < array.length; i++) {
                            syncedFields.add(new SyncField.Energyfiedsync(te, field).setArrayIndex(i).setLazy(isLazy));
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
        if (ForgePower.class.isAssignableFrom(field.getType())) return new SyncField.Energyfiedsync(te, field);
        return null;
    }
}

