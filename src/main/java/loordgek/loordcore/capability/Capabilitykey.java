package loordgek.loordcore.capability;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import java.util.List;

public class CapabilityKey {
    private final List<EnumFacing> enumFacings;
    private final Capability<?> capability;

    public CapabilityKey(List<EnumFacing> enumFacings, Capability<?> capability) {
        this.enumFacings = enumFacings;
        this.capability = capability;
    }

    public boolean hasFacing(EnumFacing facing){
        return enumFacings.contains(facing);
    }
}
