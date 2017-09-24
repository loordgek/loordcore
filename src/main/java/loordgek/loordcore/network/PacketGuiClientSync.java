package loordgek.loordcore.network;

import loordgek.loordcore.container.container.ContainerMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.PacketBuffer;

/*
*    PneumaticCraft code. author = MineMaarten
*    https://github.com/MineMaarten/PneumaticCraft
*/
public class PacketGuiSync extends AbstractPacket<PacketGuiSync> {
    private int syncId;
    private Object value;
    private byte type;

    public PacketGuiSync() {}

    public PacketGuiSync(int syncId, SyncField syncField) {
        this.syncId = syncId;
        value = syncField.getValue();
        type = NetworkUtils.getType(syncField);
    }

    @Override
    public boolean isThreadSafe() {
        return NetworkUtils.isTypeThreadSafe(type);
    }

    @Override
    public void toPacketbuf(PacketBuffer buffer) {
        buffer.writeInt(syncId);
        buffer.writeByte(type);
        NetworkUtils.writeField(buffer, value, type);
    }

    @Override
    public void fromPacketbuf(PacketBuffer buffer) {
        syncId = buffer.readInt();
        type = buffer.readByte();
        value = NetworkUtils.readField(buffer, type);
    }

    @Override
    public void handleClientSide(PacketGuiSync message, EntityPlayer player, boolean handledFromNettyThread) {
        Container container = player.openContainer;
        if (container instanceof ContainerMain) {
            ((ContainerMain) container).Updatefield(message.syncId, message.value);
        }
    }
}

