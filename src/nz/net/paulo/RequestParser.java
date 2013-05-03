package nz.net.paulo;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;

/**
 * I put this wrapper around the request in place because I wanted to have all the headers used in one readable location.
 * <p>
 * Licence: <a href="http://opensource.org/licenses/MIT">MIT</a>
 * 
 * @author Martin Paulo
 */
public class RequestParser {

    private static final File UPLOAD_DIR = new File("/tmp/jquery/");
    private static final File TEMP_DIR = new File(System.getProperty("java.io.tmpdir"));

    private HttpServletRequest request;

    public RequestParser(HttpServletRequest request) {
        this.request = request;
    }

    public String getContentRange() {
        return request.getHeader("Content-Range");
    }

    public String getContentDisposition() {
        return request.getHeader("Content-Disposition");
    }

    public boolean isMultiPart() {
        return ServletFileUpload.isMultipartContent(request);
    }

    private boolean hasNoParameters() {
        return !request.getParameterNames().hasMoreElements();
    }

    public String getParameter(final String parameter) {
        return request.getParameter(parameter);
    }

    /**
     * Limits this to Tomcat 7... Also destroys the rational for the central location
     * 
     * @return
     * @throws IllegalStateException
     * @throws IOException
     * @throws ServletException
     */
    public Collection<Part> getParts() throws IllegalStateException, IOException, ServletException {
        return request.getParts();
    }

    private boolean is(String parameter) {
        return StringUtils.isNotBlank(getParameter(parameter));
    }

    public ResponseInterface getHandler() {
        if (hasNoParameters()) {
            return new ListFilesHandler(this);
        } else if (is(FileDetailsRequestHandler.REQUEST)) {
            return new FileDetailsRequestHandler(this);
        } else if (is(FileGetRequestHandler.REQUEST)) {
            return new FileGetRequestHandler(this);
        } else if (is(FileGetThumbNailRequestHandler.REQUEST)) {
            return new FileGetThumbNailRequestHandler(this);
        }
        return new HandlerNotFound(this);
    }

    public ResponseInterface postHandler() {
        return new FileUploadHandler(this);
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    public RequestHandler deleteHandler() {
        if (is(FileDeleteRequestHandler.REQUEST)) {
            return new FileDeleteRequestHandler(this);
        }
        ;
        return new HandlerNotFound(this);
    }

    public static void init() {
        // nothing here yet
    }

    public File getFileInUploadDir(String filename) {
        return new File(getUploadDir(), filename);
    }

    public File getUploadDir() {
        return getUserDir(UPLOAD_DIR);
    }
    
    private File getTempDir() {
        return getUserDir(TEMP_DIR);
    }
    
    private File getUserDir(File parent) {
        File baseDir = new File(parent, request.getUserPrincipal().getName());
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        return baseDir;
    }

    public File getPartUploadedSoFar(String requestName) {
        return getPart(getParameter(requestName));
    }

    public File getPart(String fileName) {
        return new File(getTempDir(), fileName + RequestHandler.getPartExtension());
    }

    public File getFile(String requestName) {
        return new File(getUploadDir(), getParameter(requestName));
    }

    public String getFilePathInTempDir(String fileName, String extension) {
        return getTempDir() + File.separator + fileName + extension;
    }

    public String getFilePathInUploadDir(String fileName, String extension) {
        return getUploadDir() + File.separator + fileName + extension;
    }

}