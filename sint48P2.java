package p2;
//--------------------------------------------------IMPORTS---------------------------------------------------------------------
import java.io.*;
//Servlets
import javax.servlet.*;
import javax.servlet.http.*;
//Estructuras de datos
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.*;
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




//---------------------------------------------------CLASE SINT48P2--------------------------------------------------------------
public class Sint48P2 extends HttpServlet 
{   
    //------------------------------------------------DECLARACIONES--------------------------------------------------------------- 
	//Atributes Schema
	static String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    static String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    static String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    //static String MY_SCHEMA = "iml.xsd"; //En mi ordenador este archivo y los xml están en bin. Esto es una chapuza
    //static String MY_SCHEMA = "iml.xsd";
    public static Error error=null; //Objeto error
    public static String url;
	//Declaración de estructuras de datos
	public static HashMap<String,Document> mapDocs = new HashMap<String,Document>();	
    public  ArrayList<String>Anios = new ArrayList<String>();
    public  ArrayList<Disco>listaDiscos = new ArrayList<Disco>();
    public  ArrayList<Cancion>listaCanciones = new ArrayList<Cancion>();
    public  ArrayList<Cancion>Resultado = new ArrayList<Cancion>();
    //public static ArrayList<String>fichErroneos = new ArrayList<String>();
    public static ArrayList<String>listaErrores= new ArrayList<String>();
    public static ArrayList<String>listaEFatales = new ArrayList<String>();
    public static ArrayList<String>listaWarnings = new ArrayList<String>();
    public static ArrayList<String>urlErrores= new ArrayList<String>();
    public static ArrayList<String>urlEFatales = new ArrayList<String>();
    public static ArrayList<String>urlWarnings = new ArrayList<String>();

//------------------------------------------------------------SERVLET.INIT------------------------------------------------------------------------        
    public void init(ServletConfig config) throws ServletException
    {
        ServletContext context = config.getServletContext();
        File f= new File(context.getRealPath("iml.xsd"));
        String dir = f.getAbsolutePath();               
        //String[] parts = dir.split("/");
        //String MY_SCHEMA = parts[parts.length-1];
        String MY_SCHEMA = dir;      
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
        ArrayList<String> leidos = new ArrayList<String>();    //Era HashSet            
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
        }catch(ParserConfigurationException e)
        {
            e.printStackTrace();
        }        
        //listaFicheros.add("iml2001.xml"); //Archivo xml a parsear con el schema        
        listaFicheros.add("http://gssi.det.uvigo.es/users/agil/public_html/SINT/18-19/iml2001.xml");
	    while(!listaFicheros.isEmpty())
        {
            url = (String) listaFicheros.getFirst();        
            if(!leidos.contains(url))
            {
                try
                {
                    //Generacion del arbol DOM tras el parseo. Generará un error el método parse si el documento no  es well-formed. 				Una SAXException
                //Saltará la clase de gestión de errores en caso de que los contenga (no válido según el schema xml 				definido).            
                    doc = db.parse(url);
                    leidos.add(url);                    
                }catch(SAXException e)
                {
                    e.printStackTrace();                    
                }catch(IOException e)
                {
                    e.printStackTrace();
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                if(error.hasError())
                {                    
                    if(!error.getWarning().equals(""))
                    {                        
                        listaWarnings.add(error.getWarning());
                        urlWarnings.add(leidos.get(leidos.size()-1));
                    }
                    if(!error.getErrores().equals(""))
                    {                        
                        listaErrores.add(error.getErrores());
                        urlErrores.add(leidos.get(leidos.size()-1));
                    }
                    if(!error.getFatalError().equals(""))
                    {                        
                        listaEFatales.add(error.getFatalError());
                        urlEFatales.add(leidos.get(leidos.size()-1));
                    }
                }//if hasError()
                else
                {                    		    
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
                    }
                }//else hasError
            }//if leidos containsURL
            listaFicheros.removeFirst();            
        }///While true		
    }//Init
    
