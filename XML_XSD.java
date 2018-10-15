import javax.xml.parsers.DocumentBuilderFactory;
import javax.security.sasl.SaslException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashSet;
import java.util.LinkedList;

import java.io.IOException;

public class XML_XSD
{
    static String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    static String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    static String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    static String MY_SCHEMA = "iml.xsd";
    public static XML_XSD_ErrorHandler error=null;
    public static String url;

    public static void main(String[] args) {
        //Creada batería de parsers
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //Asignación de atributos
        dbf.setValidating(true); //Para DTD true. Por defecto ya está a false.
        dbf.setNamespaceAware(true);
        dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        dbf.setAttribute(JAXP_SCHEMA_SOURCE, MY_SCHEMA);
        //dbf.setSchema(Schema);        
        DocumentBuilder db = null;
        Document doc = null;
        LinkedList<String> files = new LinkedList<String>();
        LinkedList<String> errores = new LinkedList<String>();
        HashSet<Document> correctos = new HashSet<Document>();
        HashSet<String> leidos = new HashSet<String>();
        HashSet<String> anios_hash = new HashSet<String>();
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
        files.add("iml2001.xml");
        //files.add("http://gssi.det.uvigo.es/users/agil/public_html/SINT/18-19/iml2001.xml");
        while(!files.isEmpty())
        {
            url = (String) files.getFirst();
            if(!leidos.contains(url))
            {
                try{
                    //Generacion del arbol DOM tras el parseo. Generará un error el método parse si el documento no  es well-formed. Una SAXException
                //Saltará la clase de gestión de errores en caso de que los contenga (no válido según el schema xml definido).            
                    doc = db.parse(url);
                    //System.out.print("Esta parseando");
                }catch(SAXException e){
                    e.printStackTrace();
                    //System.out.print("Salto la primera");
                }catch(IOException e){
                    e.printStackTrace();
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(error.hasError())
                {                    
                    if(!error.Warning().equals(""))
                    {
                        //System.out.print("Hay warning");
                        errores.add(error.Warning());
                    }
                    if(!error.Error().equals(""))
                    {
                        //System.out.print("Hay errores");
                        errores.add(error.Error());
                    }
                    if(!error.fatalError().equals(""))
                    {
                        //System.out.print("Hay errores fatales");
                        errores.add(error.fatalError());
                    }
                }
                else
                {
                    correctos.add(doc);
                    Element raiz = doc.getDocumentElement();
                    NodeList anios = raiz.getElementsByTagName("Anio");
                    NodeList urls = raiz.getElementsByTagName("IML");
                    Node itemAnio =anios.item(0);
                    //int num_url = urls.getLength();
                    //System.out.println(num_url);
                    anios_hash.add(itemAnio.getTextContent());
                    for(int i = 0;i<urls.getLength();i++)
                    {
                        Node itemUrl = urls.item(i);                        
                        files.add(itemUrl.getTextContent());
                    }
                }
            }
            files.removeFirst();           
        }
        //String tipo = doc.getDoctype().getName();
        //System.out.format("El tipo es %s",tipo);
        //Element raiz = doc.getDocumentElement(); //Acceso al nodo hijo del documento. En este caso el primero, Songs
        anios_hash.stream().forEach(System.out::println);        
    }
}
class XML_XSD_ErrorHandler extends DefaultHandler {
    private boolean err = false;
    private String strWarning = "";
    private String strError = "";
    private String strFatalError = "";
    public XML_XSD_ErrorHandler () {}    
    //Metodos de la clase
    public void warning (SAXParseException spe) throws SAXException 
    {
        err = true;
        strWarning = "Warning: "+spe.toString(); 
        System.out.println("Warning: "+spe.toString()); 
    }
    public void error (SAXParseException spe) throws SAXException 
    { 
        err = true;
        strError = "Error: "+spe.toString();
        System.out.println("Error: "+spe.toString()); 
    }
    public void fatalerror (SAXParseException spe) throws SAXException 
    {
        err = true;
        strFatalError = "Fatal Error: "+spe.toString();
        System.out.println("Fatal Error: "+spe.toString());
    }
    public boolean hasError()
    {
        if(err)
        {
            return true;
        }
        else
        {
            return false;
        }
    } 
    public String Warning()
    {        
        return strWarning;
    }
    public String Error()
    {        
        return strError;
    }
    public String fatalError()
    {        
        return strFatalError;
    }
         


}