import java.io.*;
//Servlets
import javax.servlet.*;
import javax.servlet.http.*;
//Estructuras de datos
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.annotation.*;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Iterator;
//Parsers, DOM y Exceptions
import javax.xml.parsers.DocumentBuilderFactory;
import javax.security.sasl.SaslException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
//import java.io.IOException;

@WebServlet("/P2IM")
public class Sint48P2 extends HttpServlet {
    //private static final long serialVersionUID = 1L;
	//Atributes Schema
	    static String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    	static String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    	static String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
        static String MY_SCHEMA = "iml.xsd"; //En mi ordenador este archivo y los xml están en bin. Esto es una chapuza
        //static String MY_SCHEMA = "iml.xsd";
    	public static Error error=null; //Objeto error
    	public static String url;

	    //Declaración de estructuras de datos
	    public static HashMap<String,Document> mapDocs = new HashMap<String,Document>(); //ficheros correctos. key = anio, value = document
	    //HashSet<Document> correctos = new HashSet<Document>();
	
	
        public static ArrayList<String>Anios = new ArrayList<String>();
        public static ArrayList<Disco>listaDiscos = new ArrayList<Disco>();
        public static ArrayList<Cancion>listaCanciones = new ArrayList<Cancion>();
        public static ArrayList<Cancion>Resultado = new ArrayList<Cancion>();
        //public static ArrayList<String>fichErroneos = new ArrayList<String>();
        public static ArrayList<String>listaErrores= new ArrayList<String>();
        public static ArrayList<String>listaEFatales = new ArrayList<String>();
        public static ArrayList<String>listaWarnings = new ArrayList<String>();               
        
    public void init(ServletConfig config) throws ServletException
    {        
	//----------------Aquí hay que leer los ficheros. Eliminar erróneos para el procesado posterior-------------    	
	//Creada batería de parsers
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //Asignación de atributos
        dbf.setValidating(true);
        dbf.setNamespaceAware(true);
        dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        dbf.setAttribute(JAXP_SCHEMA_SOURCE, MY_SCHEMA);
        //Declaración de objetos: db y doc(almacén del árbol)       
        DocumentBuilder db = null;
        Document doc = null;
	//Estructura dinámica de todos los archivos obtenidos al leer uno (mediante los tags IML)
        LinkedList<String> listaFicheros = new LinkedList<String>();        
        //Estructura dinámica para no repetir archivos 
        HashSet<String> leidos = new HashSet<String>();        
	    String strAnios = null;
        //HashSet<String> anios_hash = new HashSet<String>();
	//-------------Objeto clase document builder dbf(conjunto de parsers)------------------
	try
        {
        //Creacion de un parser de la familia de la batería de antes
        db = dbf.newDocumentBuilder();
        error = new Error(); //Creacion del objeto error
        //Asingación de la clase de control de errores al parser 
        db.setErrorHandler(error);
        }catch(ParserConfigurationException e){
            e.printStackTrace();
        }
        //files.add("iml2001.xml");
        listaFicheros.add("iml2001.xml"); //Archivo xml a parsear con el schema        
        //files.add("http://gssi.det.uvigo.es/users/agil/public_html/SINT/18-19/iml2001.xml");
	while(!listaFicheros.isEmpty())
        {
        url = (String) listaFicheros.getFirst();
        //System.out.println(url);
        //System.out.println(url);
            if(!leidos.contains(url))
            {
                try{
                    //Generacion del arbol DOM tras el parseo. Generará un error el método parse si el documento no  es well-formed. 				Una SAXException
                //Saltará la clase de gestión de errores en caso de que los contenga (no válido según el schema xml 				definido).            
                    
                    doc = db.parse(url);
                    leidos.add(url);
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
                    if(!error.getWarning().equals(""))
                    {
                        //System.out.print("Hay warning");
                        listaWarnings.add(error.getWarning());  //Hay que añadir tipos error
                    }
                    if(!error.getErrores().equals(""))
                    {
                        //System.out.print("Hay errores");
                        listaErrores.add(error.getErrores());
                    }
                    if(!error.getFatalError().equals(""))
                    {
                        //System.out.print("Hay errores fatales");
                        listaEFatales.add(error.getFatalError());
                    }
                }//if hasError()
                else
                {
                    //correctos.add(doc);		    
                    Element raiz = doc.getDocumentElement(); //Obtencion del elemento Songs
                    NodeList anios = raiz.getElementsByTagName("Anio"); //Recoge todos los elementos anios del xml. Solo hay uno
                    NodeList urls = raiz.getElementsByTagName("IML"); //Recoge todos los elementos IML del xml
                    Node itemAnio =anios.item(0);                    
		            strAnios = itemAnio.getTextContent();
		            mapDocs.put(strAnios,doc);                    
                    for(int i = 0;i<urls.getLength();i++) //El acceso al texto de IML produce redirecciones a nuevos documentos
                    {
                        Node itemUrl = urls.item(i);                        
                        listaFicheros.add(itemUrl.getTextContent());
                        /*System.out.println("--------------------------------");
                        System.out.println(itemUrl.getTextContent());
                        System.out.println("--------------------------------");*/
                    }
                }//else hasError
            }//if leidos containsURL
            listaFicheros.removeFirst();            
        }///While true		
	}//Init*/            

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        res.setContentType("text/html ; charset=utf-8");
        PrintWriter out = res.getWriter();
	    String p = req.getParameter("p");
        String fase = req.getParameter("pfase");
        String anio = req.getParameter("panio");
        String idd = req.getParameter("pidd");
        String idc = req.getParameter("pidc");
        String auto = req.getParameter("auto");      

