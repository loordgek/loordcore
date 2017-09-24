package loordgek.extragenarators.nbt;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface INBTSerialSaver extends INBTSerializable<NBTTagCompound>, INBTSaver {

    @Override
    default NBTTagCompound serializeNBT(){
        return toNBT(new NBTTagCompound());
    }

    @Override
    default void deserializeNBT(NBTTagCompound nbt){
        fromNBT(nbt);
    }

}
