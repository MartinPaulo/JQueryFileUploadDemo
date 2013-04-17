package nz.net.paulo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;

/**
 * <p>Licence: <a href="http://opensource.org/licenses/MIT">MIT</a>
 * @author Martin Paulo
 */
public class FileUploadHandler extends RequestHandler {

    public FileUploadHandler(RequestParser requestParser) {
        super(requestParser);
    }

    /**
     * Extracts file name from the content-disposition string
     * 
     * @throws UnsupportedEncodingException
     */
    private static String extractFileName(String contentDisp) throws UnsupportedEncodingException {
        if (contentDisp != null) {
            String[] items = contentDisp.split(";");
            for (String s : items) {
                if (s.trim().startsWith("filename")) {
                    // content disposition's value can be encoded if it has different values...
                    return s.substring(s.indexOf("=") + 2, s.length() - 1);// URLDecoder.decode(, "UTF-8");
                }
            }
        }
        return "";
    }

    private static long getFileSize(String contentRange) {
        if (contentRange != null) {
            String[] items = contentRange.split("/");
            if (items.length == 2) {
                return Long.parseLong(items[1]);
            }
        }
        return 0L;
    }

    // bytes 5000000-9999999/10925556
    private static long getBytesUpTo(String contentRange) {
        if (contentRange != null) {
            String[] items = contentRange.split("\\D+");
            return Long.parseLong(items[1]);
        }
        return 0;
    }

    private static File mergeFiles(File outputFile, File partFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputFile, true);
                FileInputStream fis = new FileInputStream(partFile)) {
            IOUtils.copy(fis, fos);
            fos.flush();
            fos.close();
        } finally {
            partFile.delete();
        }
        return outputFile;
    }

    private String getTempFilePartExtension() {
        if (isFilePart()) {
            return ".tmp";
        }
        return "";
    }

    private boolean isFilePart() {
        return !StringUtils.isEmpty(rp.getContentRange());
    }

    /*
     * (non-Javadoc) some grotty code here, whilst we work out how best to handle the differing file stuff...
     * 
     * We will either have an entire file, or a part of a file.
     * 
     * @see nz.net.paulo.RequestHandler#write(javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void write(HttpServletResponse response) throws IOException {
        if (!rp.isMultiPart()) {
            throw new IllegalArgumentException("Request is not multipart, please 'multipart/form-data' enctype for your form.");
        }
        response.setContentType("application/json");
        JSONArray jsonArray = new JSONArray();
        String fileName = extractFileName(rp.getContentDisposition());
        try {
            for (Part part : rp.getParts()) {
                if (StringUtils.isBlank(fileName)) {
                    String partContentDisposition = part.getHeader("content-disposition");
                    fileName = extractFileName(partContentDisposition);
                }
                String filePath = RequestHandler.UPLOAD_DIR + File.separator + fileName + getTempFilePartExtension();
                System.out.println("FilePath: " + filePath);
                part.write(filePath);
                if (isFilePart()) {
                    File tempFile = new File(filePath);
                    File partFile = new File(RequestHandler.UPLOAD_DIR, fileName + getPartExtension());
                    if (partFile.exists()) {
                        partFile = mergeFiles(partFile, tempFile);
                    } else {
                        tempFile.renameTo(partFile);
                    }
                    if (partFile.length() == getFileSize(rp.getContentRange())) {
                        File finalFile = new File(RequestHandler.UPLOAD_DIR, fileName);
                        partFile.renameTo(finalFile);
                    }
                }
                File mainFile = new File(RequestHandler.UPLOAD_DIR, fileName);
                jsonArray.put(getFileJson(mainFile.getName(), mainFile.length()));
            }
            String responseString = "{\"files\": " + jsonArray.toString() + "}";
            try (PrintWriter writer = response.getWriter()) {
                writer.write(responseString);
            }
        } catch (IllegalStateException | ServletException e) {
            throw new IOException(e);
        }

    }

}