        if((p==null)&&(auto.equals("si")))
        {
                doXmlNop(res);
        }
        else if((!p.equals("d4r18c392b"))&&(auto.equals("si")))
        {
                doXmlIp(res);
        }        
	else
	{
		switch(fase)
        	{                        
            	case "01": doGetFase01(out,auto,res); break;
            	case "02": doGetFase02(out,auto,res); break;
            	case "11": doGetFase11(out,auto,res); break;
            	case "12": doGetFase12(out,auto,res,anio); break;
            	case "13": doGetFase13(out,auto,res,anio,idd); break;
            	case "14": doGetFase14(out,auto,res,anio,idd,idc); break;
        	}

	}        
    }//doGet              
    public void doGetFase01(PrintWriter out, String auto, HttpServletResponse res)throws IOException
    {
        if(auto==null)
        {
            doHtmlF01(out);                
        }
        else if(auto.equals("si"))
        {
            doXmlF01(res);
        }                                  
    }//doHtmlF01
    public void doHtmlF01(PrintWriter out)
    {
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=utf-8'></meta>");
        out.println("<title>Sint: Práctica 2. Consulta de canciones</title>");    
        out.println("<link rel='stylesheet' type='text/css' href='iml.css'></link>"); //href='iml.css' en el lab
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servicio de consulta de canciones</h1>");
        out.println("<h2>Bienvenido a este servicio.</h2>");                                 
        out.println("<form name = 'miformfase01' action=''>");
        out.println("<a href = '/sint48/P2IM?p=d4r18c392b&pfase=02'>Pulsa aquí para ver los ficheros erróneos</a>");
        out.println("<h3>Selecciona una consulta:</h3>");
        out.println("<input type = 'radio' checked>Consulta1: Canciones de un intérprete que duran menos que una dada</input>");
        out.println("<br></br>");
        out.println("<input type = 'submit' class = 'buttonSubmit'></input>");
        out.println("<input type = 'hidden' name = 'p' value = d4r18c392b></input>");
        out.println("<input type = 'hidden' name = 'pfase' value = '11'></input>");	
        out.println("</form>");
        out.println("</body>");
        out.println("<footer>");
        out.println("<p>sint48. @Diego Rios Castro.</p>");                
        out.println("</footer>");
        out.println("</html>");
    }//doHtmlF01
    public void doXmlF01(HttpServletResponse res)throws IOException
    {
        res.setContentType("text/xml");
        PrintWriter out = res.getWriter();        
        out.println("<?xml version='1.0' encoding='utf-8' ?>");
        out.println("<service>");
        out.println("<status>OK</status>");
        out.println("</service>");
    }
    public void doGetFase02(PrintWriter out, String auto, HttpServletResponse res)throws IOException
    {
        if(auto==null)
        {
            doHtmlF02(out);                
        }
        else if(auto.equals("si"))
        {
            doXmlF02(res);
        }         
    }//doGetFase02
    public void doHtmlF02(PrintWriter out)
    {
	//int warn = warns.size();
	//int err = errores.size();
    //int fErr = fatalErr.size();
    int numError = listaErrores.size();
    int numEFatal = listaEFatales.size();
    int numWarning = listaWarnings.size();    

    out.println("<html>");
    out.println("<head>");
    out.println("<meta charset=utf-8'></meta>");
    out.println("<title>Sint: Práctica 2. Consulta de canciones</title>");
	out.println("<link rel='stylesheet' type='text/css' href='iml.css'></link>");
    out.println("</head>");
    out.println("<body>");
    out.println("<h1>Servicio de consulta de canciones</h1>");
	//Ficheros con warnings
    out.println("<h2>Se han encontrado "+numWarning+" ficheros con warnings.</h2>");
    //if(numWarning!=0)
    //{
        for (int i=0;i<numWarning;i++)
	    {
	        out.println("<p>"+listaWarnings.get(i)+"</p>");
	    }
    //}	
    out.println("<h2>Se han encontrado "+numError+" ficheros con errores</h2>");
    //if(numError!=0)
    //{
        for (int i=0;i<listaErrores.size();i++)
	    {
	        out.println("<p>"+listaErrores.get(i)+"</p>");
        }
    //}	              
    out.println("<h2>Se han encontrado "+numEFatal+" ficheros con errores fatales</h2>");
    //if(numEFatal!=0)
    //{
        for (int i=0;i<numEFatal;i++)
	    {
	        out.println("<p>"+listaEFatales.get(i)+"</p>");
	    } 
    //}	              
    out.println("<button class = 'buttonAtras'onclick=\"window.location='/sint48/P2IM?p=d4r18c392b&pfase=01'\">Atras</button>");
    out.println("</body>");
    out.println("<footer>");
    out.println("<p>sint48. @Diego Rios Castro.</p>");                
    out.println("</footer>");
    out.println("</html>");
    }//doHtmlF02
    public void doXmlF02(HttpServletResponse res)throws IOException
    {    
        res.setContentType("text/xml");
        PrintWriter out = res.getWriter();
        out.println("<?xml version='1.0' encoding='utf-8' ?>");
        out.println("<errores>");
        out.println("<warnings>");
        for(int i=0;i<listaWarnings.size();i++)
        {
        out.println("<warning>");        
        out.println("<file>URL del fichero que provoca el warning"+listaWarnings.get(i)+"</file>");
        out.println("<cause>Explicación propia o proporcionada por el parser</cause>");
        out.println("</warning>");
        }   
        out.println("</warnings>");
        out.println("<errors>");        
        for(int i=0;i<listaErrores.size();i++)
        {
        out.println("<error>");
        out.println("<file>URL del fichero que provoca el error"+listaErrores.get(i)+"</file>");
        out.println("<cause>Explicación propia o proporcionada por el parser</cause>");
        out.println("</error>");
        }        
        out.println("</errors>");
        out.println("<fatalerrors>");
        for(int i =0;i<listaEFatales.size();i++)
        {
        out.println("<fatalerror>");
        out.println("<file>URL del fichero que provoca el fatal error"+listaEFatales.get(i)+"</file>");
        out.println("<cause>Explicación propia o proporcionada por el parser</cause>");
        out.println("</fatalerror>");
        }      
        out.println("</fatalerrors>");
        out.println("</errores>");
    }
    public void doGetFase11(PrintWriter out, String auto,HttpServletResponse res)throws IOException
    {        
        Anios = getC1Anios(); //Anios es un array list <string>
        if(auto==null)
        {
            doHtmlF11(out,Anios);                
        }
        else if(auto.equals("si"))
        {
            doXmlF11(res,Anios);
        }        
    }//doGetFase11
    public void doGetFase12(PrintWriter out, String auto, HttpServletResponse res, String anio)throws IOException
    {        
        listaDiscos = getC1Discos(anio);
        if(auto==null)
        {
             doHtmlF12(out,anio, listaDiscos);                
        }
        else if(auto.equals("si"))
        {
            doXmlF12(res,anio,listaDiscos);
        }       
        
    }//doGetFase12
    public void doGetFase13(PrintWriter out, String auto,HttpServletResponse res, String anio, String idd)throws IOException
    {        
        listaCanciones = getC1Canciones(anio, idd);
        if(auto==null)
        {
            doHtmlF13(out,anio, idd, listaCanciones);                
        }
        else if(auto.equals("si"))
        {
            doXmlF13(res,idd,listaCanciones);
        }       
        
    }//doGetFase13
    public void doGetFase14(PrintWriter out, String auto, HttpServletResponse res, String anio, String idd, String idc)throws IOException
    {        
        Resultado = getC1Resultado(anio, idd, idc);
        if(auto==null)
        {
                doHtmlF14(out,anio, idd, idc, Resultado);                
        }
        else if(auto.equals("si"))
        {
                doXmlF14(res,idc,Resultado);
        }   
        
    }//doGetFase14     
    
