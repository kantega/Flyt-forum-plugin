package no.kantega.forum.control;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.ForumCategory;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteCategoryController extends AbstractForumFormController {
    private ForumDao dao;


    public PermissionObject[] getRequiredPermissions(HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("categoryId"));
        return permissions(Permission.EDIT_CATEGORY, dao.getForumCategory(id));
    }

    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!assertPermissions(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        } else {
            long id = Long.parseLong(request.getParameter("categoryId"));
            ForumCategory fc = dao.getForumCategory(id);
            dao.delete(fc);
            return new ModelAndView(new RedirectView("."));
        }
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }

}
