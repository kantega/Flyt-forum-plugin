package no.kantega.forum.control;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Forum;
import no.kantega.forum.permission.PermissionObject;
import no.kantega.forum.permission.Permissions;
import no.kantega.forum.util.ForumUtil;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 21.des.2005
 * Time: 12:11:47
 * To change this template use File | Settings | File Templates.
 */
public class EditThreadController extends AbstractForumFormController {
    private ForumDao dao;

    public PermissionObject[] getRequiredPermissions(HttpServletRequest request) {
        String threadId = request.getParameter("threadId");

        if(threadId != null) {
            ForumThread thread = dao.getThread(Long.parseLong(threadId));
            return permissions(Permissions.EDIT_THREAD, thread);
        } else {
            long id = Long.parseLong(request.getParameter("forumId"));
            Forum forum = dao.getForum(id);
            return permissions(Permissions.ADD_THREAD, forum);
        }

    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String threadId = request.getParameter("threadId");
        if(threadId != null && !threadId.equals("0")) {
            long id = Long.parseLong(threadId);
            return dao.getPopulatedThread(id);
        } else {
            long id = Long.parseLong(request.getParameter("forumId"));

            Forum f = dao.getPopulatedForum(id);

            ForumThread t = new ForumThread();
            t.setCreatedDate(new Date());
            t.setForum(f);
            return t;
        }

    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object object, BindException bindException) throws Exception {
        ForumThread t = (ForumThread) object;
        if (ForumUtil.isSpam(request)) {
            return new ModelAndView(new RedirectView("nospam"));
        }       

        dao.saveOrUpdate(t);
        return new ModelAndView(new RedirectView(request.getContextPath() + "/forum/viewforum?forumId="+t.getForum().getId()));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