//-------------------------------------------------------------SERVLET.DOGET()--------------------------------------------------------------    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {        
        res.setContentType("text/html;charset=utf-8"); 
		req.setCharacterEncoding("UTF8");

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
            String faseinicial = "01";
            if((fase==null) || (fase.equals(faseinicial)))
            {
                doGetFase01(res,auto);
            }
            else
            {
		        switch(fase)
                {                        
                    //case "01": doGetFase01(res,auto); break;
                    case "02": doGetFase02(res,auto); break;
                    case "11": doGetFase11(res,auto); break;
                    case "12": doGetFase12(res,auto,anio); break;
                    case "13": doGetFase13(res,auto,anio,idd); break;
                    case "14": doGetFase14(res,auto,anio,idd,idc); break;
                }
            }
	    }        
    }//doGet
    
//---------------------------------------------------------FUNCTIONS------------------------------------------------------------------------
    public void doGetFase01(HttpServletResponse res, String auto)throws IOException
    {
        if(auto==null)
        {
            doHtmlF01(res);                
        }
        else if(auto.equals("si"))
        {
            doXmlF01(res);
        }        
    }//doHtmlF01
    public void doHtmlF01(HttpServletResponse res)throws IOException
    {                            
        PrintWriter out = res.getWriter();                
        out.println("<html>");
        out.println("<head>");        
        out.println("<meta charset='utf-8'></meta>");
        out.println("<title>Sint: Pr&aacute;ctica 2. Consulta de canciones</title>");    
        out.println("<link rel='stylesheet' type='text/css' href='iml.css'></link>"); //href='iml.css' en el lab
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servicio de consulta de canciones</h1>");
        out.println("<h2>Bienvenido a este servicio.</h2>");                                 
        out.println("<form name = 'miformfase01' action=''>");
        out.println("<a href = '/sint48/P2IM?p=d4r18c392b&pfase=02'>Pulsa aqu&iacute; para ver los ficheros err&oacute;neos</a>");
        out.println("<h3>Selecciona una consulta:</h3>");
        out.println("<input type = 'radio' checked>Consulta1: Canciones de un int&eacute;rprete que duran menos que una dada</input>");
        out.println("<br></br>");
        out.println("<input type = 'submit' class = 'buttonSubmit'></input>");
        out.println("<input type = 'hidden' name = 'p' value = d4r18c392b></input>");
        out.println("<input type = 'hidden' name = 'pfase' value = '11'></input>");	
        out.println("</form>");
        out.println("</body>");
        out.println("<footer>");
        out.println("<p>sint48. @Diego R&iacute;os Castro.</p>");                
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
    }//doXmlF01

    public void doGetFase02(HttpServletResponse res, String auto)throws IOException
    {
        if(auto==null)
        {
            doHtmlF02(res);                
        }
        else if(auto.equals("si"))
        {
            doXmlF02(res);
        }         
    }//doGetFase02

    public void doHtmlF02(HttpServletResponse res)throws IOException
    {	               
        PrintWriter out = res.getWriter();
        int numError = listaErrores.size();
        int numEFatal = listaEFatales.size();
        int numWarning = listaWarnings.size();    

        out.println("<html>");
        out.println("<head>");    
        out.println("<meta charset='utf-8'></meta>");
        out.println("<title>Sint: Pr&aacute;ctica 2. Consulta de canciones</title>");
	    out.println("<link rel='stylesheet' type='text/css' href='iml.css'></link>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servicio de consulta de canciones</h1>");	    
        out.println("<h2>Se han encontrado "+numWarning+" ficheros con warnings.</h2>");        
        for (int i=0;i<numWarning;i++)
	    {
	        out.println("<p>"+listaWarnings.get(i)+"</p>");
	    }        	
        out.println("<h2>Se han encontrado "+numError+" ficheros con errores</h2>");               
        for (int i=0;i<listaErrores.size();i++)
	    {
	        out.println("<p>"+listaErrores.get(i)+"</p>");
        }        	              
        out.println("<h2>Se han encontrado "+numEFatal+" ficheros con errores fatales</h2>");       
        for (int i=0;i<numEFatal;i++)
	    {
	        out.println("<p>"+listaEFatales.get(i)+"</p>");
	    }         	              
        out.println("<button class = 'buttonAtras'onclick=\"window.location='/sint48/P2IM?p=d4r18c392b&pfase=01'\">Atr&aacute;s</button>");
        out.println("</body>");
        out.println("<footer>");
        out.println("<p>sint48. @Diego R&iacute;os Castro.</p>");                
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
            out.println("<file>"+urlWarnings.get(i)+"</file>");
            out.println("<cause>"+listaWarnings.get(i)+"</cause>");
            out.println("</warning>");
        }   
        out.println("</warnings>");
        out.println("<errors>");        
        for(int i=0;i<listaErrores.size();i++)
        {
            out.println("<error>");
            out.println("<file>"+urlErrores.get(i)+"</file>");
            out.println("<cause>"+listaErrores.get(i)+"</cause>");
            out.println("</error>");
        }        
        out.println("</errors>");
        out.println("<fatalerrors>");
        for(int i =0;i<listaEFatales.size();i++)
        {
            out.println("<fatalerror>");
            out.println("<file>"+urlEFatales.get(i)+"</file>");
            out.println("<cause>"+listaEFatales.get(i)+"</cause>");
            out.println("</fatalerror>");
        }      
        out.println("</fatalerrors>");
        out.println("</errores>");
    }//doXmlF02

    public void doGetFase11(HttpServletResponse res, String auto)throws IOException
    {
        Anios.clear();        
        Anios = getC1Anios();
        Collections.sort(Anios);
        if(auto==null)
        {
            doHtmlF11(res,Anios);                
        }
        else if(auto.equals("si"))
        {
            doXmlF11(res,Anios);
        }        
    }//doGetFase11

    public void doGetFase12(HttpServletResponse res, String auto, String anio)throws IOException
    {        
        listaDiscos.clear();
        ArrayList<String> interpretes = new ArrayList<String>();                
        listaDiscos = getC1Discos(anio);
        for(int i=0;i<listaDiscos.size();i++)
        {
            interpretes.add(listaDiscos.get(i).interprete); 
        }
        Collections.sort(interpretes);        
        for(int j = 0;j<listaDiscos.size();j++)
        {
            Disco obj = listaDiscos.get(j);
            String inter = obj.interprete;
            int indice = interpretes.indexOf(inter);
            listaDiscos.remove(obj);
            listaDiscos.add(indice, obj);
        }
        if(auto==null)
        {
            doHtmlF12(res,anio, listaDiscos);                
        }
        else if(auto.equals("si"))
        {
            doXmlF12(res,anio,listaDiscos);
        }       
    }//doGetFase12

    public void doGetFase13(HttpServletResponse res, String auto, String anio, String idd)throws IOException
    {
        listaCanciones.clear();
        ArrayList<Integer> dur = new ArrayList<Integer>();        
        listaCanciones = getC1Canciones(anio, idd);
        for(int i=0;i<listaCanciones.size();i++)
        {
            dur.add(Integer.parseInt(listaCanciones.get(i).duracion)); 
        }
        Collections.sort(dur);        
        for(int j = 0;j<listaCanciones.size();j++)
        {
            Cancion obj = listaCanciones.get(j);
            String dura = obj.duracion;
            int indice = dur.indexOf(Integer.parseInt(dura));
            listaCanciones.remove(obj);
            listaCanciones.add(indice, obj);
        }
        if(auto==null)
        {
            doHtmlF13(res,anio, idd, listaCanciones);                
        }
        else if(auto.equals("si"))
        {
            doXmlF13(res,idd,listaCanciones);
        }       
    }//doGetFase13

    public void doGetFase14(HttpServletResponse res, String auto, String anio, String idd, String idc)throws IOException
    {   
        Resultado.clear();
        ArrayList<String> titulos = new ArrayList<String>();      
        Resultado = getC1Resultado(anio, idd, idc);
        for(int i=0;i<Resultado.size();i++)
        {
            titulos.add(Resultado.get(i).titulo); 
        }
        Comparator<String> comparador = Collections.reverseOrder();
        Collections.sort(titulos, comparador);        
        for(int j = 0;j<Resultado.size();j++)
        {
            Cancion obj = Resultado.get(j);
            String tit = obj.titulo;
            int indice = titulos.indexOf(tit);
            Resultado.remove(obj);
            Resultado.add(indice, obj);
        }        
        if(auto==null)
        {
            doHtmlF14(res,anio, idd, idc, Resultado);                
        }
        else if(auto.equals("si"))
        {
            doXmlF14(res,idc,Resultado);
        }       
    }//doGetFase14     
    
    public ArrayList<String> getC1Anios()
    {	
        for(String key:mapDocs.keySet())
        {
            Anios.add(key);
        }	
	    return Anios;    
    }//getC1Anios

    public ArrayList<Disco> getC1Discos (String anio) //Type Disco
    {
        Document res = null;
        String atributoUno = null;
        String atributoDos = null;
        String atributoTres = null;
        String atributoCuatro = null;
        String idiomaPais = null;
        Integer resComp = 0;
        Integer resCompMem = 0;        
        for (String key:mapDocs.keySet())
        {
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
                    else
                    {
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
                    Disco obj = new Disco(atributoUno,atributoDos,atributoTres, atributoCuatro);
                    listaDiscos.add(obj);                                          
                }                                 
            }              
        }
        return listaDiscos;
    }//GetC1Discos

    public ArrayList<Cancion> getC1Canciones (String anio, String idd) //Type Cancion
    {
        Document res = null;
        String atributoUno = null;
        String atributoDos = null;
        String atributoTres = null;
        String atributoCuatro = null;
        String atributoCinco = null;
        String atributoSeis = "";    
        for (String key:mapDocs.keySet())
        {
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
                listaCanciones.add(new Cancion(atributoUno,atributoDos,atributoTres, atributoCuatro, atributoCinco, atributoSeis));
            }
        }
        return listaCanciones;
    }//getC1Canciones

    public ArrayList<Cancion> getC1Resultado (String anio, String idd, String idc) //Todas las canciones de un interprete que duren menos que una elegida
    {
        String dur = null;
        String interprete = null;
        String atributoUno = null;
        String atributoDos = null;
        String atributoTres = null;
        String atributoCuatro = null;
        String atributoCinco = null;
        String atributoSeis = "";
        String nodosText = "";
        Boolean isFirst = false;       
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
        for (String key:mapDocs.keySet())
        {
            res = mapDocs.get(key);    
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
                        String nameInt = childDiscos.item(j).getTextContent();
                        if(nameInt.equals(interprete))
                        {
                            flag = true;                        
                        }
                        if(flag)
                        {
                            Node parentInterprete = childDiscos.item(j).getParentNode();
                            NodeList hijosDisco = parentInterprete.getChildNodes();
                            for (int x = 0; x<hijosDisco.getLength();x++)
                            {
                                Node hijoPremios = hijosDisco.item(x);
                                if(hijoPremios.getNodeName().equals("Premios"))
                                {                            
                                    NodeList hayPremios = hijoPremios.getChildNodes();
                                    for (int m = 0;m<hayPremios.getLength();m++)
                                    {
                                        Node premio = hayPremios.item(m);
                                        if((premio.getNodeName().equals("Premio")) && (m==0))
                                        {
                                            atributoSeis = premio.getTextContent();
                                            //atributoSeis = "blank";
                                        }
                                        else if((premio.getNodeName().equals("Premio")) && (m!=0))
                                        {
                                            atributoSeis = atributoSeis+" "+premio.getTextContent();
                                            //atributoSeis = "blank";
                                        }
                                    }
                                }
                            }
                        }                                         
                    }
                    if(childDiscos.item(j).getNodeName().equals("Cancion") && flag)
                    {                   
                        Node itemcancion = childDiscos.item(j);
                        atributoCuatro=itemcancion.getAttributes().getNamedItem("idc").getTextContent();
                        NodeList childCancion = itemcancion.getChildNodes();                   
                        for(int k = 0;k<childCancion.getLength(); k++)
                        {                                                   
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
                            if(childCancion.item(k).getNodeType() == org.w3c.dom.Node.TEXT_NODE)
                            {                                
                                if(!isFirst)
                                {
                                    nodosText = childCancion.item(k).getNodeValue();
                                    isFirst = true;
                                }
                                else
                                {
                                    nodosText = nodosText.concat(childCancion.item(k).getNodeValue());
                                }                                
                            }                                                                                                                       
                        }
                        atributoCinco = nodosText.trim();
                        listares.add(new Cancion(atributoUno,atributoDos,atributoTres, atributoCuatro, atributoCinco, atributoSeis));
                        isFirst = false;                                         
                    }                
                }//Cierra for recorrer hijos Disco
                flag = false;
            }//Cierra for recorrer nodos Disco               
        }//Cierra for recorrer hashmap mapDocs

        //Recorre listares y obtener las duraciones comparando cada una con la elegida. Si es menor, meterlo en la lista resultado
        for (int i = 0;i<listares.size();i++)
        {
            Cancion obj = listares.get(i);
            int durobj = Integer.parseInt(obj.getDuracion(obj));
            if(durobj < Integer.parseInt(dur))
            {
                Resultado.add(obj);                
            }
        }
        return Resultado;        
    }//getC1Resultado

    public void doHtmlF11(HttpServletResponse res, ArrayList<String> Anios)throws IOException
    {                              
        PrintWriter out = res.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='utf-8'></meta>");
        out.println("<title>Sint: Pr&aacute;ctica 2. Consulta de canciones</title>");
	    out.println("<link rel='stylesheet' type='text/css' href='iml.css'></link>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servicio de consulta de canciones</h1>");
        out.println("<h2>Consulta 1</h2>");    
        out.println("<h3>Selecciona un a&ntilde;o:</h3>");
        out.println("<form name = 'miformfase11'>");
	    out.println("<input type = 'hidden' name = 'p' value = 'd4r18c392b'></input>");    
        out.println("<input type = 'hidden' name = 'pfase' value = '12'></input>");        
        for(int i=0;i<Anios.size();i++)
        {
            if(i==0)
            {
                out.println("<p><input type = 'radio' name = 'panio' value = "+Anios.get(i)+" checked>"+Integer.toString(i+1)+".- "+Anios.get(i)+"</input></p>");
            }
            else out.println("<p><input type = 'radio' name = 'panio' value = "+Anios.get(i)+">"+Integer.toString(i+1)+".- "+Anios.get(i)+"</input></p>");            
        }       
        out.println("<br></br>");        
        out.println("<input type = 'submit' class = 'buttonSubmit'></input>");            
        out.println("</form>");
        out.println("<button type = 'button' class = 'buttonAtras' onclick=\"window.location='/sint48/P2IM?p=d4r18c392b&pfase=01'\">Atr&aacute;s</button> ");
        out.println("</body>");
        out.println("<footer>");
        out.println("<p>sint48. @Diego R&iacute;os Castro.</p>");                
        out.println("</footer>");
        out.println("</html>");
    }//doHtmlF11

    public void doXmlF11(HttpServletResponse res,ArrayList<String> Anios)throws IOException
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
    }//doXmlF11

    public void doHtmlF12(HttpServletResponse res, String anio, ArrayList<Disco> listaDiscos)throws IOException
    {                              
        PrintWriter out = res.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Sint: Pr&aacute;ctica 2. Consulta de canciones</title>");
        out.println("<meta charset='utf-8'></meta>");
	    out.println("<link rel='stylesheet' type='text/css' href='iml.css'></link>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servicio de consulta de canciones</h1>");    
        out.println("<h2>Consulta 1: A&ntilde;o="+anio+"</h2>");    
        out.println("<h3>Selecciona un disco:</h3>");
        out.println("<form name = 'miformfase12'>");
	    out.println("<input type = 'hidden' name = 'p' value = 'd4r18c392b'></input>");
        out.println("<input type = 'hidden' name = 'pfase' value = '13'></input>");	
        out.println("<input type = 'hidden' name = 'panio' value ='"+anio+"'></input>");        
        for(int i=0;i<listaDiscos.size();i++)
        {
            Disco d = listaDiscos.get(i);
            if(i==0)
            {
                out.println("<p><input type = 'radio' name = 'pidd' value = "+d.getIDD(d)+" checked>"+Integer.toString(i+1)+".-"+" T&iacute;tulo ='"+d.getTitulo(d)+"' --- IDD ='"+d.getIDD(d)+"' --- Int&eacute;rprete ='"+d.getInterprete(d)+"' --- Idiomas ='"+d.getIdiomas(d)+"'</input></p>");
            }
            else out.println("<p><input type = 'radio' name = 'pidd' value = "+d.getIDD(d)+">"+Integer.toString(i+1)+".-"+" T&iacute;tulo ='"+d.getTitulo(d)+"' --- IDD ='"+d.getIDD(d)+"' --- Int&eacute;rprete ='"+d.getInterprete(d)+"' --- Idiomas ='"+d.getIdiomas(d)+"'</input></p>");        
        }       
        out.println("<br></br>");    
        out.println("<input type = 'submit' class = 'buttonSubmit'></input>");
        out.println("</form>");
        out.println("<button class = 'buttonAtras' onclick=\"window.location='/sint48/P2IM?p=d4r18c392b&pfase=11'\">Atr&aacute;s</button> ");
        out.println("<br></br>");
        out.println("<button class = 'buttonInicio' onclick=\"window.location='/sint48/P2IM?p=d4r18c392b&pfase=01'\">Inicio</button> ");
        out.println("</body>");
        out.println("<footer>");
        out.println("<p>sint48. @Diego R&iacute;os Castro.</p>");                
        out.println("</footer>");
        out.println("</html>");
    }//doHtamlF12

    public void doXmlF12(HttpServletResponse res,String anio, ArrayList<Disco> listaDiscos)throws IOException
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
                out.println("<disco idd='"+d.getIDD(d)+"' interprete='"+d.getInterprete(d)+"' langs='"+d.getIdiomas(d)+"'>"+d.getTitulo(d)+"</disco>");
            }
            out.println("</discos>");                            
        }
    }//doXmlF12

    public void doHtmlF13(HttpServletResponse res, String anio, String idd, ArrayList<Cancion> listaCanciones)throws IOException
    {                              
        PrintWriter out = res.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Sint: Pr&aacute;ctica 2. Consulta de canciones</title>");
        out.println("<meta charset='utf-8'></meta>");
	    out.println("<link rel='stylesheet' type='text/css' href='iml.css'></link>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servicio de consulta de canciones</h1>");    
        out.println("<h2>Consulta 1: A&ntilde;o="+anio+", Disco="+idd+"</h2>");
        out.println("<form name = 'miformfase13'>");    
        out.println("<h3>Selecciona una canci&oacute;n:</h3>");
	    out.println("<input type = 'hidden' name = 'p' value = 'd4r18c392b'></input>");
        out.println("<input type = 'hidden' name = 'pfase' value = '14'></input>");	
        out.println("<input type = 'hidden' name = 'panio' value = '"+anio+"'></input>");
        out.println("<input type = 'hidden' name = 'pidd' value = '"+idd+"'></input>");        
        for(int i=0;i<listaCanciones.size();i++)
        {
            Cancion c = listaCanciones.get(i);
            if(i==0)
            {
                out.println("<p><input type = 'radio' name = 'pidc' value = "+c.getIdc(c)+" checked>"+Integer.toString(i+1)+".-"+" T&iacute;tulo ='"+c.getTitulo(c)+"' --- IDC ='"+c.getIdc(c)+"' --- G&eacutenero ='"+c.getGenero(c)+"' --- Duraci&oacute;n ='"+c.getDuracion(c)+" seg.'</input></p>");
            }
            else out.println("<p><input type = 'radio' name = 'pidc' value = "+c.getIdc(c)+">"+Integer.toString(i+1)+".-"+" T&iacute;tulo ='"+c.getTitulo(c)+"' --- IDC ='"+c.getIdc(c)+"' --- G&eacute;nero ='"+c.getGenero(c)+"' --- Duraci&oacute;n ='"+c.getDuracion(c)+" seg.'</input></p>");        
        }       
        out.println("<br></br>");    
        out.println("<input type = 'submit' class = 'buttonSubmit'></input>");
        out.println("</form>");
        out.println("<button class = 'buttonAtras' onclick=\"window.location='/sint48/P2IM?p=d4r18c392b&pfase=12&panio="+anio+"'\">Atr&aacute;s</button> ");
        out.println("<br></br>");    
        out.println("<button class = 'buttonInicio' onclick=\"window.location='/sint48/P2IM?p=d4r18c392b&pfase=01'\">Inicio</button> ");
        out.println("</body>");
        out.println("<footer>");
        out.println("<p>sint48. @Diego R&iacute;os Castro.</p>");                
        out.println("</footer>");
        out.println("</html>");
    }//doHtmlF13

    public void doXmlF13(HttpServletResponse res,String idd, ArrayList<Cancion> listaCanciones)throws IOException
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
                out.println("<cancion idc='"+c.getIdc(c)+"' genero='"+c.getGenero(c)+"' duracion='"+c.getDuracion(c)+"'>"+c.getTitulo(c)+"</cancion>");
            }
            out.println("</canciones>");                                         
        }            
    }//doXmlF13

    public void doHtmlF14(HttpServletResponse res, String anio, String idd, String idc, ArrayList<Cancion> Resultado)throws IOException
    {                              
        PrintWriter out = res.getWriter();        
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Sint: Pr&aacute;ctica 2. Consulta de canciones</title>");
        out.println("<meta charset='utf-8'></meta>");
        out.println("<link rel='stylesheet' type='text/css' href='iml.css'></link>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servicio de consulta de canciones</h1>");
        out.println("<form name = 'miformfase14'>");
        out.println("<h2>Consulta 1: A&ntilde;o="+anio+", Disco="+idd+", Canci&oacute;n="+idc+"</h2>");    
        out.println("<h3>Este es el resultado:</h3>");
        for(int i=0;i<Resultado.size();i++)
        {
            Cancion obj = Resultado.get(i);           
            out.println("<p>"+Integer.toString(i+1)+".-"+" T&iacute;tulo = '"+obj.getTitulo(obj)+"' --- Descripci&oacute;n = '"+obj.getDescripcion(obj)+"' --- Premios = '"+obj.getPremios(obj)+"'</p>");        
        }        
        out.println("</form>");
        out.println("<button class = 'buttonAtras'  onclick=\"window.location='/sint48/P2IM?p=d4r18c392b&pfase=13&panio="+anio+"&pidd="+idd+"'\">Atr&aacute;s</button> ");
        out.println("<br></br>");
        out.println("<button class = 'buttonInicio' onclick=\"window.location='/sint48/P2IM?p=d4r18c392b&pfase=01'\">Inicio</button> ");
        out.println("</body>");
        out.println("<footer>");
        out.println("<p>sint48. @Diego R&iacute;os Castro.</p>");                
        out.println("</footer>");
        out.println("</html>"); 
    }//doHtmlF14

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
                out.println("<cancion descripcion='"+obj.getDescripcion(obj)+"' premios='"+obj.getPremios(obj)+"'>"+obj.getTitulo(obj)+"</cancion>");
            }
            out.println("</canciones>");                                                       
        }        
    }//doXmlF14

    public void doXmlNop(HttpServletResponse res)throws IOException
    {
        res.setContentType("text/xml");
        PrintWriter out = res.getWriter();
        out.println("<?xml version='1.0' encoding='utf-8' ?>");
        out.println("<wrongRequest>no passwd</wrongRequest>");        
    }//doXmlNop

    public void doXmlIp(HttpServletResponse res)throws IOException
    {
        res.setContentType("text/xml");
        PrintWriter out = res.getWriter();
        out.println("<?xml version='1.0' encoding='utf-8' ?>");
        out.println("<wrongRequest>bad passwd</wrongRequest>");        
    }//doXmlIp
}//Fin SINT48P2

