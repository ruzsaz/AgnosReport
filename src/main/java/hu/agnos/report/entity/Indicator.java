package hu.agnos.report.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
public class Indicator {

    @JacksonXmlProperty(isAttribute = true)
    private String denominatorName;

    @JacksonXmlProperty(isAttribute = true)
    private String denominatorCubeName;

    @JacksonXmlProperty(isAttribute = true)
    private double denominatorMultiplier;

    @JacksonXmlProperty(isAttribute = true)
    private String denominatorSign;

    @JacksonXmlProperty(isAttribute = true)
    private boolean denominatorIsHidden;

    @JacksonXmlProperty(isAttribute = true)
    private String valueName;

    @JacksonXmlProperty(isAttribute = true)
    private String valueCubeName;

    @JacksonXmlProperty(isAttribute = true)
    private boolean valueIsHidden;

    @JacksonXmlProperty(isAttribute = true)
    private double valueMultiplier;

    @JacksonXmlProperty(isAttribute = true)
    private String valueSign;

    @JacksonXmlProperty(isAttribute = true)
    private String preferredColor;

    @JacksonXmlProperty(isAttribute = true, localName = "isColorExact")
    private boolean isColorExact;

    @JacksonXmlProperty(localName = "Labels")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<IndicatorLabels> multilingualization;

    public Indicator(List<ReportLabels> reportLabels) {
        this.multilingualization = new ArrayList<>(reportLabels.size());
        for (ReportLabels reportLabel : reportLabels) {
            multilingualization.add(new IndicatorLabels(reportLabel.getLang()));
        }
        this.valueSign = "0";
        this.valueMultiplier = 1.0;
        this.denominatorSign = "0";
        this.denominatorMultiplier = 1.0;
    }

    public void addLanguage(String lang) {
        multilingualization.add(new IndicatorLabels(lang));
    }

    public void removeLanguage(int index) {
        multilingualization.remove(index);
    }

    @JsonIgnore
    public String getCombinedValueName() {
        return (valueCubeName == null || valueCubeName.isEmpty()) ? valueName : valueCubeName + "." + valueName;
    }

    public void setCombinedValueName(String cubeAndValueName) {
        if (cubeAndValueName != null && cubeAndValueName.contains(".")) {
            String[] cubeAndValue = cubeAndValueName.split("\\.");
            this.valueCubeName = cubeAndValue[0];
            this.valueName = cubeAndValue[1];
        } else if (Objects.equals(cubeAndValueName, "1")) {
            this.valueCubeName = "";
            this.valueName = "1";
        }
    }

    @JsonIgnore
    public String getCombinedDenominatorName() {
        return (denominatorCubeName == null || denominatorCubeName.isEmpty()) ? denominatorName : denominatorCubeName + "." + denominatorName;
    }

    public void setCombinedDenominatorName(String cubeAndValueName) {
        if (cubeAndValueName != null && cubeAndValueName.contains(".")) {
            String[] cubeAndValue = cubeAndValueName.split("\\.");
            this.denominatorCubeName = cubeAndValue[0];
            this.denominatorName = cubeAndValue[1];
        } else if (Objects.equals(cubeAndValueName, "1")) {
            this.denominatorCubeName = "";
            this.denominatorName = "1";
        }
    }

}
