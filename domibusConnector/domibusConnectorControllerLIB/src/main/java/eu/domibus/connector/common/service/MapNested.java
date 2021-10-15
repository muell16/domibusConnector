package eu.domibus.connector.common.service;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;

//@Target({ElementType.FIELD, ElementType.TYPE_PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MapNested {
}
