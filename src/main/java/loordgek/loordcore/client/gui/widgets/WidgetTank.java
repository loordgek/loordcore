package loordgek.loordcore.client.gui.widgets;

import loordgek.extragenarators.client.RenderUtils;
import loordgek.extragenarators.client.gui.GuiExtragenarators;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class WidgetTank extends WidgetBase {
    private final IFluidTank tank;
    public WidgetTank(int id, int x, int y, int width, int height, GuiExtragenarators gui, IFluidTank tank) {
        super(id, x, y, width, height, gui);
        this.tank = tank;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (tank.getFluidAmount() > 0)
            RenderUtils.renderGuiTank(tank, x, y, width, height);
    }

    @Override
    public void addTooltip(int mouseX, int mouseY, List<String> tooltips, boolean shift, EntityPlayer player) {
        if (tank.getFluid() != null && tank.getFluid().getFluid() != null) {
            EnumRarity r = tank.getFluid().getFluid().getRarity(tank.getFluid());
            tooltips.add((r != null && r.rarityColor != null ? r.rarityColor : EnumRarity.COMMON.rarityColor) + tank.getFluid().getLocalizedName());
        }
        tooltips.add(I18n.format("fluids.info", tank.getFluidAmount(), tank.getCapacity()));
    }
}

