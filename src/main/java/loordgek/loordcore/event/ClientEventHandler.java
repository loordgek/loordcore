package loordgek.loordcore.event;

import loordgek.loordcore.Registar.BlockPreRegistar;
import loordgek.loordcore.Registar.ItemPreRegistar;
import loordgek.loordcore.ref.Reference;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Reference.MODINFO.MOD_ID, value = Side.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        BlockPreRegistar.RegistarRenders();
        ItemPreRegistar.RegistarRenders();
    }
}
