package nz.net.paulo;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>Licence: <a href="http://opensource.org/licenses/MIT">MIT</a>
 * @author Martin Paulo
 */
public class HandlerNotFound extends RequestHandler {

    public HandlerNotFound(RequestParser requestParser) {
        super(requestParser);
    }

    @Override
    public void write(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

}
