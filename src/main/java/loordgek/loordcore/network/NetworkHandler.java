package loordgek.loordcore.network;

import loordgek.extragenarators.ref.Reference;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
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
        INSTANCE.registerMessage(GuiSyncPacket.class, GuiSyncPacket.class, ++NetID, Side.CLIENT);
    }

    public static void SentTo(IMessage message, EntityPlayerMP playerMP){
        INSTANCE.sendTo(message, playerMP);
    }

    public static void sentAllAround(IMessage message, NetworkRegistry.TargetPoint point){
        INSTANCE.sendToAllAround(message, point);
    }
    public static void sentAllAroundTile(IMessage message, TileEntity tileEntity){
        sentAllAround(message, new NetworkRegistry.TargetPoint(tileEntity.getWorld().provider.getDimension(), tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ(), 64d));
    }
}

