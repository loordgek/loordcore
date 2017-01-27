package loordgek.loordcore.network;

import loordgek.extragenarators.util.ForgePower;
import loordgek.extragenarators.util.LogHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;

/*
*    PneumaticCraft code. author = MineMaarten
*    https://github.com/MineMaarten/PneumaticCraft
*/
public abstract class SyncField<T> {
    private final Field field;
    private final Object te;
    private T lastValue;
    private int arrayIndex = -1;
    private boolean isLazy;

    public SyncField(Object te, Field field) {
        this.field = field;
        field.setAccessible(true);
        this.te = te;
    }

    public SyncField setArrayIndex(int arrayIndex) {
        this.arrayIndex = arrayIndex;
        return this;
    }

    public SyncField setLazy(boolean lazy) {
        this.isLazy = lazy;
        return this;
    }

    public boolean update() {
        try {
            T value = arrayIndex >= 0 ? getValueForArray(field.get(te), arrayIndex) : retrieveValue(field, te);
            if (lastValue == null && value != null || lastValue != null && !equals(lastValue, value)) {
                lastValue = value == null ? null : copyWhenNecessary(value);
                return !isLazy;
            }
        } catch (Throwable e) {
            LogHelper.error("A problem occured when trying to sync the field of " + te.toString() + ". Field: " + field.toString());
            e.printStackTrace();
        }
        return false;
    }

    protected boolean equals(T oldValue, T newValue) {
        return oldValue.equals(newValue);
    }

    protected T copyWhenNecessary(T oldValue) {
        return oldValue;
    }

    protected T retrieveValue(Field field, Object te) throws Exception {
        return (T) field.get(te);
    }

    protected void injectValue(Field field, Object te, T value) throws Exception {
        field.set(te, value);
    }

    protected abstract T getValueForArray(Object array, int index);

    protected abstract void setValueForArray(Object array, int index, T value) throws Exception;

    public T getValue() {
        return lastValue;
    }

    public void setValue(T value) {
        try {
            if (arrayIndex >= 0) {
                setValueForArray(field.get(te), arrayIndex, value);
            } else {
                injectValue(field, te, value);
            }
        } catch (Exception e) {
            LogHelper.error("A problem occured when trying to sync the field of " + te.toString() + ". Field: " + field.toString());
            e.printStackTrace();
        }
    }

    public static class bytefieldsync extends SyncField<Byte> {
        public bytefieldsync(Object te, Field field) {
            super(te, field);
        }

        @Override
        protected Byte getValueForArray(Object array, int index) {
            return ((byte[]) array)[index];
        }

        @Override
        protected void setValueForArray(Object array, int index, Byte value) throws Exception {
            ((byte[]) array)[index] = value;
        }

        @Override
        protected Byte retrieveValue(Field field, Object te) throws Exception {
            Object[] enumTypes = field.getType().getEnumConstants();
            return (byte) ArrayUtils.indexOf(enumTypes, field.get(te));
        }

        @Override
        protected void injectValue(Field field, Object te, Byte value) throws Exception {
            Object enumType = field.getType().getEnumConstants()[value];
            field.set(te, enumType);
        }
    }

    public static class shortfieldsync extends SyncField<Short> {

        public shortfieldsync(Object te, Field field) {
            super(te, field);
        }

        @Override
        protected Short getValueForArray(Object array, int index) {
            return ((short[]) array)[index];
        }

        @Override
        protected void setValueForArray(Object array, int index, Short value) throws Exception {
            ((short[]) array)[index] = value;
        }
    }

    public static class Intfieldsync extends SyncField<Integer> {
        public Intfieldsync(Object te, Field field) {
            super(te, field);
        }

        @Override
        protected Integer getValueForArray(Object array, int index) {
            return ((int[]) array)[index];
        }

        @Override
        protected void setValueForArray(Object array, int index, Integer value) throws Exception {
            ((int[]) array)[index] = value;

        }
    }

    public static class longfieldsync extends SyncField<Long> {

        public longfieldsync(Object te, Field field) {
            super(te, field);
        }

        @Override
        protected Long getValueForArray(Object array, int index) {
            return ((long[]) array)[index];
        }

        @Override
        protected void setValueForArray(Object array, int index, Long value) throws Exception {
            ((long[]) array)[index] = value;

        }
    }

