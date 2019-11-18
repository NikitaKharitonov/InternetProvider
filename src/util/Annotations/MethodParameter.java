package util.Annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * Annotation MethodParameter
 * Should be used in constructor to ease reflection
 * @author anteii
 * @version 0.1
 * */
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodParameter {
    String name();
    Class type();
}
