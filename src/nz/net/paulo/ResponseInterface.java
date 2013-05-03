package nz.net.paulo;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>Licence: <a href="http://opensource.org/licenses/MIT">MIT</a>
 * @author Martin Paulo
 */
interface ResponseInterface {

    void write(HttpServletResponse response) throws IOException;
}