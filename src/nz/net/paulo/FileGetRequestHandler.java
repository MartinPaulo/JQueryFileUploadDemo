package nz.net.paulo;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Licence: <a href="http://opensource.org/licenses/MIT">MIT</a>
 * @author Martin Paulo
 */
public class FileGetRequestHandler extends RequestHandler {

    public static final String REQUEST = "getfile";

    public FileGetRequestHandler(RequestParser requestParser) {
        super(requestParser);
    }

    @Override
    public void write(HttpServletResponse response) throws IOException {
        File file = rp.getFile(REQUEST);
        if (file.exists()) {
            int bytes = 0;
            try (ServletOutputStream op = response.getOutputStream()) {
                response.setContentType(getMimeType(file));
                response.setContentLength((int) file.length());
                // should encode for non standard characters?
                response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
                byte[] bbuf = new byte[1024];
                try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
                    while ((bytes = in.read(bbuf)) != -1) {
                        op.write(bbuf, 0, bytes);
                    }
                }
                op.flush();
            }
        }
    }

}
