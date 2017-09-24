package loordgek.loordcore.event;

import loordgek.loordcore.Registar.BlockPreRegistar;
import loordgek.loordcore.Registar.ItemPreRegistar;
import loordgek.loordcore.ref.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Reference.MODINFO.MOD_ID)
public class CommenEventHandler {
    @SubscribeEvent
    public static void onRegistryRegisterBlocks(RegistryEvent.Register<Block> event) {
        BlockPreRegistar.Registarblock(event.getRegistry());

    }

    @SubscribeEvent
    public static void onRegistryRegisterItems(RegistryEvent.Register<Item> event) {
        BlockPreRegistar.RegistarItemBlock(event.getRegistry());
        ItemPreRegistar.RegistarItems(event.getRegistry());

    }
}