public static ArrayList<String> getC1Anios()
{	
	for(String key:mapDocs.keySet()){
        Anios.add(key);
    }	
	return Anios;    
}

public static ArrayList<Disco> getC1Discos (String anio) //Type Disco
{
    Document res = null;
    String atributoUno = null;
    String atributoDos = null;
    String atributoTres = null;
    String atributoCuatro = null;
    String idiomaPais = null;    
    for (String key:mapDocs.keySet()){
        if(anio.equals(key))
        {
            res = mapDocs.get(key);
        }
    }
    Element raiz = res.getDocumentElement(); //Obtencion del elemento Songs
    NodeList pais = raiz.getElementsByTagName("Pais");
    NodeList discos = raiz.getElementsByTagName("Disco");
    for(int i = 0; i<pais.getLength();i++)
    {
        Node itemPais = pais.item(i);       
        idiomaPais = itemPais.getAttributes().getNamedItem("lang").getTextContent();          
        for(int j = 0;j<discos.getLength();j++) //El acceso al texto de IML produce redirecciones a nuevos documentos
        {            
            Node itemDisco = discos.item(j);
            Node parentDisco = itemDisco.getParentNode();
            if(parentDisco.equals(itemPais))
            {
                NamedNodeMap atributosDisco = itemDisco.getAttributes();
            if(atributosDisco.getLength()>1)
            {
                atributoTres=atributosDisco.getNamedItem("idd").getTextContent();
                atributoCuatro=atributosDisco.getNamedItem("langs").getTextContent();
            }
            else{
                atributoTres=atributosDisco.getNamedItem("idd").getTextContent();
                atributoCuatro=idiomaPais;
            }                                                         
            NodeList itemDiscoChild = itemDisco.getChildNodes();         

            for(int k = 0;k<itemDiscoChild.getLength(); k++)
            {
                if(itemDiscoChild.item(k).getNodeName().equals("Titulo"))
                {
                    atributoUno = itemDiscoChild.item(k).getTextContent();
                }
                if(itemDiscoChild.item(k).getNodeName().equals("Interprete"))
                {
                    atributoDos = itemDiscoChild.item(k).getTextContent();
                }                
            }
            listaDiscos.add(new Disco(atributoUno,atributoDos,atributoTres, atributoCuatro));           
            }                                 
        }              
    }
    return listaDiscos;
}
public static ArrayList<Cancion> getC1Canciones (String anio, String idd) //Type Cancion
{
    Document res = null;
    String atributoUno = null;
    String atributoDos = null;
    String atributoTres = null;
    String atributoCuatro = null;
    String atributoCinco = null;
    //String atributoCinco[] = {""};
    for (String key:mapDocs.keySet()){
        if(anio.equals(key))
        {
            res = mapDocs.get(key);
        }
    }
    Element raiz = res.getDocumentElement(); //Obtencion del elemento Songs
    NodeList canciones = raiz.getElementsByTagName("Cancion");
    for(int i = 0;i<canciones.getLength();i++) //El acceso al texto de IML produce redirecciones a nuevos documentos
    {
        Node itemcancion = canciones.item(i);
        String stridc=itemcancion.getAttributes().getNamedItem("idc").getTextContent();
        atributoCuatro = stridc;
        String substridd = idd.substring(0,12);
        String substridc = stridc.substring(0,12);
        if (substridc.equals(substridd))
        {
            NodeList itemCancionChild = itemcancion.getChildNodes();
            for(int j = 0;j<itemCancionChild.getLength(); j++)
            {
                if(itemCancionChild.item(j).getNodeName().equals("Titulo"))
                {
                    atributoUno = itemCancionChild.item(j).getTextContent();
                }
                if(itemCancionChild.item(j).getNodeName().equals("Genero"))
                {
                    atributoDos = itemCancionChild.item(j).getTextContent();
                }
                if(itemCancionChild.item(j).getNodeName().equals("Duracion"))
                {
                    atributoTres = itemCancionChild.item(j).getTextContent();
                }                
            }
            listaCanciones.add(new Cancion(atributoUno,atributoDos,atributoTres, atributoCuatro, atributoCinco));
        }
    }
    return listaCanciones;
}
public static ArrayList<Cancion> getC1Resultado (String anio, String idd, String idc) //Todas las canciones de un interprete que duren menos que una elegida
{
    String dur = null;
    String interprete = null;
    String atributoUno = null;
    String atributoDos = null;
    String atributoTres = null;
    String atributoCuatro = null;
    String atributoCinco = null;
    //ArrayList<String> atributoCinco = new ArrayList<String>();
    //String atributoCinco[] = {""};
    Document res = null;
    Boolean flag = false;
    ArrayList<Cancion> listares = new ArrayList<Cancion>();    
    for(int i = 0;i<listaCanciones.size();i++)
    {
        Cancion objc = listaCanciones.get(i);        
        if(objc.getIdc(objc).equals(idc))
        {            
            dur = objc.getDuracion(objc); 
        }
    }
    for(int i = 0;i<listaDiscos.size();i++)
    {
        Disco objd = listaDiscos.get(i);        
        if(objd.getIDD(objd).equals(idd))
        {
            interprete = objd.getInterprete(objd); 
        }
    }
    /*for (String key:mapDocs.keySet())    
    {*/        
    for (String key:mapDocs.keySet())
    {
        res = mapDocs.get(key);
    //Iterator it = mapDocs.entrySet().iterator();
    //while (it.hasNext()) 
    //{
        //Map.Entry e = (Map.Entry)it.next();
        //res = (Document)e.getValue();
        //res = entry.getValue();    
        //res = mapDocs.get(key);
        Element raiz = res.getDocumentElement();
        NodeList nodeDiscos = raiz.getElementsByTagName("Disco");
        for(int i = 0;i<nodeDiscos.getLength();i++)
        {
            Node itemDisco = nodeDiscos.item(i);
            NodeList childDiscos = itemDisco.getChildNodes();            
            for (int j=0;j<childDiscos.getLength();j++)
            {
                if(childDiscos.item(j).getNodeName().equals("Interprete"))
                {
                    //NodeList sibInterprete = childDiscos.item(j).getNextSibling();
                    String nameInt = childDiscos.item(j).getTextContent();
                    if(nameInt.equals(interprete))
                    {
                        flag = true;                        
                    }
                }
                if(flag)
                {
                    /*if(childDiscos.item(j).getNodeName().equals("Premios"))
                    {
                        Node itemPremios = childDiscos.item(j);
                        NodeList childPremios = itemPremios.getChildNodes();
                        if(childPremios.getLength()>0)
                        {
                            for(int k =0;k<childPremios.getLength();k++)
                            {
                                if(childPremios.item(k).getNodeName().equals("Premio"))
                                {
                                    atributoCinco.add(childPremios.item(k).getTextContent());
                                }                                
                            }
                        }                                              
                    }*/
                    if(childDiscos.item(j).getNodeName().equals("Cancion"))
                    {
                        Node itemcancion = childDiscos.item(j);
                        atributoCuatro=itemcancion.getAttributes().getNamedItem("idc").getTextContent();
                        NodeList childCancion = itemcancion.getChildNodes();
                        
                        for(int k = 0;k<childCancion.getLength(); k++)
                        {
                            //Node firstchilcancion = childCancion.item(k).getFirstChild();                            
                            atributoCinco = childCancion.item(k).getNodeName();                                                       
                            if(childCancion.item(k).getNodeName().equals("Titulo"))
                            {
                                atributoUno = childCancion.item(k).getTextContent();
                            }
                            if(childCancion.item(k).getNodeName().equals("Genero"))
                            {
                                atributoDos = childCancion.item(k).getTextContent();
                            }
                            if(childCancion.item(k).getNodeName().equals("Duracion"))
                            {
                                atributoTres = childCancion.item(k).getTextContent();
                            }
                            if(childCancion.item(k).getNodeName().equals("#text"))  
                            {
                                atributoCinco = childCancion.item(k).getNodeValue();                               
                            }
                                                                          
                        }
                        listares.add(new Cancion(atributoUno,atributoDos,atributoTres, atributoCuatro, atributoCinco));                       
                    }                    
                }
            }
            flag = false;
        }               
    }
    //Recorre listares y obtener las duraciones comparando cada una con la elegida. Si es menor, meterlo en la lista resultado
    for (int i = 0;i<listares.size();i++)
    {
        Cancion obj = listares.get(i);
        int durobj = Integer.parseInt(obj.getDuracion(obj));
        if(durobj < Integer.parseInt(dur))
        {
            //Resultado.add(obj); Forma rápida inicial
            atributoUno = obj.getTitulo(obj);
            atributoDos = obj.getGenero(obj);
            atributoTres = obj.getDuracion(obj);
            atributoCuatro = obj.getIdc(obj);
            atributoCinco = obj.getDescripcion(obj);
            Resultado.add(new Cancion(atributoUno, atributoDos, atributoTres, atributoCuatro, atributoCinco));
        }
    }
    return Resultado;        
}
public void doHtmlF11(PrintWriter out, ArrayList<String> Anios)
{
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=utf-8'></meta>");
        out.println("<title>Sint: Práctica 2. Consulta de canciones</title>");
	out.println("<link rel='stylesheet' type='text/css' href='iml.css'></link>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servicio de consulta de canciones</h1>");
        out.println("<h2>Consulta 1</h2>");    
        out.println("<h3>Selecciona un año:</h3>");
        out.println("<form name = 'miformfase11'>");
	out.println("<input type = 'hidden' name = 'p' value = 'd4r18c392b'></input>");    
        out.println("<input type = 'hidden' name = 'pfase' value = '12'></input>");	
        out.println("<ol>");
        for(int i=0;i<Anios.size();i++)
        {
        out.println("<li><input type = 'radio' name = 'panio' value = "+Anios.get(i)+">-"+Anios.get(i)+"</input></li>");
        }        
        out.println("</ol>");
        out.println("<br></br>");        
        out.println("<input type = 'submit' class = 'buttonSubmit'></input>");            
        out.println("</form>");
        out.println("<button type = 'button' class = 'buttonAtras' onclick=\"window.location='/sint48/P2IM?p=d4r18c392b&pfase=01'\">Atras</button> ");
        out.println("</body>");
        out.println("<footer>");
        out.println("<p>sint48. @Diego Rios Castro.</p>");                
        out.println("</footer>");
        out.println("</html>");
}
public void doXmlF11(HttpServletResponse res, ArrayList<String> Anios)throws IOException
{    
        res.setContentType("text/xml");    
        PrintWriter out = res.getWriter();
        out.println("<?xml version='1.0' encoding='utf-8' ?>");
        out.println("<anios>");
        for(int i=0; i<Anios.size();i++)
        {
                out.println("<anio>"+Anios.get(i)+"</anio>");
        }
        out.println("</anios>");
}

