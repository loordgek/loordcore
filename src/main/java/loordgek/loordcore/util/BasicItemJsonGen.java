package loordgek.loordcore.util;

import com.google.gson.stream.JsonWriter;
import loordgek.loordcore.Register.IVariantLookup;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BasicItemJsonGen {

    public static void genItemJson(String path, String modid, String fileName, IVariantLookup lookup) throws IOException {
        File folder = new File(path + "/assets/" + modid + "/models/item/");
        File json = new File(path + "/assets/" + modid + "/models/item/" + fileName + ".json");
        folder.mkdirs();

        if (!json.exists()) {
            json.createNewFile();
        }


        JsonWriter jsonWriter = new JsonWriter(new FileWriter(json));
        jsonWriter.setIndent("    ");
        jsonWriter.setLenient(true);

        jsonWriter.beginObject();
        jsonWriter.name("parent").value("item/generated");
        jsonWriter.name("variants").beginObject();
        for (String string : lookup.variantnames()){
            jsonWriter.name(string).beginObject();
            jsonWriter.name("textures").beginObject();
            jsonWriter.name("layer0").value(modid + ":items/");
            jsonWriter.endObject();
            jsonWriter.endObject();
        }
        jsonWriter.endObject();
        jsonWriter.endObject();
        jsonWriter.close();
    }
}
