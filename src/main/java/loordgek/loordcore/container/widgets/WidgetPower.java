package loordgek.loordcore.client.gui.widgets;


import loordgek.loordcore.client.RenderUtils;
import loordgek.loordcore.client.gui.GuiContainerMain;
import loordgek.loordcore.util.MathUtil;
import loordgek.loordcore.util.PowerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class WidgetPower extends WidgetBase {
    private final IEnergyStorage forgePower;
    private static final ResourceLocation powerbarback = new WidgetResourceLocation("powerbarback");
    private static final ResourceLocation powerbar = new WidgetResourceLocation("powerbar");

    public WidgetPower(int id, int x, int y, int width, int height, GuiContainerMain gui, IEnergyStorage energyStorage) {
        super(id, x, y, width, height, gui);
        this.forgePower = energyStorage;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        RenderUtils.drawWidgetStatic(this, powerbarback);
        RenderUtils.drawWidgetUp(this, powerbar, (int) MathUtil.getpercentagereverse(forgePower.getEnergyStored() , forgePower.getMaxEnergyStored()), 100);
    }

    @Override
    public void addTooltip(int mouseX, int mouseY, List<String> tooltips, boolean shift, EntityPlayer player) {
        List<String> stringList = new ArrayList<String>();
        if (shift){
            stringList.add(Integer.toString(forgePower.getEnergyStored())+ "/");
            stringList.add(Integer.toString(forgePower.getMaxEnergyStored()));
        }
        else stringList.add(Double.toString(PowerUtil.getpercentageEnergyfilled(forgePower)) + " &");
        tooltips.addAll(stringList);

    }
}