public void doHtmlF12(PrintWriter out, String anio, ArrayList<Disco> listaDiscos)
{
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Sint: Práctica 2. Consulta de canciones</title>");
        out.println("<meta charset=utf-8'></meta>");
	    out.println("<link rel='stylesheet' type='text/css' href='iml.css'></link>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servicio de consulta de canciones</h1>");    
        out.println("<h2>Consulta 1: Año="+anio+"</h2>");    
        out.println("<h3>Selecciona un disco:</h3>");
        out.println("<form name = 'miformfase12'>");
	    out.println("<input type = 'hidden' name = 'p' value = 'd4r18c392b'></input>");
        out.println("<input type = 'hidden' name = 'pfase' value = '13'></input>");	
        out.println("<input type = 'hidden' name = 'panio' value ='"+anio+"'></input>");
        out.println("<ol>");
        for(int i=0;i<listaDiscos.size();i++)
        {
        Disco d = listaDiscos.get(i);            
        out.println("<li><input type = 'radio' name = 'pidd' value = "+d.getIDD(d)+">- Titulo ='"+d.getTitulo(d)+"' --- IDD ='"+d.getIDD(d)+"' --- Interprete ='"+d.getInterprete(d)+"' --- Idiomas ='"+d.getIdiomas(d)+"'</input></li>");        
        }        
        out.println("</ol>");
        out.println("<br></br>");    
        out.println("<input type = 'submit' class = 'buttonSubmit'></input>");
        out.println("</form>");
        out.println("<button class = 'buttonAtras' onclick=\"window.location='/sint48/P2IM?p=d4r18c392b&pfase=11'\">Atras</button> ");
        out.println("<br></br>");
        out.println("<button class = 'buttonInicio' onclick=\"window.location='/sint48/P2IM?p=d4r18c392b&pfase=01'\">Inicio</button> ");
        out.println("</body>");
        out.println("<footer>");
        out.println("<p>sint48. @Diego Rios Castro.</p>");                
        out.println("</footer>");
        out.println("</html>");
}
public void doXmlF12(HttpServletResponse res, String anio, ArrayList<Disco> listaDiscos)throws IOException
    {
            res.setContentType("text/xml");
            PrintWriter out = res.getWriter();            
            out.println("<?xml version='1.0' encoding='utf-8' ?>");
            if(anio==null)
            {
                out.println("<wrongRequest>no param:panio</wrongRequest>");
            }            
            else
            {
                out.println("<discos>");
                for(int i=0;i<listaDiscos.size();i++)
                {
                Disco d = listaDiscos.get(i);               
                out.println("<disco idd="+d.getIDD(d)+" interprete="+d.getInterprete(d)+" langs="+d.getIdiomas(d)+">"+d.getTitulo(d)+"</disco>");
                }
                out.println("</discos>");                            
            }
    }
