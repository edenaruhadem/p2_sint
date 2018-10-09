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

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        res.setContentType("text/html ; charset='utf-8'");
        PrintWriter out = res.getWriter();
	String passwd = req.getParameter("passwd");
        String fase = req.getParameter("pfase");
        String anio = req.getParameter("panio");
        String idd = req.getParameter("pidd");
        String idc = req.getParameter("pidc");
        //String auto = req.getParameter("auto");
                
        //StringBuilder xmlStringBuilder = new StringBuilder();

	if(!passwd.equals("d4r18c392b"))
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
	}
	else
	{
		switch(fase)
        	{        
            	case "01": doGetFase01(out); break;
            	case "02": doGetFase02(out); break;
            	case "11": doGetFase11(out); break;
            	case "12": doGetFase12(out,anio); break;
            	case "13": doGetFase13(out,anio,idd); break;
            	case "14": doGetFase14(out,anio,idd,idc); break;
        	}

	}        
    }//doGet
    /*public void doGetFase01(PrintWriter out)
    {                       
    }*/           
    public void doGetFase01(PrintWriter out)
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
        out.println("<a href = '/sint48/P2IM?passwd=d4r18c392b&pfase=02'>Pulsa aquí para ver los ficheros erróneos</a>");
        out.println("<h3>Selecciona una consulta:</h3>");
        out.println("<input type = 'radio' checked>Consulta1: Canciones de un intérprete que duran menos que una dada</input>");
        out.println("<br></br>");
        out.println("<input type = 'submit' class = 'buttonSubmit'></input>");
        out.println("<input type = 'hidden' name = 'passwd' value = d4r18c392b></input>");
        out.println("<input type = 'hidden' name = 'pfase' value = '11'></input>");	
        out.println("</form>");
        out.println("</body>");
        out.println("<footer>");
        out.println("<p>sint48. @Diego Rios Castro.</p>");                
        out.println("</footer>");
        out.println("</html>");
    }//doHtmlF01
    /*public void doXmlF01(PrintWriter out)
    {        
        out.println("<?xml version=’1.0’ encoding=’utf-8’ ?>");
        out.println("<service>");
        out.println("<status>OK</status>");
        out.println("</service>");
    }*/
    public void doGetFase02(PrintWriter out)
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
        out.println("<h3>Se han encontrado 4 ficheros con errores</h3>");
        out.println("<p>Error</p>");
        out.println("<p>Error</p>");    
        out.println("<p>Error</p>");    
        out.println("<p>Error</p>");        
        out.println("<button class = 'buttonAtras'onclick=\"window.location='/sint48/P2IM?passwd=d4r18c392b&pfase=01'\">Atras</button>");
        out.println("</body>");
        out.println("<footer>");
        out.println("<p>sint48. @Diego Rios Castro.</p>");                
        out.println("</footer>");
        out.println("</html>");
    }//doGetFase02
    public void doGetFase11(PrintWriter out)
    {
        ArrayList<String>Anios = new ArrayList<String>();
        Anios = getC1Anios();
        doHtmlF11(out,Anios);
    }//doGetFase11

    public void doGetFase12(PrintWriter out, String anio)
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
	out.println("<input type = 'hidden' name = 'passwd' value = 'd4r18c392b'></input>");
        out.println("<input type = 'hidden' name = 'pfase' value = '13'></input>");	
        out.println("<input type = 'hidden' name = 'panio' value ='"+anio+"'></input>");
        out.println("<ol>");
        out.println("<li><input type = 'radio' name = 'pidd' value = 'BloodMountain'checked>-Blood Mountain</input></li>");
        out.println("<li><input type = 'radio' name = 'pidd' value = 'Remission'>-Remission</input></li>");
        out.println("<li><input type = 'radio' name = 'pidd' value = 'Leviathan'>-Leviathan</input></li>");
        out.println("</ol>");
        out.println("<br></br>");    
        out.println("<input type = 'submit' class = 'buttonSubmit'></input>");
        out.println("</form>");
        out.println("<button class = 'buttonAtras' onclick=\"window.location='/sint48/P2IM?passwd=d4r18c392b&pfase=11'\">Atras</button> ");
        out.println("<br></br>");
        out.println("<button class = 'buttonInicio' onclick=\"window.location='/sint48/P2IM?passwd=d4r18c392b&pfase=01'\">Inicio</button> ");
        out.println("</body>");
        out.println("<footer>");
        out.println("<p>sint48. @Diego Rios Castro.</p>");                
        out.println("</footer>");
        out.println("</html>");    
    }//doGetFase12
    public void doGetFase13(PrintWriter out, String anio, String idd)
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
	out.println("<input type = 'hidden' name = 'passwd' value = 'd4r18c392b'></input>");
        out.println("<input type = 'hidden' name = 'pfase' value = '14'></input>");	
        out.println("<input type = 'hidden' name = 'panio' value = '"+anio+"'></input>");
        out.println("<input type = 'hidden' name = 'pidd' value = '"+idd+"'></input>");
        out.println("<ol>");
        out.println("<li><input type = 'radio' name = 'pidc' value = 'LaBarbacoa'checked>-La Barbacoa</input></li>");
        out.println("<li><input type = 'radio' name = 'pidc' value = 'LaCucaracha'>-La Cucaracha</input></li>");
        out.println("<li><input type = 'radio' name = 'pidc' value = 'LaBomba'>-La Bomba</input></li>");
        out.println("</ol>");
        out.println("<br></br>");    
        out.println("<input type = 'submit' class = 'buttonSubmit'></input>");
        out.println("</form>");
        out.println("<button class = 'buttonAtras' onclick=\"window.location='/sint48/P2IM?passwd=d4r18c392b&pfase=12&panio="+anio+"'\">Atras</button> ");
        out.println("<br></br>");    
        out.println("<button class = 'buttonInicio' onclick=\"window.location='/sint48/P2IM?passwd=d4r18c392b&pfase=01'\">Inicio</button> ");
        out.println("</body>");
        out.println("<footer>");
        out.println("<p>sint48. @Diego Rios Castro.</p>");                
        out.println("</footer>");
        out.println("</html>");
    
    }//doGetFase13
    public void doGetFase14(PrintWriter out, String anio, String idd, String idc)
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
        out.println("<p>La Bamba</p>");
        out.println("<p>La Estaca</p>");
        out.println("</form>");
        out.println("<button class = 'buttonAtras'  onclick=\"window.location='/sint48/P2IM?passwd=d4r18c392b&pfase=13&panio="+anio+"&pidd="+idd+"'\">Atras</button> ");
        out.println("<br></br>");
        out.println("<button class = 'buttonInicio' onclick=\"window.location='/sint48/P2IM?passwd=d4r18c392b&pfase=01'\">Inicio</button> ");
        out.println("</body>");
        out.println("<footer>");
        out.println("<p>sint48. @Diego Rios Castro.</p>");                
        out.println("</footer>");
        out.println("</html>");    
    }//doGetFase14
public static ArrayList<String> getC1Anios()
{
        String[] anios = {"anio1","anio2","anio3","anio4"};        
        return new ArrayList<String>(Arrays.asList(anios));
}


public static ArrayList<Disco> getC1Discos (String anio)
{

}
public static ArrayList<Cancion> getC1Canciones (String anio, String idd)
{

}
public static ArrayList<Cancion> getC1Resultado (String anio, String idd, String idc)
{
        
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
	out.println("<input type = 'hidden' name = 'passwd' value = 'd4r18c392b'></input>");    
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
        out.println("<button type = 'button' class = 'buttonAtras' onclick=\"window.location='/sint48/P2IM?passwd=d4r18c392b&pfase=01'\">Atras</button> ");
        out.println("</body>");
        out.println("<footer>");
        out.println("<p>sint48. @Diego Rios Castro.</p>");                
        out.println("</footer>");
        out.println("</html>");
}


}

