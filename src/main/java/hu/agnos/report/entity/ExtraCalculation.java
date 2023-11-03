package hu.agnos.report.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

/**
 * @author parisek
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExtraCalculation {

    @JacksonXmlProperty(isAttribute = true)
    private String function;

    @JacksonXmlProperty(isAttribute = true)
    private String args;

}