public void doHtmlF13(PrintWriter out, String anio, String idd, ArrayList<Cancion> listaCanciones)
{
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Sint: Práctica 2. Consulta de canciones</title>");
        out.println("<meta charset=utf-8'></meta>");
	    out.println("<link rel='stylesheet' type='text/css' href='iml.css'></link>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servicio de consulta de canciones</h1>");    
        out.println("<h2>Consulta 1: Año="+anio+", Disco="+idd+"</h2>");
        out.println("<form name = 'miformfase13'>");    
        out.println("<h3>Selecciona una cancion:</h3>");
	    out.println("<input type = 'hidden' name = 'p' value = 'd4r18c392b'></input>");
        out.println("<input type = 'hidden' name = 'pfase' value = '14'></input>");	
        out.println("<input type = 'hidden' name = 'panio' value = '"+anio+"'></input>");
        out.println("<input type = 'hidden' name = 'pidd' value = '"+idd+"'></input>");
        out.println("<ol>");
        for(int i=0;i<listaCanciones.size();i++)
        {
        Cancion c = listaCanciones.get(i);
        out.println("<li><input type = 'radio' name = 'pidc' value = "+c.getIdc(c)+">- Titulo ='"+c.getTitulo(c)+"' --- IDC ='"+c.getIdc(c)+"' --- Genero ='"+c.getGenero(c)+"' --- Duracion ='"+c.getDuracion(c)+" seg.'</input></li>");        
        }        
        out.println("</ol>");
        out.println("<br></br>");    
        out.println("<input type = 'submit' class = 'buttonSubmit'></input>");
        out.println("</form>");
        out.println("<button class = 'buttonAtras' onclick=\"window.location='/sint48/P2IM?p=d4r18c392b&pfase=12&panio="+anio+"'\">Atras</button> ");
        out.println("<br></br>");    
        out.println("<button class = 'buttonInicio' onclick=\"window.location='/sint48/P2IM?p=d4r18c392b&pfase=01'\">Inicio</button> ");
        out.println("</body>");
        out.println("<footer>");
        out.println("<p>sint48. @Diego Rios Castro.</p>");                
        out.println("</footer>");
        out.println("</html>");

}
public void doXmlF13(HttpServletResponse res, String idd, ArrayList<Cancion> listaCanciones)throws IOException
    {
            res.setContentType("text/xml");
            PrintWriter out = res.getWriter();
            out.println("<?xml version='1.0' encoding='utf-8' ?>");
            if(idd==null)
            {
                out.println("<wrongRequest>no param:pidd</wrongRequest>");
            }            
            else
            {
                out.println("<canciones>");
                for(int i=0;i<listaCanciones.size();i++)
                {
                Cancion c = listaCanciones.get(i);
                out.println("<cancion idc="+c.getIdc(c)+" genero="+c.getGenero(c)+" duracion="+c.getDuracion(c)+">"+c.getTitulo(c)+"</cancion>");
                }
                out.println("</canciones>");                                         
            }
            
    }
