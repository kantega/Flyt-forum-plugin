package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Post;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.permission.PermissionManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 21.des.2005
 * Time: 12:38:26
 * To change this template use File | Settings | File Templates.
 */
public class DeleteThreadController implements Controller {
    private ForumDao dao;


    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!request.getMethod().equals("POST")) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return null;
        }
        long id = Long.parseLong(request.getParameter("threadId"));
        ForumThread t = dao.getThread(id);
        long forumId = t.getForum().getId();
        dao.delete(t);
        return new ModelAndView(new RedirectView("viewforum?forumId=" + forumId));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}