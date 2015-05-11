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

    public PermissionObject[] getRequiredPermissions(HttpServletRequest request) {
        return permissions(Permission.EDIT_FORUM, null);
    }
    public ModelAndView handleRequestInternal (HttpServletRequest request, HttpServletResponse response) throws Exception {
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