public void doHtmlF14(PrintWriter out, String anio, String idd, String idc, ArrayList<Cancion> Resultado)
{
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Sint: Práctica 2. Consulta de canciones</title>");
        out.println("<meta charset=utf-8'></meta>");
	    out.println("<link rel='stylesheet' type='text/css' href='iml.css'></link>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servicio de consulta de canciones</h1>");
        out.println("<form name = 'miformfase14'>");
        out.println("<h2>Consulta 1: Año="+anio+", Disco="+idd+", Cancion="+idc+"</h2>");    
        out.println("<h3>Este es el resultado:</h3>");
        for(int i=0;i<Resultado.size();i++)
        {
            Cancion obj = Resultado.get(i);
            //ArrayList<String> premios = obj.getPremios(obj);
            /*if(premios.isEmpty())
            {
                out.println("<p>- Titulo = '"+obj.getTitulo(obj)+"'</p>");
            }
            else
            {*/
            out.println("<p>- Titulo = '"+obj.getTitulo(obj)+"' --- Descripcion = '"+obj.getDescripcion(obj)+"' --- IDC = '"+obj.getIdc(obj)+"'</p>");  
            //}           
        }        
        out.println("</form>");
        out.println("<button class = 'buttonAtras'  onclick=\"window.location='/sint48/P2IM?p=d4r18c392b&pfase=13&panio="+anio+"&pidd="+idd+"'\">Atras</button> ");
        out.println("<br></br>");
        out.println("<button class = 'buttonInicio' onclick=\"window.location='/sint48/P2IM?p=d4r18c392b&pfase=01'\">Inicio</button> ");
        out.println("</body>");
        out.println("<footer>");
        out.println("<p>sint48. @Diego Rios Castro.</p>");                
        out.println("</footer>");
        out.println("</html>"); 
}
public void doXmlF14(HttpServletResponse res,String idc, ArrayList<Cancion> Resultado)throws IOException
    {
        res.setContentType("text/xml");
        PrintWriter out = res.getWriter();
        out.println("<?xml version='1.0' encoding='utf-8' ?>");
        if(idc==null)
            {
                out.println("<wrongRequest>no param:pidc</wrongRequest>");
            }            
            else
            {
                out.println("<canciones>");
                for(int i=0;i<Resultado.size();i++)
                {
                Cancion obj = Resultado.get(i);
                out.println("<cancion decripcion="+obj.getDescripcion(obj)+">"+obj.getTitulo(obj)+"</cancion>");
                }
                out.println("</canciones>");                                                       
            }        
    }
