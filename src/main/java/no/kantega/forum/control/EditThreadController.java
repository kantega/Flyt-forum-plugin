package no.kantega.forum.control;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionObject;
import no.kantega.forum.util.ForumUtil;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class EditThreadController extends AbstractForumFormController {
    private ForumDao dao;

    public PermissionObject[] getRequiredPermissions(HttpServletRequest request) {
        String threadId = request.getParameter("threadId");

        if(threadId != null) {
            ForumThread thread = dao.getThread(Long.parseLong(threadId));
            return permissions(Permission.EDIT_THREAD, thread);
        } else {
            long id = Long.parseLong(request.getParameter("forumId"));
            Forum forum = dao.getForum(id);
            return permissions(Permission.ADD_THREAD, forum);
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

        t.setModifiedDate(new Date());

        dao.saveOrUpdate(t);
        return new ModelAndView(new RedirectView(request.getContextPath() + "/forum/viewforum?forumId="+t.getForum().getId()));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
