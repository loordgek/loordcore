package loordgek.loordcore;

import loordgek.loordcore.event.ModuleManagerConstructionEvent;
import loordgek.loordcore.moduleloader.ModuleLoader;
import loordgek.loordcore.network.NetworkHandler;
import loordgek.loordcore.proxy.IProxy;
import loordgek.loordcore.ref.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MODINFO.MOD_ID, name = Reference.MODINFO.MOD_NAME, version = Reference.MODINFO.VERSION)

public class Loordcore {

    @SidedProxy(clientSide = Reference.MODINFO.CLIENT_PROXY_CLASS, serverSide = Reference.MODINFO.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @EventHandler
    public void onFMLConstruction(FMLConstructionEvent event) {
        MinecraftForge.EVENT_BUS.post(new ModuleManagerConstructionEvent(ModuleLoader.LOADER));
    }

    @EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event) {
        ModuleLoader.LOADER.invokeFMLEvent(event, Reference.MODINFO.MOD_ID);
        NetworkHandler.initNetwork();
    }

    @EventHandler
    public void onFMLInitialization(FMLInitializationEvent event) {
        ModuleLoader.LOADER.invokeFMLEvent(event, Reference.MODINFO.MOD_ID);
    }

    @EventHandler
    public void onFMLPostInitialization(FMLPostInitializationEvent event) {
        ModuleLoader.LOADER.invokeFMLEvent(event, Reference.MODINFO.MOD_ID);
    }
}