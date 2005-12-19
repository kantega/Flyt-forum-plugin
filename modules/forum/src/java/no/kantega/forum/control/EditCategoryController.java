package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.ForumCategory;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 13.des.2005
 * Time: 12:53:55
 * To change this template use File | Settings | File Templates.
 */
public class EditCategoryController extends SimpleFormController {
    private ForumDao dao;
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        long id = Long.parseLong(request.getParameter("id"));
        return dao.getForumCategory(id);
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object object, BindException bindException) throws Exception {
        ForumCategory fc = (ForumCategory) object;
        dao.saveOrUpdate(fc);
        return new ModelAndView(new RedirectView("/"));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
