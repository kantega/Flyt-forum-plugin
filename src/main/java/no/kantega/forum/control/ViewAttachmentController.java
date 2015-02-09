package no.kantega.forum.control;

import no.kantega.commons.client.util.RequestParameters;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Attachment;
import no.kantega.publishing.common.data.Multimedia;
import no.kantega.publishing.multimedia.ImageEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewAttachmentController extends AbstractController {
    private ForumDao dao;
    private ImageEditor imageEditor;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletOutputStream out = response.getOutputStream();

        RequestParameters param = new RequestParameters(request);
        int id = param.getInt("attachmentId");
        int width = param.getInt("width");
        int height = param.getInt("height");

        if (id != -1) {
            Attachment attachment = dao.getAttachment((long)id);
            if (attachment != null) {
                String mimeType = attachment.getMimeType();
                if (mimeType == null || mimeType.length() == 0) {
                    mimeType = "application/octet-stream";
                }

                response.setContentType(mimeType);
                response.addHeader("Content-Disposition", "attachment; filename=\"" + attachment.getFileName() + "\"");

                if (mimeType.contains("image") && (width != -1 || height != -1)) {
                    Multimedia source = new Multimedia();
                    source.setData(attachment.getData());
                    source.setFilename(attachment.getFileName());
                    Multimedia multimedia = imageEditor.resizeMultimedia(source, width, height);
                    out.write(multimedia.getData());
                } else {
                    out.write(attachment.getData());
                }
            }
        }

        return null;
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }

    public void setImageEditor(ImageEditor imageEditor) {
        this.imageEditor = imageEditor;
    }
}
