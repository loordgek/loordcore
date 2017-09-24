package loordgek.loordcore.modulecontroller;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.reflect.ClassPath;
import loordgek.loordcore.event.EventBlockPreRegistar;
import loordgek.loordcore.event.EventItemPreRegistar;
import loordgek.loordcore.event.ModuleInitEvent;
import loordgek.loordcore.ref.Reference;
import loordgek.loordcore.util.LogHelper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

public class ModuleManager {
    private final Map<String, Object> modules;
    private final Configuration moduleconfigmaster;
    private final String modid;

    public ModuleManager(String modid) {
        this.modid = modid;
        modules = Maps.newHashMap();
        File modulefolder = new File(Reference.MCINFO.MCDIR + "/config/" + modid + "/modules");
        modulefolder.mkdirs();
        moduleconfigmaster = new Configuration(new File(Reference.MCINFO.MCDIR + "/config/" + modid + "/modules/modulemanager.cfg"));
        moduleconfigmaster.load();
    }

    private boolean isModuleEnebled(String modulename){
        boolean boolprop = moduleconfigmaster.get("modules", modulename, true).getBoolean();
        if (moduleconfigmaster.hasChanged()) {
            moduleconfigmaster.save();
        }
        return boolprop;

    }

    public void setupModules() {
        try {
            for (ClassPath.ClassInfo info : ClassPath.from(ModuleManager.class.getClassLoader()).getTopLevelClassesRecursive(ModuleManager.class.getPackage().getName()))
                if (info.getSimpleName().startsWith("Compat") && !info.getSimpleName().startsWith("CompatModule"))
                    registerModule(info);
        } catch (IOException e) {
            throw new RuntimeException("Unknown error while searching for modules!", e);
        }
    }

    private void registerModule(ClassPath.ClassInfo moduleClassInfo) {
        try {
            String id;
            Class<?> moduleClass = moduleClassInfo.load();
            LCModule LCModule = moduleClass.getAnnotation(LCModule.class);
            id = LCModule.id();
            if (isModuleEnebled(id) && areRequiredModsLoaded(LCModule.requiredMods(), id)) {
                LogHelper.info(String.format("CompatModule '%s' activated.", id));
                modules.put(id, moduleClass.newInstance());
            }

        } catch (Throwable t) {
            LogHelper.error(String.format("CompatModule '%s' failed to load: '%s'!", moduleClassInfo.getName(), t.getMessage()));
        }

    }

    private boolean areRequiredModsLoaded(String requiredMods, String modulename){
        if (!Strings.isNullOrEmpty(requiredMods)) {
            for (String mod : requiredMods.split(",")) {
                if (!Loader.isModLoaded(mod)) {
                    LogHelper.info(String.format("CompatModule '%s' is missing a dependency: '%s'! This LCModule will not be loaded.", modulename, mod));
                    return false;
                }
            }
        }
        return true;

    }
    public void ModuleInit(){
        for (Object modules : modules.values()){
                invokeHandlers(modules, new ModuleInitEvent(new File(Reference.MCINFO.MCDIR + "/config/" + modid + "/modules/moduleconfig")));
        }
    }

    @ModuleController.MController
    public void onBlockPreRegistar(EventBlockPreRegistar event) {
        for (Object modules : modules.values()){
            invokeHandlers(modules, event);
        }
    }

    @ModuleController.MController
    public void onItemPreRegistar(EventItemPreRegistar event) {
        for (Object modules : modules.values()){
            invokeHandlers(modules, event);
        }
    }

    @ModuleController.MController
    public void onFMLPreInitialization(FMLPreInitializationEvent event) {
        for (Object modules : modules.values()){
            invokeHandlers(modules, event);
        }
    }

    @ModuleController.MController
    public void onFMLInitialization(FMLInitializationEvent event) {
        for (Object modules : modules.values()){
            invokeHandlers(modules, event);
        }
    }
    @ModuleController.MController
    public void onFMLPostInitialization(FMLPostInitializationEvent event) {
        for (Object modules : modules.values()){
            invokeHandlers(modules, event);
        }
    }
    private void invokeHandlers(Object module, Object event) {
        for (Method method : module.getClass().getDeclaredMethods()) {
            try {
                if (method.getAnnotation(LCModule.Handler.class) == null)
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
}
