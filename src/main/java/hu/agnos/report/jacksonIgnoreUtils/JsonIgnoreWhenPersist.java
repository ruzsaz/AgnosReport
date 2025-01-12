package hu.agnos.report.jacksonIgnoreUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Annotation to ignore field when saving data to the disk as xml
 */
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonIgnore
public @interface JsonIgnoreWhenPersist {
}