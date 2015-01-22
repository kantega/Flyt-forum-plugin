package no.kantega.forum.control;

import no.kantega.commons.client.util.RequestParameters;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Attachment;
import no.kantega.forum.util.ImageHelper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;

/**
 * User: Anders Skar, Kantega AS
 * Date: Jan 27, 2007
 * Time: 7:07:54 PM
 */
public class ViewAttachmentController extends AbstractController {
    private ForumDao dao;

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

                if (mimeType.indexOf("image") != -1 && (width != -1 || height != -1)) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bos.write(attachment.getData());
                    ByteArrayOutputStream tmp = ImageHelper.resizeImage(bos, width, height, "jpg", 85);
                    out.write(tmp.toByteArray());

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
}
