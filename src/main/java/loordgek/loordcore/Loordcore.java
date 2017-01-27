package loordgek.loordcore;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
        modid = Loordcore.MOD_ID,
        name = Loordcore.MOD_NAME,
        version = Loordcore.VERSION
)
public class Loordcore {

    public static final String MOD_ID = "loordcore";
    public static final String MOD_NAME = "Loordcore";
    public static final String VERSION = "1.11.2-0.1";

    @EventHandler
    public void init(FMLInitializationEvent event) {

    }
}
