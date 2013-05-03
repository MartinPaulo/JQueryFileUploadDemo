package nz.net.paulo;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>Licence: <a href="http://opensource.org/licenses/MIT">MIT</a>
 * @author Martin Paulo
 */
interface ResponseInterface {

    public static final File UPLOAD_DIR = new File("/tmp/jquery/");
    public static final File TEMP_DIR = new File(System.getProperty("java.io.tmpdir"));

    void write(HttpServletResponse response) throws IOException;
}