/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.agnos.report.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
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
public class ReportLabels {

    @JacksonXmlProperty(isAttribute = true)
    private String lang;
    
    @JacksonXmlProperty(isAttribute = true)
    private String caption;

    @JacksonXmlProperty(isAttribute = true)
    private String datasource;

    @JacksonXmlProperty(isAttribute = true)
    private String description;

    public ReportLabels(String lang) {
        this.lang = lang;
    }

   
    
}
