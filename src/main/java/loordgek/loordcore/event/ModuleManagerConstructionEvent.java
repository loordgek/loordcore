package loordgek.loordcore.module;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ModuleManagerConstructionEvent extends Event {

    private final IModuleContoller contoller;

    public ModuleManagerConstructionEvent(IModuleContoller contoller) {

        this.contoller = contoller;
    }

    public IModuleContoller getContoller() {
        return contoller;
    }
}
