package hu.agnos.report.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class Kpi {

    @JacksonXmlProperty(isAttribute = true)
    private int indicatorIndex = -1;

    @JacksonXmlProperty(isAttribute = true, localName = "isRatio")
    private boolean isRatio;

    @JacksonXmlProperty(localName = "Labels")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<KpiLabels> labels = new ArrayList<>(2);

    public Kpi(List<KpiLabels> kpiLabels) {
        this.isRatio = false;
        this.labels = new ArrayList<>(kpiLabels.size());
        for (KpiLabels reportLabel : kpiLabels) {
            labels.add(new KpiLabels(reportLabel.getLang()));
        }
    }

    public void addLanguage(String lang) {
        labels.add(new KpiLabels(lang));
    }

    public void removeLanguage(int index) {
        labels.remove(index);
    }

}
