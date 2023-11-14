package hu.agnos.report.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author parisek
 */
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
    private String valueSign;

    @JacksonXmlProperty(localName = "Labels")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<IndicatorLabels> multilingualization;

    public Indicator(List<ReportLabels> reportLabels) {
        this.multilingualization = new ArrayList<>(reportLabels.size());
        for (ReportLabels reportLabel : reportLabels) {
            multilingualization.add(new IndicatorLabels(reportLabel.getLang()));
        }
        this.valueSign = "0";
        this.denominatorSign = "0";
        this.denominatorMultiplier = 1.0;
    }

//    public Indicator(int languageNumber, String denominatorName, String denominatorCubeName, double multiplier, String denominatorSign, boolean denominatorIsHidden, String valueName, String valueCubeName, boolean valueIsHidden, String valueSign) {
//        this(languageNumber, multiplier);
//        this.denominatorName = denominatorName;
//        this.denominatorCubeName = denominatorCubeName;
//        this.denominatorSign = denominatorSign;
//        this.denominatorIsHidden = denominatorIsHidden;
//        this.valueName = valueName;
//        this.valueCubeName = valueCubeName;
//        this.valueIsHidden = valueIsHidden;
//        this.valueSign = valueSign;
//    }

    public void addLanguage(String lang) {
        multilingualization.add(new IndicatorLabels(lang));
    }

    public void removeLanguage(int index) {
        multilingualization.remove(index);
    }

//    public Indicator deepCopy() {
//        Indicator result = new Indicator(multilingualization.size(), denominatorName, denominatorCubeName, denominatorMultiplier, denominatorSign, denominatorIsHidden, valueName, valueCubeName, valueIsHidden, valueSign);
//        result.setMultilingualization(new ArrayList<>(multilingualization));
//        return result;
//    }

    @JsonIgnore
    public String getCombinedValueName() {
        return valueCubeName + "." + valueName;
    }

    public void setCombinedValueName(String cubeAndValueName) {
        if (cubeAndValueName != null && cubeAndValueName.contains(".")) {
            String[] cubeAndValue = cubeAndValueName.split("\\.");
            this.valueCubeName = cubeAndValue[0];
            this.valueName = cubeAndValue[1];
        }
    }

    @JsonIgnore
    public String getCombinedDenominatorName() {
        return denominatorCubeName + "." + denominatorName;
    }

    public void setCombinedDenominatorName(String cubeAndValueName) {
        if (cubeAndValueName != null && cubeAndValueName.contains(".")) {
            String[] cubeAndValue = cubeAndValueName.split("\\.");
            this.denominatorCubeName = cubeAndValue[0];
            this.denominatorName = cubeAndValue[1];
        }
    }

}
