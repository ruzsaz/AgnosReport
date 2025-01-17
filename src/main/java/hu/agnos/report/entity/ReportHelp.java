package hu.agnos.report.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
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
public class ReportHelp {

    @JacksonXmlProperty(isAttribute = true)
    private String lang;

    @JacksonXmlCData
    @JacksonXmlText
    private String message;

    public ReportHelp(String lang) {
        this.lang = lang;
    }

}
