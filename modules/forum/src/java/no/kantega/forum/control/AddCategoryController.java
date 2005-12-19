package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.forum.model.ForumCategory;
import no.kantega.forum.model.User;
import no.kantega.forum.dao.ForumDao;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 13.des.2005
 * Time: 13:51:46
 * To change this template use File | Settings | File Templates.
 */
public class AddCategoryController extends SimpleFormController {
    private ForumDao dao;

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {
        ForumCategory fc = new ForumCategory();
        User u = dao.getUser(1);
        fc.setOwner(u);
        return fc;
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {
        ForumCategory fc = (ForumCategory) object;
        System.out.println("owner="+String.valueOf(fc.getOwner().getId()));
        dao.saveOrUpdate(fc);
        return new ModelAndView(new RedirectView("/"));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
