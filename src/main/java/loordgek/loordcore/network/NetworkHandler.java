package loordgek.loordcore.network;

import loordgek.loordcore.ref.Reference;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/*
*    PneumaticCraft code. author = MineMaarten
*    https://github.com/MineMaarten/PneumaticCraft
*/
public class NetworkHandler {
    private static int NetID;
    private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODINFO.MOD_NAME);

    public static void initNetwork(){
        INSTANCE.registerMessage(PacketGuiClientSync.class, PacketGuiClientSync.class, ++NetID, Side.CLIENT);
        INSTANCE.registerMessage(PacketGuiServerSync.class, PacketGuiServerSync.class, ++NetID, Side.CLIENT);
        INSTANCE.registerMessage(PacketDescription.class, PacketDescription.class, ++NetID, Side.CLIENT);
        INSTANCE.registerMessage(PacketTileRequest.class, PacketTileRequest.class, ++NetID, Side.SERVER);
    }

    public static void SentTo(IMessage message, EntityPlayerMP playerMP){
        INSTANCE.sendTo(message, playerMP);
    }

    public static void sentAllAround(IMessage message, NetworkRegistry.TargetPoint point){
        INSTANCE.sendToAllAround(message, point);
    }

    public static void sendToAllAround(PacketLocationInt message, World world){
        sentAllAround(message, message.getTargetPoint(world));
    }

    public static void sentToServer(IMessage message){
        INSTANCE.sendToServer(message);
    }

    public static void sendToPlayer(IMessage message, EntityPlayerMP player) {
        INSTANCE.sendTo(message, player);
    }

    public static void sendNetworkPacketTile(IMessage message, TileEntity tileEntity){
        sendNetworkPacket(message, tileEntity.getPos(), tileEntity.getWorld());
    }

    public static void sendNetworkPacket(IMessage message, BlockPos pos, World world) {
        if (!(world instanceof WorldServer)) {
            return;
        }

        WorldServer worldServer = (WorldServer) world;
        PlayerChunkMap playerManager = worldServer.getPlayerChunkMap();

        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;

        for (Object playerObj : world.playerEntities) {
            if (playerObj instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) playerObj;

                if (playerManager.isPlayerWatchingChunk(player, chunkX, chunkZ)) {
                    sendToPlayer(message, player);
                }
            }
        }
    }

}

