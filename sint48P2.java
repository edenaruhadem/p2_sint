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
String anio = req.getParameter("anio");
String disco = req.getParameter("disco");
String cancion = req.getParameter("cancion");
switch(fase)
{
    case "01": inicio(out,fase); break;
}
}
public void inicio(PrintWriter out, String fase)
{
    out.println("<html>");
    out.println("<head>");
    out.println("<title>Sint: Práctica 2. Consulta de canciones</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<h1>Servicio de consulta de canciones</h1>");
    out.println("<h2>Bienvenido a este servicio.</h2>");
    out.println("<form name = \"miforminit\">");
    out.println("<a href = \"Pulsa aqui para ver los ficheros erroneos\" onclick = 'document.miforminit.action=\"?fase02\"'></a>");
    out.println("<h3>Selecciona una consulta:</h3>");
    out.println("<input type = \"radio\" checked>Consulta1: Canciones de un interprete que duran menos que una dada</input>");
    out.println("<br></br>");
    out.println("<input type = \"submit\"></input>");
    out.println("<input type = \"hidden\" name = \"pfase\"></input>");
    out.println("</body>");
    out.println("</html>");
} 
}
