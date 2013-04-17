package nz.net.paulo;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * <p>Licence: <a href="http://opensource.org/licenses/MIT">MIT</a>
 * @author Martin Paulo
 */
public abstract class RequestHandler implements ResponseInterface {
    
    protected static final Logger LOG = Logger.getLogger("nz.net.mpaulo.rh");

    private static final String SERVLET_NAME = "UploadServlet";
    
    protected RequestParser rp;

    public RequestHandler(RequestParser requestParser) {
        this.rp = requestParser;
    }
    
    public static void createUploadDirectories() {
        UPLOAD_DIR.mkdirs();
    }

    @Override
    public abstract void write(HttpServletResponse response) throws IOException;

    protected static String getPartExtension() {
        return ".part";
    }

    protected static String getMimeType(final File file) {
        String result = "";
        if (file.exists()) {
            if (is(file, "png")) {
                result = "image/png";
            } else if (is(file, "jpg")) {
                result = "image/jpg";
            } else if (is(file,"jpeg")) {
                result = "image/jpeg";
            } else if (is(file,"gif")) {
                result = "image/gif";
            } else {
                // if we used ConfigurableMimeFileTypeMap from Spring we wouldn't have to do all the prior cases
                javax.activation.MimetypesFileTypeMap mtMap = new javax.activation.MimetypesFileTypeMap();
                result = mtMap.getContentType(file);
            }
        }
        return result;
    }

    private static boolean is(final File file, final String extension) {
        return extension.equals(getExtension(file.getName()));
    }

    private static String getExtension(final String filename) {
        String result = "";
        int pos = filename.lastIndexOf('.');
        if (pos > 0 && pos < filename.length() - 1) {
            result = filename.substring(pos + 1);
        }
        return result.toLowerCase();
    }

    protected static JSONObject getFileJson(String fileName, long size) {
        JSONObject json = new JSONObject();
        json.put("name", fileName);
        json.put("size", size);
        json.put("url", SERVLET_NAME + "?getfile=" + fileName);
        json.put("thumbnail_url", SERVLET_NAME+ "?getthumb=" + fileName);
        json.put("delete_url", SERVLET_NAME+ "?delfile=" + fileName);
        json.put("delete_type", "DELETE");
        return json;
    }
}