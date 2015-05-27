package no.kantega.forum.control;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.listeners.ForumListener;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionObject;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DeleteThreadController extends AbstractForumFormController {
    private ForumDao dao;

    @Override
    public PermissionObject[] getRequiredPermissions(HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("threadId"));
        return permissions(Permission.DELETE_THREAD, dao.getThread(id));
    }

    @Override
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (!assertPermissions(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        if(!request.getMethod().equals("POST")) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return null;
        }
        long id = Long.parseLong(request.getParameter("threadId"));
        ForumThread t = dao.getThread(id);
        long forumId = t.getForum().getId();
        Set<Post> posts = new HashSet<>(dao.getLastPostsInThread(t.getId(), 10000));
        t.setPosts(posts);

        Map<String, ForumListener> ratingNotificationListenerBeans =  BeanFactoryUtils.beansOfTypeIncludingAncestors(getApplicationContext(), ForumListener.class);
        if (ratingNotificationListenerBeans != null && ratingNotificationListenerBeans.size() > 0)  {
            for (ForumListener notificationListener : ratingNotificationListenerBeans.values()) {
                notificationListener.beforeThreadDelete(t);
            }
        }

        dao.delete(t);

        if(isAjaxRequest(request)) {
            return new ModelAndView("ajax-deletethread");
        }

        return new ModelAndView(new RedirectView("viewforum?forumId=" + forumId));
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
