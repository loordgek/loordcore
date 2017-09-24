package loordgek.loordcore.client.gui.widgets;

import loordgek.loordcore.client.RenderUtils;
import loordgek.loordcore.client.gui.GuiContainerMain;
import loordgek.loordcore.util.IFire;
import loordgek.loordcore.util.MathUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WidgetFire extends WidgetBase {
    private static final ResourceLocation firepng = new WidgetResourceLocation("fire");
    private static final ResourceLocation firebackpng = new WidgetResourceLocation("fireback");
    private final IFire fire;
    public WidgetFire(int id, int x, int y, GuiContainerMain gui, IFire fire) {
        super(id, x, y, 14, 14, gui);
        this.fire = fire;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        RenderUtils.drawWidgetStatic(this, firebackpng);
        if (fire.FireCurrent() != 0){
            RenderUtils.drawWidgetUp(this, firepng, (int) MathUtil.reverseNumber(fire.FireCurrent(), 0, fire.FireMax()), fire.FireMax());
        }
    }

}