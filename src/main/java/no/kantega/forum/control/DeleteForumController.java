package no.kantega.forum.control;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Forum;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteForumController extends AbstractForumFormController {
    private ForumDao dao;

    @Override
    public PermissionObject[] getRequiredPermissions(HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("forumId"));
        Forum f = dao.getPopulatedForum(id);
        return permissions(Permission.EDIT_FORUM, f);
    }

    @Override
    public ModelAndView handleRequestInternal (HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (!assertPermissions(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        long id = Long.parseLong(request.getParameter("forumId"));
        Forum f = dao.getPopulatedForum(id);
        long catId = f.getForumCategory().getId();
        dao.delete(f);
        return new ModelAndView(new RedirectView(request.getContextPath() + "/forum/viewcategory?categoryId="+catId));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
