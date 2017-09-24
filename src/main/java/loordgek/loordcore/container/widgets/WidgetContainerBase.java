package loordgek.loordcore.client.gui.widgets;

import loordgek.loordcore.client.gui.GuiContainerMain;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WidgetContainerBase extends WidgetBase {
    protected List<WidgetBase> widgetBaseList = new ArrayList<>();



    public WidgetContainerBase(int id, int x, int y, int width, int height, GuiContainerMain gui) {
        super(id, x, y, width, height, gui);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        for (WidgetBase widgetBase : widgetBaseList) {
            widgetBase.render(mouseX, mouseY);

        }
        List<String> tooltips = new ArrayList<>();

        for (WidgetBase widget : widgetBaseList)
            if (widget.getBounds().contains(mouseX, mouseY))
                widget.addTooltip(mouseX, mouseY, tooltips, GuiScreen.isShiftKeyDown(), gui.getPlayer());

        if (!tooltips.isEmpty()) {
            List<String> finalLines = new ArrayList<>();
            for (String line : tooltips) {
                String[] lines = WordUtils.wrap(line, 30).split(System.getProperty("line.separator"));
                Collections.addAll(finalLines, lines);
            }
            gui.drawHoveringTextWidget(finalLines, mouseX, mouseY, gui.getFontRenderer());
        }
    }

    @Override
    public void onWidgetClicked(int x, int y, int button, EntityPlayer player) {
        for (WidgetBase widgetBase : widgetBaseList) {
            if (widgetBase.getBounds().contains(x, y) && widgetBase.enabled)
                widgetBase.onWidgetClicked(x, y, button, player);
        }
    }

    public void addWidget(WidgetBase widget) {
        widgetBaseList.add(widget);
    }
}

