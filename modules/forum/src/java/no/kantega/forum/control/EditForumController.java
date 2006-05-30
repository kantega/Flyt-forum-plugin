package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.Group;
import no.kantega.forum.model.User;
import no.kantega.forum.model.ForumCategory;
import no.kantega.forum.permission.PermissionObject;
import no.kantega.forum.permission.Permissions;

import java.util.Set;
import java.util.HashSet;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 20.des.2005
 * Time: 09:38:56
 * To change this template use File | Settings | File Templates.
 */
public class EditForumController extends AbstractForumFormController {
    private ForumDao dao;

    public PermissionObject[] getRequiredPermissions(HttpServletRequest request) {
        String forumId = request.getParameter("forumId");
        String categoryId = request.getParameter("categoryId");
        ForumCategory category;
        if(forumId != null) {
            category = dao.getForum(Long.parseLong(forumId)).getForumCategory();
        } else {
            category = dao.getForumCategory(Long.parseLong(categoryId));
        }

        return new PermissionObject[] {new PermissionObject(Permissions.EDIT_FORUM, category)};

    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String forumId = request.getParameter("forumId");
        String categoryId = request.getParameter("categoryId");

        if(forumId != null) {
            long id = Long.parseLong(forumId);
            return dao.getPopulatedForum(id);
        } else {
            long id = Long.parseLong(categoryId);

            // create groups
            /*
            Set groups = new HashSet();
            Group g = dao.getGroup(2);
            groups.add(g);

            User u = dao.getUser(1);
            Date d = new Date();

*/
            ForumCategory fc = dao.getForumCategory(id);

            Forum f = new Forum();
            //f.setOwner(u);
            f.setCreatedDate(new Date());
            f.setForumCategory(fc);
            //f.setGroups(groups);
            return f;
        }
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object object, BindException bindException) throws Exception {
        Forum f = (Forum) object;
        dao.saveOrUpdate(f);
        return new ModelAndView(new RedirectView("viewforum"), "forumId", new Long(f.getId()));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
