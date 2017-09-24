package loordgek.loordcore.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JavaUtil {
    public static final Random random = new Random();

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

}
