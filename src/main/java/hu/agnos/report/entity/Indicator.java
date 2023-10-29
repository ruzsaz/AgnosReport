package hu.agnos.report.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.ArrayList;
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

    @JacksonXmlProperty(localName = "ExtraCalculation")
    private ExtraCalculation extraCalculation;

    @JacksonXmlProperty(localName = "Labels")
    @JacksonXmlElementWrapper(useWrapping = false)
    private ArrayList<IndicatorLabels> multilingualization;

    public Indicator(int languageNumber) {
        this.multilingualization = new ArrayList<>();
        for (int i = 0; i < languageNumber; i++) {
            this.multilingualization.add(new IndicatorLabels());
        }
        this.extraCalculation = new ExtraCalculation();
    }

    public Indicator(int languageNumber, Double multiplier) {
        this(languageNumber);
        this.denominatorMultiplier = multiplier;
    }

    public Indicator(int languageNumber, String denominatorName, String denominatorCubeName, double multiplier, String denominatorSign, boolean denominatorIsHidden, String valueName, String valueCubeName, boolean valueIsHidden, String valueSign) {
        this(languageNumber, multiplier);
        this.denominatorName = denominatorName;
        this.denominatorCubeName = denominatorCubeName;
        this.denominatorSign = denominatorSign;
        this.denominatorIsHidden = denominatorIsHidden;
        this.valueName = valueName;
        this.valueCubeName = valueCubeName;
        this.valueIsHidden = valueIsHidden;
        this.valueSign = valueSign;
    }

    public boolean hasExtraCalculation() {
        return !(this.extraCalculation == null || "".equals(this.extraCalculation.getFunction()));
    }

    public void addLanguage(String lang) {
        this.multilingualization.add(new IndicatorLabels(lang));
    }

    public void removeLanguage(int index) {
        this.multilingualization.remove(index);
    }

    public Indicator deepCopy() {
        Indicator result = new Indicator(this.multilingualization.size(), denominatorName, denominatorCubeName, denominatorMultiplier, denominatorSign, denominatorIsHidden, valueName, valueCubeName, valueIsHidden, valueSign);
        result.setMultilingualization(new ArrayList<>(multilingualization));
        result.setExtraCalculation(extraCalculation);
        return result;
    }

    
    
}
