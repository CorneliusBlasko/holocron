import controllers.SWControllerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.SWService;
import services.SWServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "holocron", urlPatterns = {"/ships", "/characters"})
public class HolocronServlet extends HttpServlet{

    private static Logger logger = LoggerFactory.getLogger(HolocronServlet.class);
    private final SWServiceImpl service = new SWServiceImpl();
    private final SWControllerImpl controller = new SWControllerImpl(service);


    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws IOException{
        String response;
        String error;

        String entity = req.getServletPath().replaceAll("/", "");

        try{
            response = controller.getResult(entity);
        }catch(Exception e){
            error = "Error retrieving params from SWAPI request. Error: " + e;
            logger.error(error);
            response = error;
        }

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.addHeader("Access-Control-Allow-Origin","*");
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");

        out.print(response);
        out.flush();
    }

}
