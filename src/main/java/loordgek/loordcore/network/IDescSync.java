package loordgek.loordcore.network;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/*
*    PneumaticCraft code. author = MineMaarten
*    https://github.com/MineMaarten/PneumaticCraft
*/
public interface IDescSync {

    enum Type{
        TILE_ENTITY, SEMI_BLOCK;
    }
    Type getSyncType();

    List<SyncField> getDescriptionFields();

    void writeToPacket(NBTTagCompound tag);

    void readFromPacket(NBTTagCompound tag);

    BlockPos getPosition();

    void onDescUpdate();

}
