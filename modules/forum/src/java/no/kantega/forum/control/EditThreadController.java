package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.ForumThread;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 21.des.2005
 * Time: 12:11:47
 * To change this template use File | Settings | File Templates.
 */
public class EditThreadController extends SimpleFormController {
    private ForumDao dao;

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        long id = Long.parseLong(request.getParameter("threadId"));
        return dao.getPopulatedThread(id);
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
