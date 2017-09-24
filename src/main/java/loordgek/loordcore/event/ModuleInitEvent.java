package loordgek.loordcore.module;

import net.minecraftforge.fml.common.eventhandler.Event;

import java.io.File;

public class ModuleInitEvent extends Event {
    private final File mofuledir;

    public ModuleInitEvent(File mofuledir) {
        this.mofuledir = mofuledir;
    }

    public File getMofuledir() {
        return mofuledir;
    }
}
