package nz.net.paulo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Licence: <a href="http://opensource.org/licenses/MIT">MIT</a>
 * @author Martin Paulo 👴
 */
@MultipartConfig
public class UploadServlet extends HttpServlet {

    private static final long serialVersionUID = 4827483655377505914L;

    @Override
    public void init() throws ServletException {
        RequestParser.init();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        new RequestParser(request).deleteHandler().write(response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        new RequestParser(request).getHandler().write(response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        new RequestParser(request).postHandler().write(response);
    }

}