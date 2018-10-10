import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.annotation.*;
import java.util.Map;
import java.util.HashMap;
@WebServlet("/P2IM")
public class Sint48P2 extends HttpServlet {
        ArrayList<String>Anios = new ArrayList<String>();
        ArrayList<String>Discos = new ArrayList<String>();
        ArrayList<String>Canciones = new ArrayList<String>();
        ArrayList<String>Resultado = new ArrayList<String>();
        public static ArrayList<String>fichErroneos = new ArrayList<String>();       
        
    public void init(ServletConfig config) throws ServletException
    {
                String[] fich = {"fichero1","fichero2","fichero3","fichero4","fichero5"};
                ArrayList<String>ficheros = new ArrayList<String>(Arrays.asList(fich));
                fichErroneos.add(0, fich[0]);
                fichErroneos.add(1, fich[2]); 
                fichErroneos.add(2, fich[3]);               
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        res.setContentType("text/html ; charset='utf-8'");
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
        /*if(!p.equals("d4r18c392b"))
	{
		out.println("<html>");
		out.println("<head>");
		out.println("<meta charset=utf-8'></meta>");
		out.println("<title>Sint: Práctica 2. Consulta de canciones</title>");    
		out.println("<link rel='stylesheet' href='/styles/iml.css'></link>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>La password introducida es erronea</h1>");
                out.println("</body>");
                out.println("<footer>");
                out.println("<p>sint48. @Diego Rios Castro.</p>");                
                out.println("</footer>"); 
		out.println("</html>");	    		
	}*/
	else
	{
		switch(fase)
        	{                        
            	case "01": doGetFase01(out,auto,res); break;
            	case "02": doGetFase02(out,auto,fichErroneos,res); break;
            	case "11": doGetFase11(out,auto,res, Anios); break;
            	case "12": doGetFase12(out,auto,res,anio, Discos); break;
            	case "13": doGetFase13(out,auto,res,anio,idd, Canciones); break;
            	case "14": doGetFase14(out,auto,res,anio,idd,idc, Resultado); break;
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
        out.println("<link rel='stylesheet' type='text/css' href='iml.css'></link>");
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
    public void doGetFase02(PrintWriter out, String auto, ArrayList fichErroneos, HttpServletResponse res)throws IOException
    {
        if(auto==null)
        {
            doHtmlF02(out, fichErroneos);                
        }
        else if(auto.equals("si"))
        {
            doXmlF02(res, fichErroneos);
        }         
    }//doGetFase02
    public void doHtmlF02(PrintWriter out, ArrayList fichErroneos)
    {
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=utf-8'></meta>");
        out.println("<title>Sint: Práctica 2. Consulta de canciones</title>");
	out.println("<link rel='stylesheet' type='text/css' href='iml.css'></link>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servicio de consulta de canciones</h1>");
        out.println("<h2>Se han encontrado 2 ficheros con warnings.</h2>");
        out.println("<p>Warning</p>");
        out.println("<p>Warning</p>");
        out.println("<h2>Se han encontrado 4 ficheros con errores</h2>");
        for(int i=0;i<fichErroneos.size();i++)
        {
        out.println("<p>"+fichErroneos.get(i)+"</p>");
        }
        /*out.println("<p>Error</p>");
        out.println("<p>Error</p>");    
        out.println("<p>Error</p>");    
        out.println("<p>Error</p>");*/
        out.println("<h2>Se han encontrado 2 ficheros con errores fatales</h2>");
        out.println("<p>Error</p>");
        out.println("<p>Error</p>");        
        out.println("<button class = 'buttonAtras'onclick=\"window.location='/sint48/P2IM?p=d4r18c392b&pfase=01'\">Atras</button>");
        out.println("</body>");
        out.println("<footer>");
        out.println("<p>sint48. @Diego Rios Castro.</p>");                
        out.println("</footer>");
        out.println("</html>");
    }//doHtmlF02
    public void doXmlF02(HttpServletResponse res, ArrayList fichErroneos)throws IOException
    {    
        res.setContentType("text/xml");
        PrintWriter out = res.getWriter();
        out.println("<?xml version='1.0' encoding='utf-8' ?>");
        out.println("<errores>");
        out.println("<warnings>");
        out.println("<warning>");
        out.println("<file>URL del fichero que provoca el warning</file>");
        out.println("<cause>Explicación propia o proporcionada por el parser</cause>");
        out.println("</warning>");
        out.println("<warning>");
        out.println("<file>URL del fichero que provoca el warning</file>");
        out.println("<cause>Explicación propia o proporcionada por el parser</cause>");
        out.println("</warning>");
        out.println("</warnings>");
        out.println("<errors>");
        
        for(int i=0;i<fichErroneos.size();i++)
        {
        out.println("<error>");
        out.println("<file>URL del fichero que provoca el error"+fichErroneos.get(i)+"</file>");
        out.println("<cause>Explicación propia o proporcionada por el parser</cause>");
        out.println("</error>");
        }        
        /*out.println("<error>");
        out.println("<file>URL del fichero que provoca el error</file>");
        out.println("<cause>Explicación propia o proporcionada por el parser</cause>");
        out.println("</error>");
        out.println("<error>");
        out.println("<file>URL del fichero que provoca el error</file>");
        out.println("<cause>Explicación propia o proporcionada por el parser</cause>");
        out.println("</error>");
        out.println("<error>");
        out.println("<file>URL del fichero que provoca el error</file>");
        out.println("<cause>Explicación propia o proporcionada por el parser</cause>");
        out.println("</error>");*/
        out.println("</errors>");
        out.println("<fatalerrors>");
        out.println("<fatalerror>");
        out.println("<file>URL del fichero que provoca el fatal error</file>");
        out.println("<cause>Explicación propia o proporcionada por el parser</cause>");
        out.println("</fatalerror>");
        out.println("<fatalerror>");
        out.println("<file>URL del fichero que provoca el fatal error</file>");
        out.println("<cause>Explicación propia o proporcionada por el parser</cause>");
        out.println("</fatalerror>");
        out.println("</fatalerrors>");
        out.println("</errores>");
    }
    public void doGetFase11(PrintWriter out, String auto,HttpServletResponse res, ArrayList Anios)throws IOException
    {        
        Anios = getC1Anios();
        if(auto==null)
        {
            doHtmlF11(out,Anios);                
        }
        else if(auto.equals("si"))
        {
            doXmlF11(res,Anios);
        }        
    }//doGetFase11
    public void doGetFase12(PrintWriter out,String auto, HttpServletResponse res, String anio, ArrayList Discos)throws IOException
    {        
        Discos = getC1Discos(anio);
        if(auto==null)
        {
             doHtmlF12(out,anio, Discos);                
        }
        else if(auto.equals("si"))
        {
            doXmlF12(res,Discos);
        }
        
    }//doGetFase12
    public void doGetFase13(PrintWriter out,String auto,HttpServletResponse res, String anio, String idd, ArrayList Canciones)throws IOException
    {        
        Canciones = getC1Canciones(anio, idd);
        if(auto==null)
        {
              doHtmlF13(out,anio, idd, Canciones);                
        }
        else if(auto.equals("si"))
        {
            doXmlF13(res,Canciones);
        }
        
    }//doGetFase13
    public void doGetFase14(PrintWriter out, String auto, HttpServletResponse res, String anio, String idd, String idc, ArrayList Resultado)throws IOException
    {        
        Resultado = getC1Resultado(anio, idd, idc);
        if(auto==null)
        {
                doHtmlF14(out,anio, idd, idc, Resultado);                
        }
        else if(auto.equals("si"))
        {
                doXmlF14(res,Resultado);
        }
        
    }//doGetFase14     
    
public static ArrayList<String> getC1Anios()
{
        String[] anios = {"anio1","anio2","anio3","anio4"};        
        return new ArrayList<String>(Arrays.asList(anios));
}


public static ArrayList<String> getC1Discos (String anio) //Type Disco
{
        String[] discos = {"disco1","disco2","disco3","disco4"};
        return new ArrayList<String>(Arrays.asList(discos));   //Type Disco

}
public static ArrayList<String> getC1Canciones (String anio, String idd) //Type Cancion
{
        String[] canciones = {"cancion1","cancion2","cancion3","cancion4"};
        return new ArrayList<String>(Arrays.asList(canciones)); //Type Cancion

}
public static ArrayList<String> getC1Resultado (String anio, String idd, String idc) //Type Cancion
{
        String[] resultado = {"resultado1","resultado2","resultado3","resultado4"};
        return new ArrayList<String>(Arrays.asList(resultado));  //Type Cancion
}
public void doHtmlF11(PrintWriter out, ArrayList Anios)
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
        /*out.println("<li><input type = 'radio' name = 'panio' value = '2004'checked>-2004</input></li>");
        out.println("<li><input type = 'radio' name = 'panio' value = '2005'>-2005</input></li>");
        out.println("<li><input type = 'radio' name = 'panio' value = '2006'>-2006</input></li>");*/
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
public void doXmlF11(HttpServletResponse res, ArrayList Anios)throws IOException
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

public void doHtmlF12(PrintWriter out, String anio, ArrayList Discos)
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
        for(int i=0;i<Discos.size();i++)
        {
        out.println("<li><input type = 'radio' name = 'pidd' value = "+Discos.get(i)+">-"+Discos.get(i)+"</input></li>");
        }
        /*out.println("<li><input type = 'radio' name = 'pidd' value = 'BloodMountain'checked>-Blood Mountain</input></li>");
        out.println("<li><input type = 'radio' name = 'pidd' value = 'Remission'>-Remission</input></li>");
        out.println("<li><input type = 'radio' name = 'pidd' value = 'Leviathan'>-Leviathan</input></li>");*/
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
public void doXmlF12(HttpServletResponse res, ArrayList Discos)throws IOException
    {
            res.setContentType("text/xml");
            PrintWriter out = res.getWriter();
            out.println("<?xml version='1.0' encoding='utf-8' ?>");
            out.println("<discos>");
            for(int i=0;i<Discos.size();i++)
            {
            out.println("<disco>"+Discos.get(i)+"</disco>");
            }
            out.println("</discos>");
    }
public void doHtmlF13(PrintWriter out, String anio, String idd, ArrayList Canciones)
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
        for(int i=0;i<Canciones.size();i++)
        {
        out.println("<li><input type = 'radio' name = 'pidc' value = "+Canciones.get(i)+">-"+Canciones.get(i)+"</input></li>");
        }
        /*out.println("<li><input type = 'radio' name = 'pidc' value = 'LaBarbacoa'checked>-La Barbacoa</input></li>");
        out.println("<li><input type = 'radio' name = 'pidc' value = 'LaCucaracha'>-La Cucaracha</input></li>");
        out.println("<li><input type = 'radio' name = 'pidc' value = 'LaBomba'>-La Bomba</input></li>");*/
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
public void doXmlF13(HttpServletResponse res, ArrayList Canciones)throws IOException
    {
            res.setContentType("text/xml");
            PrintWriter out = res.getWriter();
            out.println("<?xml version='1.0' encoding='utf-8' ?>");
            out.println("<canciones>");
            for(int i=0;i<Canciones.size();i++)
            {
            out.println("<cancion>"+Canciones.get(i)+"</cancion>");
            }
            out.println("</canciones>");
    }
public void doHtmlF14(PrintWriter out, String anio, String idd, String idc, ArrayList Resultado)
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
        out.println("<p>"+Resultado.get(i)+"</p>");
        }
        /*out.println("<p>La Bamba</p>");
        out.println("<p>La Estaca</p>");*/
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
public void doXmlF14(HttpServletResponse res, ArrayList Resultado)throws IOException
    {
        res.setContentType("text/xml");
        PrintWriter out = res.getWriter();
        out.println("<?xml version='1.0' encoding='utf-8' ?>");
        out.println("<canciones>");
        for(int i=0;i<Resultado.size();i++)
        {
        out.println("<cancion>"+Resultado.get(i)+"</cancion>");
        }
        out.println("</canciones>");
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

