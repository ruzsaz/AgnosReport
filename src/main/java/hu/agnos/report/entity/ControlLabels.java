package hu.agnos.report.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ControlLabels {

    @JacksonXmlProperty(isAttribute = true)
    private String lang;

    @JacksonXmlProperty(isAttribute = true)
    private String caption;

    @JacksonXmlProperty(isAttribute = true)
    private String description;

    @JacksonXmlProperty(isAttribute = true)
    private String values;

    public ControlLabels(String lang) {
        this.lang = lang;
        this.caption = "";
        this.description = "";
        this.values = "";
    }

}
