package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.User;
import no.kantega.forum.model.ForumCategory;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 20.des.2005
 * Time: 09:32:07
 * To change this template use File | Settings | File Templates.
 */
public class AddForumController extends SimpleFormController {
    private ForumDao dao;

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        long id = Long.parseLong(request.getParameter("id"));

        User u = dao.getUser(1);
        Date d = new Date();
        ForumCategory fc = dao.getForumCategory(id);

        Forum f = new Forum();
        f.setOwner(u);
        f.setCreatedDate(d);
        f.setForumCategory(fc);
        System.out.println("formBackingObject id="+f.getId());
        return f;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object object, BindException bindException) throws Exception {
        Forum f = (Forum) object;
        System.out.println("owner="+f.getOwner().getId());
        System.out.println("fc="+f.getForumCategory().getId());
        System.out.println("name="+f.getName());
        System.out.println("desc="+f.getDescription());
        System.out.println("id="+f.getId());
        //dao.addForumToCategory(f, f.getForumCategory());
        dao.saveOrUpdate(f);
        return new ModelAndView(new RedirectView(request.getContextPath() + "/forum/viewcategory?id=5"));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
