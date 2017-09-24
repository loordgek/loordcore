package loordgek.loordcore.testmod;


import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = "testmod")
public class Maintest {

    @Mod.Instance
    public static Maintest maintest;

    @Mod.EventBusSubscriber
    public static class regestryclass{
        @SubscribeEvent
        public void onItemRegister(RegistryEvent.Register<Block> event) {
        }

        @SubscribeEvent
        public void onBlockRegister(RegistryEvent.Register<Item> event) {

        }
    }
}
