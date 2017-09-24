package loordgek.loordcore.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

public class ForgePower implements IEnergyStorage, INBTSerializable<NBTTagCompound> {
    protected final int initialcapacity;
    protected double energy;
    protected double capacity;
    protected int maxReceive;
    protected int maxExtract;
    protected float Floatenergy;

    public ForgePower(int capacity)
    {
        this(capacity, capacity, capacity);
    }

    public ForgePower(int capacity, int maxTransfer)
    {
        this(capacity, maxTransfer, maxTransfer);
    }

    public ForgePower(int capacity, int maxReceive, int maxExtract)
    {
        this.initialcapacity = capacity;
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;

        int energyReceived = (int) Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        int energyExtracted = (int) Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    public float floatreceiveEnergy(float maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;
        float energyReceived = Math.min((float) capacity - Floatenergy, Math.min((float) this.maxReceive, maxReceive));
        if (!simulate) {
            Floatenergy += energyReceived;
            int floor_float = MathHelper.floor(Floatenergy);
            receiveEnergy(floor_float, false);
            Floatenergy -= floor_float;
        }

        return energyReceived;
    }

    // TODO: 10-10-2016
    @Deprecated
    public float floatextractEnergy(float maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        float energyExtracted = (float) Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            Floatenergy -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return (int) energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return (int) capacity;
    }

    @Override
    public boolean canExtract()
    {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive()
    {
        return this.maxReceive > 0;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setDouble("energy", energy);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        energy = nbt.getDouble("energy");

    }

    public void Increasecapacity(int TimesMultiplier){
        capacity = initialcapacity * TimesMultiplier;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }
}
