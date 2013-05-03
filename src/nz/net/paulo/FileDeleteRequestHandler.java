package nz.net.paulo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>Licence: <a href="http://opensource.org/licenses/MIT">MIT</a>
 * @author Martin Paulo
 */
public class FileDeleteRequestHandler extends RequestHandler {

    public static final String REQUEST = "delfile";

    public FileDeleteRequestHandler(RequestParser requestParser) {
        super(requestParser);
    }

    @Override
    public void write(HttpServletResponse response) throws IOException {
        String filename = rp.getParameter(REQUEST);
        boolean error = true; // always negative, heh?
        String message = "File " + filename + " not found on server."; // the most likely cause of error
        try (PrintWriter writer = response.getWriter()) {
            try {
                File file = rp.getFileInUploadDir(filename);
                if (file.exists()) {
                    error = !file.delete();
                }
            } catch (Exception e) {
                error = true; // should be true, but...
                LOG.log(Level.WARNING, "Could not delete " + filename, e);
                message = e.getMessage();
            }
            if (error) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                writer.write(message);
            }
        }
    }

}