//-----------------------------------------------------------------ERROR CLASS-----------------------------------------------------------------------
class Error extends DefaultHandler 
{
    //Atributos
    private boolean err = false;
    private String warns = "";
	private String errores = "";
    private String fatalErr = "";

    //Constructor    
    public Error () {} 

    //Metodos
    public void warning (SAXParseException spe) throws SAXException 
    {        
        err = true;
        warns = "Warning: "+spe.toString(); 
        System.out.println("Warning: "+spe.toString());         
    }

    public void error (SAXParseException spe) throws SAXException 
    {         
        err = true;
        errores = "Error: "+spe.toString();
        System.out.println("Error: "+spe.toString());         
    }

    public void fatalerror (SAXParseException spe) throws SAXException 
    {        
        err = true;
        fatalErr = "Fatal Error: "+spe.toString();
        System.out.println("Fatal Error: "+spe.toString());        
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
}//Fin clase errores

//---------------------------------------------------------------------CLASE DISCO-----------------------------------------------------------------
class Disco
{
    //Atributos
    private String titulo = "";
	private String iDD = "";
    public String interprete = ""; 
    private String idiomas = "";

    //Constructor
    public Disco(String atributoUno, String atributoDos, String atributoTres, String atributoCuatro) 
    {
        titulo = atributoUno;
        interprete = atributoDos;
        iDD = atributoTres;
        idiomas = atributoCuatro;
    }

