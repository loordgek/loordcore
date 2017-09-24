package loordgek.loordcore.capability;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityMap implements ICapabilityProvider{
    private class Node{
        private final Capabilitykey capabilitykey;
        private final Object object;
        private final ResourceLocation resourceLocation;

        private Node(Capabilitykey capabilitykey, Object object, ResourceLocation resourceLocation) {
            this.capabilitykey = capabilitykey;
            this.object = object;
            this.resourceLocation = resourceLocation;
        }

        public Capabilitykey getCapabilitykey() {
            return capabilitykey;
        }

        public Object getObject() {
            return object;
        }

        public ResourceLocation getResourceLocation() {
            return resourceLocation;
        }

        public Object getObject(Capability<?> capability, EnumFacing facing){
            if (capabilitykey.getCapability() == capability && capabilitykey.hasFacing(facing)) return object;
            return null;
        }
    }
    private Node[] nodes;

    public void addCapability(Capability<?> capability, Object object, ResourceLocation resourceLocation, EnumFacing... enumFacings){

    }

    public void removeCapability(Object object){

    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return false;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return null;
    }


}
