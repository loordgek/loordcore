package loordgek.loordcore.testmod;

import net.minecraft.util.IStringSerializable;

public enum MultiEnum implements IStringSerializable {
    MULTIMASTER("master"),
    MULTISLAVE("slave");


    private final String name;

    MultiEnum(String name) {
        this.name = name;
    }

    public static final MultiEnum[] enumarray = new MultiEnum[values().length];

    @Override
    public String getName() {
        return name;
    }
}
