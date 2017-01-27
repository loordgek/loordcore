package loordgek.loordcore.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JavaUtil {
    public static List<Field> GetFields(Object te, Class searchedAnnotation) {
        List<Field> fieldList = new ArrayList<Field>();
        Class examinedClass = te.getClass();
        while (examinedClass != null) {
            for (Field field : examinedClass.getDeclaredFields()) {
                if (field.getAnnotation(searchedAnnotation) != null) {
                    fieldList.add(field);
                }
            }
            examinedClass = examinedClass.getSuperclass();
        }
        return fieldList;
    }

    @Nullable
    public static String getsourcepath(@Nonnull Class<?> clazz, int numberstogoupadirectory) {
        URL fileUrl = clazz.getProtectionDomain().getCodeSource().getLocation();
        File file = null;
        try {
            file = new File(fileUrl.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < numberstogoupadirectory; i++) {
            file = file != null ? file.getParentFile() : null;
        }
        return file != null ? file.getParentFile().getParent() : null;
    }
}
