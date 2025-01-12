package hu.agnos.report.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Dimension {

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlProperty(isAttribute = true)
    private int allowedDepth;

    @JacksonXmlProperty(localName = "Labels")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<DimensionLabels> multilingualization;

    @JacksonXmlProperty(localName = "TransparentInCube")
    @JacksonXmlElementWrapper(useWrapping = false)
    private Set<String> transparentInCubes = new HashSet<>(1);

    public Dimension(List<ReportLabels> reportLabels) {
        this.name = "";
        this.type = "";
        this.transparentInCubes = new HashSet<>(1);
        this.multilingualization = new ArrayList<>(reportLabels.size());
        for (ReportLabels reportLabel : reportLabels) {
            multilingualization.add(new DimensionLabels(reportLabel.getLang()));
        }
    }

    public void addLanguage(String lang) {
        multilingualization.add(new DimensionLabels(lang));
    }

    public void removeLanguage(int index) {
        multilingualization.remove(index);
    }

    public void addTransparentCubeName(String cubeName) {
        transparentInCubes.add(cubeName);
    }

    public void removeTransparentCubeName(String cubeName) {
        transparentInCubes.remove(cubeName);
    }

}
