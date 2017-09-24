package loordgek.loordcore.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public interface IThreadSafeMessage extends IMessage {
    boolean isThreadSafe();
    void toPacketBuf(PacketBuffer buffer);
    void fromPacketBuf(PacketBuffer buffer);

    @Override
    default void fromBytes(ByteBuf buf){
        fromPacketBuf(new PacketBuffer(buf));
    }

    @Override
    default void toBytes(ByteBuf buf){
        toPacketBuf(new PacketBuffer(buf));
    }
}
