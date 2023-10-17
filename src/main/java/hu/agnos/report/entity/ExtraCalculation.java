/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.agnos.report.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author parisek
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExtraCalculation {

    @JacksonXmlProperty(isAttribute = true)
    private String function;

    @JacksonXmlProperty(isAttribute = true)
    private String args;
}
