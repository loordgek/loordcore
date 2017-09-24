package loordgek.loordcore.util;


import loordgek.loordcore.ref.Reference;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.util.List;

public class LogHelper {
    public static void log(Level logLevel, Object object){
        FMLLog.log(Reference.MODINFO.MOD_ID, logLevel, String.valueOf(object));
    }

    public static void all(Object object){
        log(Level.ALL, object);
    }

    public static void debug(Object object){
        log(Level.DEBUG, object);
    }

    public static void error(Object object){
        log(Level.ERROR, object);
    }

    public static void fatal(Object object){
        log(Level.FATAL, object);
    }

    public static void info(Object object){
        log(Level.INFO, object);
    }

    public static void off(Object object){
        log(Level.OFF, object);
    }

    public static void trace(Object object){
        log(Level.TRACE, object);
    }

    public static void warn(Object object){
        log(Level.WARN, object);
    }
    public static void logItemstack(ItemStack stack){
        info(stack.getItem().getUnlocalizedName() + " stackSize " + stack.getCount() + " Metadata " + stack.getMetadata() + " NBT " + stack.getTagCompound());
    }
    public static void logEnergyStorage(IEnergyStorage energyStorage){
        info("EnergyStored " + energyStorage.getEnergyStored() + " MaxEnergyStored " + energyStorage.getMaxEnergyStored());
    }
    public static void LogList(List<?> list){
        for (Object o: list) {
            LogHelper.info(o);
        }
    }

}
