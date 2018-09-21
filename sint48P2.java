import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.ArrayList;
import java.util.List;
public class Sint48P2 extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        res.setContentType("text/html ; charset='utf-8'");
        PrintWriter out = res.getWriter();
        String fase = req.getParameter("pfase");
        String anio = req.getParameter("panio");
        String idd = req.getParameter("pidd");
        String idc = req.getParameter("pidc");

        switch(fase)
        {        
            case "01": inicio(out); break;
            case "02": error(out); break;
            case "11": selAnio(out); break;
            case "12": selDisco(out,anio); break;
            case "13": selCancion(out,anio,idd); break;
            case "14": resultado(out,anio,idd,idc); break;
        }
    }
    public void inicio(PrintWriter out)
    {
        out.println("<html>");
        out.println("<head>");
        //out.println("<meta charset=utf-8'></meta>");
        out.println("<title>Sint: Práctica 2. Consulta de canciones</title>");    
        out.println("<link rel='stylesheet' href='/styles/iml.css'></link>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servicio de consulta de canciones</h1>");
        out.println("<h2>Bienvenido a este servicio.</h2>");
        out.println("<form name = 'miforminit' action=''>");
        out.println("<a href = '/Sint48/P2IM?pfase=02'>Pulsa aquí para ver los ficheros erróneos</a>");
        out.println("<h3>Selecciona una consulta:</h3>");
        out.println("<input type = 'radio' checked>Consulta1: Canciones de un intérprete que duran menos que una dada</input>");
        out.println("<br></br>");
        out.println("<input type = 'submit' class = 'buttonSubmit'></input>");
        out.println("<input type = 'hidden' name = 'pfase' value = '11'></input>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }
    public void selAnio(PrintWriter out)
    {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Sint: Práctica 2. Consulta de canciones</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servicio de consulta de canciones</h1>");
        out.println("<h2>Consulta 1</h2>");    
        out.println("<h3>Selecciona un año:</h3>");
        out.println("<form name = 'miformpanio'>");    
        out.println("<input type = 'hidden' name = 'pfase' value = '12'></input>");
        out.println("<ol>");
        out.println("<li><input type = 'radio' name = 'panio' value = '2004'checked>-2004</input></li>");
        out.println("<li><input type = 'radio' name = 'panio' value = '2005'>-2005</input></li>");
        out.println("<li><input type = 'radio' name = 'panio' value = '2006'>-2006</input></li>");
        out.println("</ol>");
        out.println("<br></br>");        
        out.println("<input type = 'submit' class = 'buttonSubmit'></input>");            
        out.println("</form>");
        out.println("<button type = 'button' class = 'buttonAtras' onclick=\"window.location='/Sint48/P2IM?pfase=01'\">Atras</button> ");
        out.println("</body>");
        out.println("</html>");    
    }
    public void error(PrintWriter out)
    {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Sint: Práctica 2. Consulta de canciones</title>");
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
        out.println("<button class = 'buttonAtras'onclick=\"window.location='/Sint48/P2IM?pfase=01'\">Atras</button>");
        out.println("</body>");
        out.println("</html>");
    }
    public void selDisco(PrintWriter out, String anio)
    {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Sint: Práctica 2. Consulta de canciones</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servicio de consulta de canciones</h1>");    
        out.println("<h2>Consulta 1: Año="+anio+"</h2>");    
        out.println("<h3>Selecciona un disco:</h3>");
        out.println("<form name = 'miformpidd'>");
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
        out.println("<button class = 'buttonAtras' onclick=\"window.location='/Sint48/P2IM?pfase=11'\">Atras</button> ");
        out.println("<br></br>");
        out.println("<button class = 'buttonInicio' onclick=\"window.location='/Sint48/P2IM?pfase=01'\">Inicio</button> ");    
    }
    public void selCancion(PrintWriter out, String anio, String idd)
    {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Sint: Práctica 2. Consulta de canciones</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servicio de consulta de canciones</h1>");    
        out.println("<h2>Consulta 1: Año="+anio+", Disco="+idd+"</h2>");
        out.println("<form name = 'miformpidc'>");    
        out.println("<h3>Selecciona una cancion:</h3>");
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
        out.println("<button class = 'buttonAtras' onclick=\"window.location='/Sint48/P2IM?pfase=12'\">Atras</button> ");
        out.println("<br></br>");    
        out.println("<button class = 'buttonInicio' onclick=\"window.location='/Sint48/P2IM?pfase=01'\">Inicio</button> ");
    
    }
    public void resultado(PrintWriter out, String anio, String idd, String idc)
    {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Sint: Práctica 2. Consulta de canciones</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servicio de consulta de canciones</h1>");
        out.println("<form name = 'miformfinal'>");
        out.println("<h2>Consulta 1: Año="+anio+", Disco="+idd+", Cancion="+idc+"</h2>");    
        out.println("<h3>Este es el resultado:</h3>");
        out.println("<p>La Bamba</p>");
        out.println("<p>La Estaca</p>");
        out.println("</form>");
        out.println("<button class = 'buttonAtras'  onclick=\"window.location='/Sint48/P2IM?pfase=13'\">Atras</button> ");
        out.println("<br></br>");
        out.println("<button class = 'buttonInicio' onclick=\"window.location='/Sint48/P2IM?pfase=01'\">Inicio</button> ");    
    } 
}
