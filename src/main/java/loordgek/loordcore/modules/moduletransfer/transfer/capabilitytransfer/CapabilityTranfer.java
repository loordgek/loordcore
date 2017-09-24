package loordgek.loordcore.modules.moduletransfer.transfer.capabilitytransfer;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityTranfer {

    @CapabilityInject(ITransfer.class)
    public static Capability<ITransfer> TRANSFER_CAPABILITY = null;

    public static void register(){
        CapabilityManager.INSTANCE.register(ITransfer.class, new Capability.IStorage<ITransfer>() {
            @Override
            public NBTBase writeNBT(Capability<ITransfer> capability, ITransfer instance, EnumFacing side) {
                return null;
            }

            @Override
            public void readNBT(Capability<ITransfer> capability, ITransfer instance, EnumFacing side, NBTBase nbt) {

            }
        }, dummyTransfer::new);
    }


}
