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
public class Visualization {

    @JacksonXmlProperty(isAttribute = true)
    private String initString;

    public Visualization deepCopy() {
        return new Visualization(initString);
    }

}
