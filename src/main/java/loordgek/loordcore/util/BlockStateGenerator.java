package loordgek.loordcore.util;

import com.google.common.collect.ImmutableMap;
import com.google.gson.stream.JsonWriter;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BlockStateGenerator {
    /**
     * Generates a Blockstate JSON for the specified block using all of it's properties.
     *
     * @param path     Starting path ie "E://Modding/forge".
     * @param modid    The modid for this blocks mod, used mainly as an identifier.
     * @param fileName Pretty self explanatory.
     * @param block    The block that is relevant to the blockstate.
     */

    public static void BlocktateGeneratorforgeV1(String path, String modid, String fileName, Block block) throws IOException {
        File json = new File(path + "/assets/" + modid + "/blockstates/" + fileName + ".json");
        File folders = new File(path + "/assets/" + modid + "/blockstates/");

        folders.mkdirs();

        if (!json.exists()){
            json.createNewFile();
        }

        ImmutableMap<IProperty<?>, Comparable<?>> properties = block.getBlockState().getBaseState().getProperties();

        JsonWriter jsonWriter = new JsonWriter(new FileWriter(json));
        jsonWriter.setIndent("    ");
        jsonWriter.setLenient(false);
        jsonWriter.beginObject();
        jsonWriter.name("forge_marker").value(1);
        jsonWriter.name("defaults").beginObject().endObject();
        jsonWriter.name("variants").beginObject();
        jsonWriter.name("inventory").beginObject().endObject();
        for (IProperty<?> property : properties.keySet()) {
            WriteProperty(property, jsonWriter);
        }
        jsonWriter.endObject();
        jsonWriter.endObject();
        jsonWriter.close();
    }

    private static void WriteProperty(IProperty<?> property, JsonWriter jsonWriter) throws IOException {
        jsonWriter.name(property.getName());
        jsonWriter.beginObject();
        for (Object object : property.getAllowedValues().toArray()) {
            jsonWriter.name(object.toString()).beginObject().endObject();
        }
        jsonWriter.endObject();
    }
}