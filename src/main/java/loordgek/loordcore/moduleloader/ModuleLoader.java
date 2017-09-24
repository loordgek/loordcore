package loordgek.loordcore.moduleloader;

import loordgek.loordcore.event.EventBlockPreRegistar;
import loordgek.loordcore.event.EventItemPreRegistar;
import loordgek.loordcore.ref.Reference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Reference.MODINFO.MOD_ID)
public class ModuleController implements IModuleContoller {
    public static final ModuleController controller = new ModuleController();
    private final Map<String, ModuleManager> moduleManagers = new HashMap<>();

    public void Add(String modid, ModuleManager manager){
        moduleManagers.put(modid, manager);
        manager.setupModules();
        manager.ModuleInit();
    }

    public void invokeEvent(FMLEvent event, String modID){
        moduleManagers.get(modID).invokeEvent(event);
    }

    @SubscribeEvent
    public void onBlockPreRegistar(EventBlockPreRegistar event) {
        for (Object moduleManagers : moduleManagers.values()){
            invokeHandlers(moduleManagers, event);
        }
    }

    @SubscribeEvent
    public void onItemPreRegistar(EventItemPreRegistar event) {
        for (Object moduleManagers : moduleManagers.values()){
            invokeHandlers(moduleManagers, event);
        }
    }

    public void onFMLPreInitialization(FMLPreInitializationEvent event) {
        for (Object moduleManagers : moduleManagers.values()){
            invokeHandlers(moduleManagers, event);
        }
    }

    public void onFMLInitialization(FMLInitializationEvent event) {
        for (Object moduleManagers : moduleManagers.values()){
            invokeHandlers(moduleManagers, event);
        }
    }

    public void onFMLPostInitialization(FMLPostInitializationEvent event) {
        for (Object moduleManagers : moduleManagers.values()){
            invokeHandlers(moduleManagers, event);
        }
    }

    private void invokeHandlers(Object module, Object event) {
        for (Method method : module.getClass().getDeclaredMethods()) {
            try {if (method.getAnnotation(MController.class) == null)
                    continue;
                if (method.getParameterTypes().length != 1)
                    continue;
                if (method.getParameterTypes()[0].isAssignableFrom(event.getClass()))
                    method.invoke(module, event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface MController{}

}