package hu.agnos.report.jacksonIgnoreUtils;

import java.lang.annotation.Annotation;

import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlAnnotationIntrospector;

public class DisableIgnoreWhenSendInspector extends JacksonXmlAnnotationIntrospector {

    @Override
    public boolean isAnnotationBundle(final Annotation ann) {
        if (ann.annotationType().equals(JsonIgnoreWhenSend.class)) {
            return false;
        } else {
            return super.isAnnotationBundle(ann);
        }
    }
}
