package loordgek.loordcore.wrappers;

import loordgek.loordcore.util.fluid.IFluidTankHandler;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class IFluidHandlerWrapper implements IFluidTankHandler {
    private final IFluidHandler fluidHandler;

    public IFluidHandlerWrapper(IFluidHandler fluidHandler) {
        this.fluidHandler = fluidHandler;
    }

    @Nullable
    @Override
    public FluidStack getContents() {
        return fluidHandler.getTankProperties()[0].getContents();
    }

    @Override
    public int getCapacity() {
        return fluidHandler.getTankProperties()[0].getCapacity();
    }

    @Override
    public boolean canFill() {
        return fluidHandler.getTankProperties()[0].canFill();
    }

    @Override
    public boolean canDrain() {
        return fluidHandler.getTankProperties()[0].canDrain();
    }

    @Override
    public boolean canFillFluidType(FluidStack fluidStack) {
        return fluidHandler.getTankProperties()[0].canFillFluidType(fluidStack);
    }

    @Override
    public boolean canDrainFluidType(FluidStack fluidStack) {
        return fluidHandler.getTankProperties()[0].canDrainFluidType(fluidStack);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return fluidHandler.fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return fluidHandler.drain( resource ,doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return fluidHandler.drain(maxDrain ,doDrain);
    }
}
