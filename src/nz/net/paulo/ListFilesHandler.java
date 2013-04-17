package nz.net.paulo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

/**
 * <p>Licence: <a href="http://opensource.org/licenses/MIT">MIT</a>
 * @author Martin Paulo
 */
public class ListFilesHandler extends RequestHandler {

    public ListFilesHandler(RequestParser requestParser) {
        super(requestParser);
    }

    @Override
    public void write(HttpServletResponse response) throws IOException {
        JSONArray jsonArray = new JSONArray();
        try (PrintWriter writer = response.getWriter()) {
            response.setContentType("application/json");
            File[] files = UPLOAD_DIR.listFiles();
            for (File file : files) {
                jsonArray.put(getFileJson(file.getName(), file.length()));
            }
            String responseString = "{\"files\": " + jsonArray.toString() + "}";
            LOG.info(responseString);
            writer.write(responseString);
        }
    }

}
