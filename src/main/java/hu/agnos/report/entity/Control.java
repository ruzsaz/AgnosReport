package hu.agnos.report.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Control {

    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlProperty(isAttribute = true)
    private String parameters;

    @JacksonXmlProperty(isAttribute = true)
    private String defaultValue;

    @JacksonXmlProperty(localName = "Labels")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ControlLabels> multilingualization;

    public Control(List<ReportLabels> reportLabels) {
        this.type = "";
        this.parameters = "";
        this.defaultValue = "";
        this.multilingualization = new ArrayList<>(reportLabels.size());
        for (ReportLabels reportLabel : reportLabels) {
            multilingualization.add(new ControlLabels(reportLabel.getLang()));
        }
    }

    public void addLanguage(String lang) {
        multilingualization.add(new ControlLabels(lang));
    }

    public void removeLanguage(int index) {
        multilingualization.remove(index);
    }

}
