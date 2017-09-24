package loordgek.loordcore.util;

import net.minecraft.util.ResourceLocation;

public enum EnumFlow {
    NONE(null),
    PUCH(null),
    PULL(null);


    public final ResourceLocation texture;

    private static EnumFlow[] flows = values();

    EnumFlow(ResourceLocation texture) {
        this.texture = texture;
    }
    public EnumFlow nextFlow(){
        return flows[(this.ordinal() + 1) % flows.length];
    }
}
