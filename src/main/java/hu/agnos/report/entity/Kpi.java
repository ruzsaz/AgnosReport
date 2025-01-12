package hu.agnos.report.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String baseLevel;

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

    @JsonIgnore
    public ArrayList<ArrayList<String>> getParsedBaseLevel() {
        if (baseLevel == null || baseLevel.isEmpty()) {
            return null;
        }
        String[] split = baseLevel.split("],");
        ArrayList<ArrayList<String>> result = new ArrayList<>(split.length);

        for (String s0: split) {
            String[] split1 = s0.split("',");
            ArrayList<String> partialResult = new ArrayList<>(split1.length);
            for (String s : split1) {
                String dimElement = s.replace("[", "")
                        .replace("]", "")
                        .replace("'", "")
                        .trim();
                if (!dimElement.isEmpty()) {
                    partialResult.add(dimElement);
                }
            }
            result.add(partialResult);
        }
        return result;
    }

}
