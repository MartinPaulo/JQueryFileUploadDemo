package nz.net.paulo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.imgscalr.Scalr;

/**
 * <p>Licence: <a href="http://opensource.org/licenses/MIT">MIT</a>
 * @author Martin Paulo
 */
public class FileGetThumbNailRequestHandler extends RequestHandler {

    private static final int ICON_SIZE = 36;
    public static final String REQUEST = "getthumb";

    public FileGetThumbNailRequestHandler(RequestParser requestParser) {
        super(requestParser);
    }

    @Override
    public void write(HttpServletResponse response) throws IOException {
        File file = rp.getFile(REQUEST);
        if (file.exists()) {
            BufferedImage im = null;
            String mimetype = getMimeType(file);
            if (mimetype.endsWith("png") || mimetype.endsWith("jpeg") || mimetype.endsWith("jpg") || mimetype.endsWith("gif")) {
                im = ImageIO.read(file);
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (im != null) {
                im = Scalr.resize(im, ICON_SIZE);
                if (mimetype.endsWith("png")) {
                    ImageIO.write(im, "PNG", os);
                    response.setContentType("image/png");
                } else if (mimetype.endsWith("jpeg") || mimetype.endsWith("jpg")) {
                    ImageIO.write(im, "jpg", os);
                    response.setContentType("image/jpeg");
                } else {
                    ImageIO.write(im, "GIF", os);
                    response.setContentType("image/gif");
                }
            } else {
                im = new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_RGB);
                Graphics g = im.getGraphics();
                g.setColor(Color.green);
                g.fillOval(0, 0, ICON_SIZE, ICON_SIZE);
                g.dispose();
                ImageIO.write(im, "jpg", os);
                response.setContentType("image/jpeg");
            }
            try (ServletOutputStream srvos = response.getOutputStream()) {
                response.setContentLength(os.size());
                response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
                os.writeTo(srvos);
            }
        }
    } 

}