public void doXmlNop(HttpServletResponse res)throws IOException
    {
        res.setContentType("text/xml");
        PrintWriter out = res.getWriter();
        out.println("<?xml version='1.0' encoding='utf-8' ?>");
        out.println("<wrongRequest>no passwd</wrongRequest>");        
    }
public void doXmlIp(HttpServletResponse res)throws IOException
    {
        res.setContentType("text/xml");
        PrintWriter out = res.getWriter();
        out.println("<?xml version='1.0' encoding='utf-8' ?>");
        out.println("<wrongRequest>bad passwd</wrongRequest>");        
    }
}


class Error extends DefaultHandler { //Clase gestión errores parseo
    //Atributos clase Error
    private boolean err = false;
    private String warns = "";
	private String errores = "";
    private String fatalErr = "";
    //Constructor    
    public Error () {}    
    //Metodos de la clase
    public void warning (SAXParseException spe) throws SAXException 
    {        
        err = true;
        warns = "Warning: "+spe.toString(); 
        System.out.println("Warning: "+spe.toString());
        //err = false; 
    }
    public void error (SAXParseException spe) throws SAXException 
    {         
        err = true;
        errores = "Error: "+spe.toString();
        System.out.println("Error: "+spe.toString());
        //err = false; 
    }
    public void fatalerror (SAXParseException spe) throws SAXException 
    {        
        err = true;
        fatalErr = "Fatal Error: "+spe.toString();
        System.out.println("Fatal Error: "+spe.toString());
        //err = false;
    }
    public boolean hasError()
    {
        if(err)
        {
            err = false;
            return true;
        }
        else
        {
            err = false;
            return false;
        }
    }
    //getters 
    public String getWarning()
    {        
        return warns;
    }
    public String getErrores()
    {        
        return errores;
    }
    public String getFatalError()
    {        
        return fatalErr;
    }
}
class Disco {
    private String titulo = "";
	private String iDD = "";
    private String interprete = ""; 
    private String idiomas = "";
    public Disco(String atributoUno, String atributoDos, String atributoTres, String atributoCuatro) 
    {
        titulo = atributoUno;
        interprete = atributoDos;
        iDD = atributoTres;
        idiomas = atributoCuatro;
    }
    public String getTitulo(Disco d){        
        return d.titulo;
    }
    
