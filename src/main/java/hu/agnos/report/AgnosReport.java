/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.agnos.report;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import hu.agnos.report.entity.Report;
import hu.agnos.report.repository.ReportRepository;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 *
 * @author parisek
 */
public class AgnosReport {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ReportRepository rr = new ReportRepository();
        Optional<Report> optReport = rr.findById("NEW_REPORT_CRC_BNO_1.report.xml");
        if (optReport.isPresent()) {
//            System.out.println(optReport.get().toString());

            try {
                XmlMapper xmlMapper = new XmlMapper();
                xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
                xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
                String s = xmlMapper.writeValueAsString(optReport.get());
                
                System.out.println(s);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

        }

    }
}
