package nz.net.paulo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * <p>Licence: <a href="http://opensource.org/licenses/MIT">MIT</a>
 * @author Martin Paulo
 */
public class FileDetailsRequestHandler extends RequestHandler {

    public static final String REQUEST = "file";

    public FileDetailsRequestHandler(RequestParser requestParser) {
        super(requestParser);
    }

    @Override
    public void write(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        JSONObject files = new JSONObject();
        try (PrintWriter writer = response.getWriter()) {
            File file = rp.getPartUploadedSoFar(REQUEST);
            if (file.exists()) {
                files.put("file", getFileJson(file));
            }
            writer.write(files.toString());
        }
    }

    private static JSONObject getFileJson(File file) {
        JSONObject fileData = new JSONObject();
        fileData.put("name", file.getName());
        fileData.put("size", file.length());
        return fileData;
    }

}
