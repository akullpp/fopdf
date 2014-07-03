package de.akull.pdf;

import generated.Employee;
import generated.ObjectFactory;
import generated.Report;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


public class Main {

    public static void main(String[] args) {
        try {
            ObjectFactory factory = new ObjectFactory();
            Report report = factory.createReport();
            Report.Header header = factory.createReportHeader();
            Report.Content content = factory.createReportContent();

            // Create and fill author
            Employee author = new Employee();

            author.setFullname("Max Mustermann");
            author.setMailaddress("mustermann@mustermail.de");
            header.setAuthor(author);

            // Create and fill creationdate
            GregorianCalendar calendar = new GregorianCalendar();
            XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);

            header.setCreationdate(xmlCalendar);
            header.setTitle("Report");
            content.setText("Hello world.");
            report.setHeader(header);
            report.setContent(content);

            Builder<Report> rb = new Builder(Report.class);

            rb.toXML(report);

            System.out.println(rb.getXml());

            rb.toFO(new FileInputStream("./src/main/resources/report.xsl"));

            System.out.println(rb.getFo());

            rb.toPDF(new FileOutputStream("report.pdf"));
        }
        catch (DatatypeConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
