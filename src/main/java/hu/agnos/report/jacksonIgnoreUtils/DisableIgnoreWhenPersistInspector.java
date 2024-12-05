package hu.agnos.report.jacksonIgnoreUtils;

import java.lang.annotation.Annotation;

import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class DisableIgnoreWhenPersistInspector extends JacksonAnnotationIntrospector {

    @Override
    public boolean isAnnotationBundle(final Annotation ann) {
        if (ann.annotationType().equals(JsonIgnoreWhenPersist.class)) {
            return false;
        } else {
            return super.isAnnotationBundle(ann);
        }
    }
}
