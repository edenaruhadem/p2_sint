import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.IOException;

public class XML_XSD
{
    static String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    static String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    static String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    static String MY_SCHEMA = "iml.xsd";
    public static XML_XSD_ErrorHandler error=null;
    public static void main(String[] args) {
        //Creada batería de parsers
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //Asignación de atributos
        dbf.setValidating(true);
        dbf.setNamespaceAware(true);
        dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        dbf.setAttribute(JAXP_SCHEMA_SOURCE, MY_SCHEMA);
        DocumentBuilder db = null;
        Document doc = null;
        try
        {
        //Creacion de un parser de la familia de la batería de antes
        db = dbf.newDocumentBuilder();
        error = new XML_XSD_ErrorHandler();
        //Asingación de la clase de control de errores al parser 
        db.setErrorHandler(error);
        }catch(ParserConfigurationException e){
            e.printStackTrace();
        }
        
        try{
            //Generacion del arbol DOM tras el parseo. Generará un error el método parse si el documento no  es well-formed. Una SAXException
        //Saltará la clase de gestión de errores en caso de que los contenga (no válido según el schema xml definido). 
            doc = db.parse("http://gssi.det.uvigo.es/users/agil/public_html/SINT/18-19/iml2001.xml");
        }catch(SAXException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

        String tipo = doc.getDoctype().getName();
        System.out.print("El tipo es "+tipo);
        Element raiz = doc.getDocumentElement();
        System.out.print("El elemento raiz es "+raiz.getTagName());        
    }
}
class XML_XSD_ErrorHandler extends DefaultHandler {
    public XML_XSD_ErrorHandler () {}
    //Metodos de la clase
    public void warning (SAXParseException spe) { System.out.println("Warning: "+spe.toString()); }
    public void error (SAXParseException spe) { System.out.println("Error: "+spe.toString()); }
    public void fatalerror (SAXParseException spe) { System.out.println("Fatal Error: "+spe.toString());}
    }