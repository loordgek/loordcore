package loordgek.loordcore.modulecontroller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LCModule {

    String id();

    String requiredMods() default "";

    Compat mode();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Handler {
    }

    enum Compat{
        MODULE,
        COMPAT
    }
}
