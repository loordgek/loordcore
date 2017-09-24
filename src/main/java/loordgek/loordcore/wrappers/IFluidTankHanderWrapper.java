package loordgek.loordcore.wrappers;

import loordgek.loordcore.util.fluid.IFluidTankHandler;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class IFluidTankHanderWrapper implements IFluidHandler {
    private final IFluidTankHandler fluidTankHandler;

    public IFluidTankHanderWrapper(IFluidTankHandler fluidTankHandler) {
        this.fluidTankHandler = fluidTankHandler;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new IFluidTankProperties[]{
                new IFluidTankProperties() {
                    @Nullable
                    @Override
                    public FluidStack getContents() {
                        return fluidTankHandler.getContents();
                    }

                    @Override
                    public int getCapacity() {
                        return fluidTankHandler.getCapacity();
                    }

                    @Override
                    public boolean canFill() {
                        return fluidTankHandler.canFill();
                    }

                    @Override
                    public boolean canDrain() {
                        return fluidTankHandler.canDrain();
                    }

                    @Override
                    public boolean canFillFluidType(FluidStack fluidStack) {
                        return fluidTankHandler.canFillFluidType(fluidStack);
                    }

                    @Override
                    public boolean canDrainFluidType(FluidStack fluidStack) {
                        return fluidTankHandler.canDrainFluidType(fluidStack);
                    }
                }
        };
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return fluidTankHandler.fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return fluidTankHandler.drain(resource, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return fluidTankHandler.drain(maxDrain, doDrain);
    }
}
