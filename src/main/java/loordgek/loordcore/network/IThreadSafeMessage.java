package loordgek.loordcore.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public interface IThreadSafeMessage extends IMessage {
    boolean isThreadSafe();
    void toPacketbuf(PacketBuffer buffer);
    void fromPacketbuf(PacketBuffer buffer);

    @Override
    default void fromBytes(ByteBuf buf){
        fromPacketbuf(new PacketBuffer(buf));
    }

    @Override
    default void toBytes(ByteBuf buf){
        toPacketbuf(new PacketBuffer(buf));
    }
}
