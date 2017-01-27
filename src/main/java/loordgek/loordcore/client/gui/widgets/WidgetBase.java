package loordgek.loordcore.client.gui.widgets;

import loordgek.extragenarators.client.gui.GuiExtragenarators;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.Rectangle;

import java.util.List;

@SideOnly(Side.CLIENT)
public class WidgetBase{

    public final int id;
    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public final GuiExtragenarators gui;
    public boolean enabled = true;
    public boolean playSound = true;

    public WidgetBase(int id, int x, int y, int width, int height, GuiExtragenarators gui) {
        this.id = id;
        this.x = x + gui.getguileft();
        this.y = y + gui.getguitop();
        this.width = width;
        this.height = height;
        this.gui = gui;}

    public void render(int mouseX, int mouseY) {}

    public void onWidgetClicked(int x, int y, int button, EntityPlayer player) {}

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void addTooltip(int mouseX, int mouseY, List<String> tooltips, boolean shift, EntityPlayer player) {}
}