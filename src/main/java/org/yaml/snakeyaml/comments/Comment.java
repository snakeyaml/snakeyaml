package org.yaml.snakeyaml.comments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Comment {
    /** The text of the comment to insert. */
    String value();

    /** Should a space be inserted between the # and the start of the comment? */
    boolean prefixSpace() default true;
}
