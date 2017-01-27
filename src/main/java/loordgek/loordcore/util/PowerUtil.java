package loordgek.loordcore.util;

import net.minecraftforge.energy.IEnergyStorage;

public class PowerUtil {
    public static int getpercentageEnergyfilled(IEnergyStorage energy){
        return energy.getEnergyStored() * 100 / energy.getMaxEnergyStored();
    }
    public static double getpercentageEnergyfilledreverse(IEnergyStorage energy){
        return MathUtil.getpercentagereverse(energy.getEnergyStored(), energy.getMaxEnergyStored());
    }
}