    public String getIDD(Disco d){        
        return d.iDD;
    }
    public String getInterprete(Disco d){
        return d.interprete;
    }
    public String getIdiomas(Disco d){        
        return d.idiomas;
    }
}
class Cancion {
    private String titulo = "";
	private String iDC = "";
    private String genero = ""; 
    private String duracion = "";
    private String desc = "";
    //private ArrayList<String> premios = new ArrayList<String>();

    public Cancion(String atributoUno, String atributoDos, String atributoTres, String atributoCuatro, String atributoCinco/*ArrayList<String> atributoCinco*/) 
    {
        titulo = atributoUno;
        genero = atributoDos;
        duracion = atributoTres;
        iDC = atributoCuatro;
        desc = atributoCinco;
        /*for (int i = 0;i<atributoCinco.size();i++)
        {
            premios.add(atributoCinco.get(i));
        }*/
        
    }
    public String getTitulo(Cancion c){        
        return c.titulo;
    }    
    public String getGenero(Cancion c){
        return c.genero;
    }
    public String getDuracion(Cancion c){        
        return c.duracion;
    }
    public String getIdc(Cancion c){        
        return c.iDC;
    }
    public String getDescripcion(Cancion c){        
        return c.desc;
    }
    /*public ArrayList<String> getPremios(Cancion c){      
        return c.premios;
    }*/
}