    //Métodos
    public String getTitulo(Disco d)
    {        
        return d.titulo;
    }
    
    public String getIDD(Disco d)
    {        
        return d.iDD;
    }

    public String getInterprete(Disco d)
    {
        return d.interprete;
    }

    public String getIdiomas(Disco d)
    {        
        return d.idiomas;
    }    
}//Fin clase Disco

//------------------------------------------------------------------------CLASE CANCION-------------------------------------------
class Cancion 
{
    //Atributos
    public String titulo = "";
	private String iDC = "";
    private String genero = ""; 
    public String duracion = "";
    private String desc = "";
    private String premios = "";

    //Constructor
    public Cancion(String atributoUno, String atributoDos, String atributoTres, String atributoCuatro, String atributoCinco, String atributoSeis) 
    {
        titulo = atributoUno;
        genero = atributoDos;
        duracion = atributoTres;
        iDC = atributoCuatro;
        desc = atributoCinco;
        premios = atributoSeis;        
    }

    //Métodos
    public String getTitulo(Cancion c)
    {        
        return c.titulo;
    }    
    public String getGenero(Cancion c)
    {
        return c.genero;
    }
    public String getDuracion(Cancion c)
    {        
        return c.duracion;
    }
    public String getIdc(Cancion c)
    {        
        return c.iDC;
    }
    public String getDescripcion(Cancion c)
    {        
        return c.desc;
    }
    public String getPremios(Cancion c)
    {      
        return c.premios;
    }
}//fin clase Cancion
//-------------------------------------------------------------------------------END--------------------------------------------------------------------