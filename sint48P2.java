import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class Sint48P2 extends HttpServlet {
public void doGet(HttpServletRequest req, HttpServletResponse res)
throws IOException, ServletException
{
res.setContentType("text/html ; charset=UTF-8");
PrintWriter out = res.getWriter();
String fase = req.getParameter("pfase");
String anio = req.getParameter("panio");
String disco = req.getParameter("pidd");
String cancion = req.getParameter("pidc");
switch(fase)
{
    case "11": selAnio(out); break;
    case "02": error(out); break;
    //case "02": errors(out); break;
    case "01": inicio(out); break;
    case "12": selDisco(out,anio); break;
    case "13": selCancion(out,anio,disco); break;
    case "14": resultado(out,anio,disco,cancion); break;
}
}
public void inicio(PrintWriter out)
{
    out.println("<html>");
    out.println("<head>");
    out.println("<title>Sint: Práctica 2. Consulta de canciones</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<h1>Servicio de consulta de canciones</h1>");
    out.println("<h2>Bienvenido a este servicio.</h2>");
    out.println("<form name = 'miforminit' action=''>");
    out.println("<a href = '/Sint48/P2IM?pfase=02'>Pulsa aqui para ver los ficheros erroneos</a>");
    out.println("<h3>Selecciona una consulta:</h3>");
    out.println("<input type = 'radio' checked>Consulta1: Canciones de un interprete que duran menos que una dada</input>");
    out.println("<br></br>");
    out.println("<input type = 'submit'></input>");
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
    out.println("<input type = 'radio' name = 'panio' value = '2004'checked>2004</input>");
    out.println("<input type = 'radio' name = 'panio' value = '2005'>2005</input>");
    out.println("<input type = 'radio' name = 'panio' value = '2006'>2006</input>");    
    //out.println("<input type = 'hidden' name = 'panio'></input>");    
    out.println("<input type = 'submit'></input>");
    out.println("<button  onclick=\"window.location='/Sint48/P2IM?pfase=01'\">Atras</button> ");    
    out.println("</form>");
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
    out.println("<button onclick=\"window.location='/Sint48/P2IM?pfase=01'\">Atras</button>");
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
    out.println("<input type = 'radio' name = 'pidd' value = 'Blood Mountain'checked>Blood Mountain</input>");
    out.println("<input type = 'radio' name = 'pidd' value = 'Remission'>Remission</input>");
    out.println("<input type = 'radio' name = 'pidd' value = 'Leviathan'>Leviathan</input>");
    out.println("<br></br>");    
    out.println("<input type = 'submit'></input>");
    out.println("<button  onclick=\"window.location='/Sint48/P2IM?pfase=11'\">Atras</button> ");
    out.println("<button  onclick=\"window.location='/Sint48/P2IM?pfase=01'\">Inicio</button> ");
    //Atencion no guarda panioooooooo
    
}
public void selCancion(PrintWriter out, String anio, String disco)
{
    out.println("<html>");
    out.println("<head>");
    out.println("<title>Sint: Práctica 2. Consulta de canciones</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<h1>Servicio de consulta de canciones</h1>");
    out.println("<form name = 'miformpidc'>");
    out.println("<h2>Consulta 1: Año="+anio+", Disco="+disco+"</h2>");    
    out.println("<h3>Selecciona una cancion:</h3>");
    out.println("<input type = 'radio' name = 'sCancion' value = 'La Barbacoa'checked>La Barbacoa</input>");
    out.println("<input type = 'radio' name = 'sCancion' value = 'La Cucaracha'>La Cucaracha</input>");
    out.println("<input type = 'radio' name = 'sCancion' value = 'La Bomba'>La Bomba</input>");
    out.println("<input type = 'hidden' name = 'pfase' value = '14'></input>");
    out.println("<input type = 'hidden' name = 'pidd' value = 'pidd'></input>");
    out.println("<input type = 'hidden' name = 'pidc' value = 'document.miformselCancion.sCancion.value'></input>");
    out.println("<button  onclick=\"window.location='/Sint48/P2IM?pfase=null'\">Inicio</button> ");
    out.println("<input type = 'submit'></input>");
    out.println("<button  onclick=\"window.location='/Sint48/P2IM?pfase=12'\">Atras</button> ");
}
public void resultado(PrintWriter out, String anio, String disco, String cancion)
{
    out.println("<html>");
    out.println("<head>");
    out.println("<title>Sint: Práctica 2. Consulta de canciones</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<h1>Servicio de consulta de canciones</h1>");
    out.println("<form name = 'miformfinal'>");
    out.println("<h2>Consulta 1: Año='panio', Disco=pidd, Cancion='pidc'</h2>");    
    out.println("<h3>Este es el resultado:</h3>");
    out.println("<p>La Bamba</p>");
    out.println("<p>La Estaca</p>");    
    out.println("<input type = 'hidden' name = 'pfase' value = '14'></input>");
    out.println("<input type = 'hidden' name = 'pidd' value = 'pidd'></input>");
    out.println("<input type = 'hidden' name = 'pidc' value = 'pidc'></input>");
    out.println("<button  onclick=\"window.location='/Sint48/P2IM?pfase=null'\">Inicio</button> ");    
    out.println("<button  onclick=\"window.location='/Sint48/P2IM?pfase=13'\">Atras</button> ");
} 
}