    public static class floatfieldsync extends SyncField<Float> {
        public floatfieldsync(Object te, Field field) {
            super(te, field);
        }

        @Override
        protected Float getValueForArray(Object array, int index) {
            return ((float[]) array)[index];
        }

        @Override
        protected void setValueForArray(Object array, int index, Float value) throws Exception {
            ((float[]) array)[index] = value;

        }
    }

    public static class doublefieldsync extends SyncField<Double> {

        public doublefieldsync(Object te, Field field) {
            super(te, field);
        }

        @Override
        protected Double getValueForArray(Object array, int index) {
            return ((double[]) array)[index];
        }

        @Override
        protected void setValueForArray(Object array, int index, Double value) throws Exception {
            ((double[]) array)[index] = value;
        }
    }

    public static class booleanfieldsync extends SyncField<Boolean> {

        public booleanfieldsync(Object te, Field field) {
            super(te, field);
        }

        @Override
        protected Boolean getValueForArray(Object array, int index) {
            return ((boolean[]) array)[index];
        }

        @Override
        protected void setValueForArray(Object array, int index, Boolean value) throws Exception {
            ((boolean[]) array)[index] = value;
        }
    }

    public static class Stringfieldsync extends SyncField<String> {
        public Stringfieldsync(Object te, Field field) {
            super(te, field);
        }

        @Override
        protected String getValueForArray(Object array, int index) {
            return ((String[]) array)[index];
        }

        @Override
        protected void setValueForArray(Object array, int index, String value) throws Exception {
            ((String[]) array)[index] = value;
        }
    }

    public static class ItemStackfieldsync extends SyncField<ItemStack> {
        public ItemStackfieldsync(Object te, Field field) {
            super(te, field);
        }

        @Override
        protected ItemStack getValueForArray(Object array, int index) {
            return ((ItemStack[]) array)[index];
        }

        @Override
        protected void setValueForArray(Object array, int index, ItemStack value) throws Exception {
            ((ItemStack[]) array)[index] = value;
        }
    }

    public static class FluidStackfieldsync extends SyncField<FluidStack> {
        public FluidStackfieldsync(Object te, Field field) {
            super(te, field);
        }

        @Override
        protected FluidStack getValueForArray(Object array, int index) {
            return ((FluidTank[]) array)[index].getFluid();
        }

        @Override
        protected void setValueForArray(Object array, int index, FluidStack value) throws Exception {
            ((FluidTank[]) array)[index].setFluid(value);
        }

        @Override
        protected FluidStack retrieveValue(Field field, Object te) throws Exception {
            FluidTank tank = (FluidTank) field.get(te);
            return tank.getFluid();
        }

        @Override
        protected void injectValue(Field field, Object te, FluidStack value) throws Exception {
            FluidTank tank = (FluidTank) field.get(te);
            tank.setFluid(value);
        }

        @Override
        protected boolean equals(FluidStack oldValue, FluidStack newValue) {
            return oldValue.isFluidEqual(newValue) && oldValue.amount == newValue.amount;
        }

        @Override
        protected FluidStack copyWhenNecessary(FluidStack oldValue) {
            return oldValue.copy();
        }

    }

    public static class Energyfiedsync extends SyncField<PowerSync> {
        public Energyfiedsync(Object te, Field field) {
            super(te, field);
        }

        @Override
        protected PowerSync getValueForArray(Object array, int index) {
            return new PowerSync(((ForgePower[]) array)[index].getEnergyStored(), ((ForgePower[]) array)[index].getMaxEnergyStored());
        }

        @Override
        protected void setValueForArray(Object array, int index, PowerSync value) throws Exception {
            ((ForgePower[]) array)[index].setEnergy(value.getEnergy());
        }

        @Override
        protected PowerSync retrieveValue(Field field, Object te) throws Exception {
            ForgePower power = (ForgePower) field.get(te);
            return new PowerSync(power.getEnergyStored(), power.getMaxEnergyStored());
        }

        @Override
        protected void injectValue(Field field, Object te, PowerSync value) throws Exception {
            ForgePower power = (ForgePower) field.get(te);
            power.setEnergy(value.getEnergy());
            power.setCapacity(value.getEnergystore());
        }
    }
}
