package de.akull.fopdf;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;


public class Builder<T> {
    private static final Logger LOGGER = Logger.getLogger(Builder.class.getName());
    private final Class<T> type;
    private String xml;
    private String fo;

    public Builder(Class<T> type) {
        this.type = type;
    }

    public void toXML(T t) {
        StringWriter writer = new StringWriter();

        try {
            JAXBContext context = JAXBContext.newInstance(type);
            Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(t, writer);
        }
        catch (JAXBException jaxbe) {
            LOGGER.log(Level.SEVERE, "{0}", new Object[]{jaxbe.getMessage()});
        }
        xml = writer.toString();
    }

    public void toFO(InputStream xsl) {
        StringWriter writer = new StringWriter();

        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsl));

            Source src = new StreamSource(new StringReader(xml));
            Result res = new StreamResult(writer);

            transformer.transform(src, res);
        }
        catch (TransformerConfigurationException tce) {
            LOGGER.log(Level.SEVERE, "{0}", new Object[]{tce.getMessage()});
        }
        catch (TransformerException te) {
            LOGGER.log(Level.SEVERE, "{0}", new Object[]{te.getMessage()});
        }
        fo = writer.toString();
    }

    public void toPDF(OutputStream os) {
        try {
            FOUserAgent userAgent = FopFactory.newInstance().newFOUserAgent();
            Fop fop = FopFactory.newInstance().newFop(MimeConstants.MIME_PDF, userAgent, os);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();

            Source src = new StreamSource(new StringReader(fo));
            Result res = new SAXResult(fop.getDefaultHandler());

            transformer.transform(src, res);
        }
        catch (FOPException fope) {
            LOGGER.log(Level.SEVERE, "{0}", new Object[]{fope.getMessage()});
        }
        catch (TransformerConfigurationException tce) {
            LOGGER.log(Level.SEVERE, "{0}", new Object[]{tce.getMessage()});
        }
        catch (TransformerException te) {
            LOGGER.log(Level.SEVERE, "{0}", new Object[]{te.getMessage()});
        }
    }

    public String getXml() {
        return xml;
    }

    public String getFo() {
        return fo;
    }
}
