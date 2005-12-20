package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.User;
import no.kantega.forum.model.Forum;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 20.des.2005
 * Time: 13:37:11
 * To change this template use File | Settings | File Templates.
 */
public class AddThreadController extends SimpleFormController {
    private ForumDao dao;

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        long id = Long.parseLong(request.getParameter("forumId"));

        User u = dao.getUser(1);
        Date d = new Date();
        Forum f = dao.getPopulatedForum(id);

        ForumThread t = new ForumThread();
        t.setOwner(u);
        t.setCreatedDate(d);
        t.setForum(f);
        return t;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object object, BindException bindException) throws Exception {
        ForumThread t = (ForumThread) object;
        dao.saveOrUpdate(t);
        return new ModelAndView(new RedirectView(request.getContextPath() + "/forum/viewforum?forumId="+t.getForum().getId()));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
