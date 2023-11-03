package hu.agnos.report.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

/**
 * @author parisek
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IndicatorLabels {

    @JacksonXmlProperty(isAttribute = true)
    private String caption;

    @JacksonXmlProperty(isAttribute = true)
    private String description;

    @JacksonXmlProperty(isAttribute = true)
    private String lang;

    @JacksonXmlProperty(isAttribute = true)
    private String valueUnit;

    @JacksonXmlProperty(isAttribute = true)
    private String valueUnitPlural;

    @JacksonXmlProperty(isAttribute = true)
    private String denominatorUnit;

    @JacksonXmlProperty(isAttribute = true)
    private String denominatorUnitPlural;

    public IndicatorLabels(String lang) {
        this.lang = lang;
    }

}
