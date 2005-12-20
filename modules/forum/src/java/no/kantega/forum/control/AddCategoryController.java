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

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 13.des.2005
 * Time: 13:51:46
 * To change this template use File | Settings | File Templates.
 */
public class AddCategoryController extends SimpleFormController {
    private ForumDao dao;

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        User u = dao.getUser(1);
        Date d = new Date();
        ForumCategory fc = new ForumCategory();
        fc.setOwner(u);
        fc.setCreatedDate(d);
        return fc;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object object, BindException bindException) throws Exception {
        ForumCategory fc = (ForumCategory) object;
        dao.saveOrUpdate(fc);
        return new ModelAndView(new RedirectView(request.getContextPath() + "/forum/"));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
