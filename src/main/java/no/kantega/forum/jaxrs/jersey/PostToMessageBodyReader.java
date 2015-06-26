package no.kantega.forum.jaxrs.jersey;

import no.kantega.forum.jaxrs.bol.Fault;
import no.kantega.forum.jaxrs.tol.AttachmentTo;
import no.kantega.forum.jaxrs.tol.PostTo;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.joda.time.Instant;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-25
 */
@Provider
@Consumes("multipart/form-data")
public class PostToMessageBodyReader implements MessageBodyReader<PostTo> {

    @Context
    private HttpServletRequest request;

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (PostTo.class.equals(type)) {
            return ServletFileUpload.isMultipartContent(request);
        }
        return false;
    }

    @Override
    public PostTo readFrom(Class<PostTo> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
        try {
            PostTo postTo = new PostTo();
            postTo.setSubject(request.getParameter("subject"));
            postTo.setBody(request.getParameter("body"));
            if (request instanceof MultipartRequest) {
                MultipartRequest multipartHttpServletRequest = (MultipartRequest)request;
                List<MultipartFile> files = multipartHttpServletRequest.getFiles("attachment");
                for (MultipartFile file : files) {
                    if (postTo.getAttachments() == null) {
                        postTo.setAttachments(new ArrayList<AttachmentTo>());
                    }
                    AttachmentTo attachmentTo = new AttachmentTo();
                    attachmentTo.setCreated(Instant.now());
                    attachmentTo.setData(file.getInputStream());
                    attachmentTo.setFileName(file.getOriginalFilename());
                    attachmentTo.setMimeType(file.getContentType());
                    attachmentTo.setFileSize(file.getSize());
                    postTo.getAttachments().add(attachmentTo);
                }
            } else {
                FileItemIterator fileItemIterator = servletFileUpload.getItemIterator(new JaxrsRequestContext(request, entityStream));
                while (fileItemIterator.hasNext()) {
                    FileItemStream fileItem = fileItemIterator.next();
                    if ("attachment".equals(fileItem.getFieldName())) {
                        if (postTo.getAttachments() == null) {
                            postTo.setAttachments(new ArrayList<AttachmentTo>());
                        }
                        AttachmentTo attachmentTo = new AttachmentTo();
                        attachmentTo.setCreated(Instant.now());
                        attachmentTo.setData(fileItem.openStream());
                        attachmentTo.setFileName(fileItem.getName());
                        attachmentTo.setMimeType(fileItem.getContentType());
                        postTo.getAttachments().add(attachmentTo);
                    }
                }
            }
            return postTo;
        } catch (FileUploadException | IOException cause) {
            throw new Fault(500, cause);
        }
    }

    private static class JaxrsRequestContext implements RequestContext {

        private HttpServletRequest request;
        private InputStream inputStream;

        public JaxrsRequestContext(HttpServletRequest request, InputStream inputStream) {
            this.request = request;
            this.inputStream = inputStream;
        }

        @Override
        public String getCharacterEncoding() {
            return request.getCharacterEncoding();
        }

        @Override
        public String getContentType() {
            return request.getContentType();
        }

        @Override
        public int getContentLength() {
            return request.getContentLength();
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return inputStream;
        }
    }
}
