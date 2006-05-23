package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.ForumCategory;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.forum.permission.PermissionObject;
import no.kantega.forum.permission.Permissions;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 21.des.2005
 * Time: 15:36:15
 * To change this template use File | Settings | File Templates.
 */
public class DeleteCategoryController extends AbstractForumFormController {
    private ForumDao dao;


    public PermissionObject[] getRequiredPermissions(HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("categoryId"));
        return permissions(Permissions.EDIT_CATEGORY, dao.getForumCategory(id));
    }

    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!assertPermissions(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        } else {
            long id = Long.parseLong(request.getParameter("categoryId"));
            ForumCategory fc = dao.getForumCategory(id);
            dao.delete(fc);
            return new ModelAndView(new RedirectView(request.getContextPath() + "/forum/"));
        }
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }

}
