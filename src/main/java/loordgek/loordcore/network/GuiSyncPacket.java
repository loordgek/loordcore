package loordgek.loordcore.network;

import io.netty.buffer.ByteBuf;
import loordgek.extragenarators.container.container.ContainerExtragenarators;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/*
*    PneumaticCraft code. author = MineMaarten
*    https://github.com/MineMaarten/PneumaticCraft
*/
public class GuiSyncPacket extends AbstractPacket<GuiSyncPacket> {
    private int syncId;
    private Object value;
    private byte type;

    public GuiSyncPacket() {
    }

    public GuiSyncPacket(int syncId, SyncField syncField) {
        this.syncId = syncId;
        value = syncField.getValue();
        type = getType(syncField);
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
        else if (syncedField instanceof SyncField.Energyfiedsync) return 10;

        else {
            throw new IllegalArgumentException("Invalid sync type! " + syncedField);
        }
    }

    public static Object readField(ByteBuf buf, int type) {
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
            case 10:
                return new PowerSync(buf.readInt(), buf.readInt());
        }
        throw new IllegalArgumentException("Invalid sync type! " + type);
    }

    public static void writeField(ByteBuf buf, Object value, int type) {
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
            case 10:
                PowerSync powerSync = (PowerSync) value;
                buf.writeInt(powerSync.getEnergy());
                buf.writeInt(powerSync.getEnergystore());
                break;
        }
    }

    private boolean isTypeThreadSafe(byte Type) {
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
            case 10:
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean isThreadSafe() {
        return isTypeThreadSafe(type);
    }

    @Override
    public void toPacketbuf(PacketBuffer buffer) {
        buffer.writeInt(syncId);
        buffer.writeByte(type);
        writeField(buffer, value, type);
    }

    @Override
    public void fromPacketbuf(PacketBuffer buffer) {
        syncId = buffer.readInt();
        type = buffer.readByte();
        value = readField(buffer, type);
    }

    @Override
    public void handleClientSide(GuiSyncPacket message, EntityPlayer player, boolean handledFromNettyThread) {
        Container container = player.openContainer;
        if (container instanceof ContainerExtragenarators) {
            ((ContainerExtragenarators) container).Updatefield(message.syncId, message.value);
        }
    }

    @Override
    public void handleServerSide(GuiSyncPacket message, EntityPlayer player, boolean handledFromNettyThread) {}
}

