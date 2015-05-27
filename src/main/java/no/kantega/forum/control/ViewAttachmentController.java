package no.kantega.forum.control;

import no.kantega.commons.util.HttpHelper;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Attachment;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.modules.user.UserResolver;
import no.kantega.publishing.common.data.ImageResizeParameters;
import no.kantega.publishing.common.data.Multimedia;
import no.kantega.publishing.common.data.enums.Cropping;
import no.kantega.publishing.common.exception.InvalidImageFormatException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ViewAttachmentController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private ForumDao dao;
    private MultimediaRequestHandlerHelper multimediaRequestHandlerHelper;
    private PermissionManager permissionManager;
    private UserResolver userResolver;

    @RequestMapping(value = "/viewattachment", method = RequestMethod.GET)
    public void handleAttachment(@RequestParam long attachmentId,
                                 @RequestParam(required = false, defaultValue = "-1") int width,
                                 @RequestParam(required = false, defaultValue = "-1") int height,
                                 @RequestParam(required = false, defaultValue = "CONTAIN") Cropping cropping,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ServletOutputStream out = response.getOutputStream();

        Attachment attachment = dao.getAttachment(attachmentId);

        if (attachment != null) {

            String userName = getUsername(request);
            if (!permissionManager.hasPermission(userName, Permission.VIEW, dao.getForumForAttachment(attachment))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            String mimeType = StringUtils.defaultIfEmpty(attachment.getMimeType(), "application/octet-stream");

            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + attachment.getFileName() + "\"");

            Multimedia source = new Multimedia();
            source.setData(attachment.getData());
            source.setFilename(attachment.getFileName());
            source.setId((int) attachmentId);
            if (mimeType.contains("image")) {
                handleImage(width, height, cropping, request, response, out, mimeType, source);
            } else {
                out.write(attachment.getData());
            }
        } else {
            log.error("Attachment with id {} not found", attachmentId);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    private String getUsername(HttpServletRequest request) {
        String userName = null;
        ResolvedUser user = userResolver.resolveUser(request);
        if(user != null) {
            userName = user.getUsername();
        }
        return userName;
    }

    private void handleImage(int width, int height, Cropping cropping, HttpServletRequest request,
                             HttpServletResponse response, ServletOutputStream out, String mimeType, Multimedia source) throws IOException, InvalidImageFormatException {
        ImageResizeParameters resizeParams = new ImageResizeParameters(height, width, cropping);
        String key = getCacheKey(source.getId(), source, resizeParams);
        if (HttpHelper.isInClientCache(request, response, key, source.getLastModified())) {
            response.sendError(HttpServletResponse.SC_NOT_MODIFIED);

        } else if (shouldResize(mimeType, resizeParams)) {
            byte[] bytes = multimediaRequestHandlerHelper.getResizedMultimediaBytes(key, source, resizeParams);

            response.setHeader("Content-Disposition", "attachment; filename=" + "\"thumb" + key + "\"");
            response.setHeader("Content-Length", String.valueOf(bytes.length));

            out.write(bytes);

        } else {
            response.setHeader("Content-Length", String.valueOf(source.getData().length));
            out.write(source.getData());
        }
    }

    private String getCacheKey(int mmId, Multimedia mm, ImageResizeParameters resizeParams) {
        return Integer.toString(mmId) + "-" + resizeParams.toString() + "-" + mm.getLastModified().getTime();
    }

    private boolean shouldResize(String mimetype, ImageResizeParameters resizeParams) {
        return (mimetype.contains("image") && !mimetype.contains("svg")) && !resizeParams.skipResize();
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }

    public void setMultimediaRequestHandlerHelper(MultimediaRequestHandlerHelper multimediaRequestHandlerHelper) {
        this.multimediaRequestHandlerHelper = multimediaRequestHandlerHelper;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }
}
