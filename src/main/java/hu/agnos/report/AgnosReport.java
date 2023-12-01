package hu.agnos.report;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import hu.agnos.report.entity.Report;
import hu.agnos.report.repository.ReportRepository;
import org.springframework.data.repository.CrudRepository;

import java.io.IOException;
import java.util.Optional;

public final class AgnosReport {

    private AgnosReport() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CrudRepository<Report, String> reportRepository = new ReportRepository();
        Optional<Report> optReport = reportRepository.findById("NEW_REPORT_CRC_BNO_1.report.xml");
        if (optReport.isPresent()) {

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
