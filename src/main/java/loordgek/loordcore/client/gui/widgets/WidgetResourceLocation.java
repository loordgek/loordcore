package loordgek.loordcore.client.gui.widgets;

import loordgek.extragenarators.ref.Reference;
import net.minecraft.util.ResourceLocation;

public class WidgetResourceLocation extends ResourceLocation {
    public WidgetResourceLocation(String widgetname) {
        super(Reference.MODINFO.MOD_ID, "textures/gui/widgets/" + widgetname + ".png");
    }
}