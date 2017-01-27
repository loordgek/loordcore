package loordgek.loordcore.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/*
*    PneumaticCraft code. author = MineMaarten
*    https://github.com/MineMaarten/PneumaticCraft
*/

/**
 * Fields marked with this and also @link @DescSync will be included in a desc packet. However, changes to this field won't cause a desc
 * packet to be sent.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LazySync {
}
