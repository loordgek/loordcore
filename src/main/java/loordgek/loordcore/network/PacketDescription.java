package loordgek.loordcore.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.List;


public class PacketDescription extends LocationIntPacket<PacketDescription>{
    private byte[] types;
    private Object[] values;
    private NBTTagCompound extraData;
    private IDescSync.Type type;

    public PacketDescription(){}

    public PacketDescription(IDescSync te){
        super(te.getPosition());
        type = te.getSyncType();
        values = new Object[te.getDescriptionFields().size()];
        types = new byte[values.length];
        for(int i = 0; i < values.length; i++) {
            values[i] = te.getDescriptionFields().get(i).getValue();
            types[i] = GuiSyncPacket.getType(te.getDescriptionFields().get(i));
        }
        extraData = new NBTTagCompound();
        te.writeToPacket(extraData);
    }

    @Override
    public void toPacketbuf(PacketBuffer buf) {
        super.toPacketbuf(buf);
        buf.writeByte(type.ordinal());
        buf.writeInt(values.length);
        for(int i = 0; i < types.length; i++) {
            buf.writeByte(types[i]);
            GuiSyncPacket.writeField(buf, values[i], types[i]);
        }
        ByteBufUtils.writeTag(buf, extraData);
    }

    @Override
    public void fromPacketbuf(PacketBuffer buf) {
        super.fromPacketbuf(buf);
        type = IDescSync.Type.values()[buf.readByte()];
        int dataAmount = buf.readInt();
        types = new byte[dataAmount];
        values = new Object[dataAmount];
        for(int i = 0; i < dataAmount; i++) {
            types[i] = buf.readByte();
            values[i] = GuiSyncPacket.readField(buf, types[i]);
        }
        extraData = ByteBufUtils.readTag(buf);
    }

    @Override
    public boolean isThreadSafe() {
        return false;
    }

    public static Object getSyncableForType(LocationIntPacket message, EntityPlayer player, IDescSync.Type type){
        switch(type){
            case TILE_ENTITY:
                return message.getTileEntity(player.world);
            case SEMI_BLOCK:
                return null;
        }
        return null;
    }

    @Override
    public void handleClientSide(PacketDescription message, EntityPlayer player, boolean handledFromNettyThread){
        if(player.world.isBlockLoaded(message.pos)) {
            Object syncable = getSyncableForType(message, player, message.type);
            if(syncable instanceof IDescSync) {
                IDescSync descSynced = (IDescSync)syncable;
                List<SyncField> descFields = descSynced.getDescriptionFields();
                if(descFields != null && descFields.size() == message.types.length) {
                    for(int i = 0; i < descFields.size(); i++) {
                        descFields.get(i).setValue(message.values[i]);
                    }
                }
                descSynced.readFromPacket(message.extraData);
                descSynced.onDescUpdate();
            }
        }
    }

    @Override
    public void handleServerSide(PacketDescription message, EntityPlayer player, boolean handledFromNettyThread) {

    }
}
