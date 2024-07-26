package clov3r.oneit_server.config.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Auth {
    boolean required() default true;
}
