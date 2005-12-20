package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.forum.model.Post;
import no.kantega.forum.model.User;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.dao.ForumDao;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 20.des.2005
 * Time: 14:05:19
 * To change this template use File | Settings | File Templates.
 */
public class AddPostController extends SimpleFormController {
    private ForumDao dao;

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        long id = Long.parseLong(request.getParameter("threadId"));
        User u = dao.getUser(1);
        Date d = new Date();
        ForumThread t = dao.getPopulatedThread(id);

        Post p = new Post();
        p.setOwner(u);
        p.setPostDate(d);
        p.setThread(t);
        return p;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object object, BindException bindException) throws Exception {
        Post p = (Post) object;
        dao.saveOrUpdate(p);
        return new ModelAndView(new RedirectView(request.getContextPath() + "/forum/viewthread?threadId="+p.getThread().getId()));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
