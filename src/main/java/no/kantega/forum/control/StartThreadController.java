package no.kantega.forum.control;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionObject;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.publishing.api.content.ContentIdentifier;
import no.kantega.publishing.common.data.Content;
import no.kantega.publishing.common.service.ContentManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


public class StartThreadController extends AbstractForumFormController  {

    private ForumDao dao;

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public PermissionObject[] getRequiredPermissions(HttpServletRequest request) {
        return new PermissionObject[0];

    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        int contentId = Integer.parseInt(request.getParameter("contentId"));

        Content c = new ContentManagementService(request).getContent(ContentIdentifier.fromContentId(contentId));

        if(c.getForumId() <= 0) {
            log.error("Content " + c.getId() +" does not have a forum attached to it");
            return null;
        } else if(dao.getThreadAboutContent(c.getId()) > 0) {
            log.error("Content " + c.getId() +" already has thread " +dao.getThreadAboutContent(c.getId()) +" created for it");
            return null;
        }
        else {
            Forum f = dao.getForum(c.getForumId());




            //Permission check
            ResolvedUser user = userResolver.resolveUser(request);
            String userName = null;
            if(user != null) {
                userName = user.getUsername();
            }
            PermissionObject[] permissions = permissions(Permission.EDIT_THREAD, f);
            if (permissions != null) {
                for (PermissionObject permissionObject : permissions) {
                    if (!permissionManager.hasPermission(userName, permissionObject.getPermission(), permissionObject.getObject())) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return null;
                    }
                }
            }





            ForumThread t = new ForumThread();
            t.setCreatedDate(new Date());
            t.setForum(f);
            t.setName(c.getTitle());
            t.setContentId(c.getId());


            dao.saveOrUpdate(t);

            return new ModelAndView(new RedirectView("editpost"), "threadId", Long.toString(t.getId()));
        }
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }

    @Override
    protected boolean assertPermissions(HttpServletRequest request) {

        ResolvedUser user = userResolver.resolveUser(request);
        String userName = null;
        if(user != null) {
            userName = user.getUsername();
        }
        PermissionObject[] permissions = getRequiredPermissions(request);
        if (permissions != null) {
            for (PermissionObject permissionObject : permissions) {
                if (!permissionManager.hasPermission(userName, permissionObject.getPermission(), permissionObject.getObject())) {
                    return false;
                }
            }
        }
        return true;
    }
}
