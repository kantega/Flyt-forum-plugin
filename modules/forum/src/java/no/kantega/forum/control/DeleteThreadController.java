package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Post;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.forum.permission.PermissionObject;
import no.kantega.forum.permission.Permissions;
import no.kantega.forum.listeners.ForumListener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 21.des.2005
 * Time: 12:38:26
 * To change this template use File | Settings | File Templates.
 */
public class DeleteThreadController extends AbstractForumFormController {
    private ForumDao dao;

    public PermissionObject[] getRequiredPermissions(HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("threadId"));
        return permissions(Permissions.EDIT_THREAD, dao.getThread(id));
    }

    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!request.getMethod().equals("POST")) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return null;
        }
        long id = Long.parseLong(request.getParameter("threadId"));
        ForumThread t = dao.getThread(id);
        long forumId = t.getForum().getId();

        Map ratingNotificationListenerBeans = getApplicationContext().getBeansOfType(ForumListener.class);
        if (ratingNotificationListenerBeans != null && ratingNotificationListenerBeans.size() > 0)  {
            for (ForumListener notificationListener : (Iterable<? extends ForumListener>) ratingNotificationListenerBeans.values()) {
                notificationListener.beforeThreadDelete(t);
            }
        }
        
        dao.delete(t);
        return new ModelAndView(new RedirectView("viewforum?forumId=" + forumId));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
