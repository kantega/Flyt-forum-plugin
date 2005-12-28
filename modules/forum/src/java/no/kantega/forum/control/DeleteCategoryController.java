package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.ForumCategory;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 21.des.2005
 * Time: 15:36:15
 * To change this template use File | Settings | File Templates.
 */
public class DeleteCategoryController implements Controller {
    private ForumDao dao;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long id = Long.parseLong(request.getParameter("categoryId"));
        ForumCategory fc = dao.getPopulatedForumCategory(id);
        dao.delete(fc);
        return new ModelAndView(new RedirectView(request.getContextPath() + "/forum/"));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
