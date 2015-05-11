package no.kantega.forum.control;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.ForumCategory;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionObject;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class EditCategoryController extends AbstractForumFormController {
    private ForumDao dao;

    private PermissionObject[] permissions = new PermissionObject[] {new PermissionObject(Permission.EDIT_CATEGORY, null)};

    public PermissionObject[] getRequiredPermissions(HttpServletRequest request) {
        return permissions;
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String idString = request.getParameter("categoryId");
        if(idString != null && !idString.equals("0")) {
            long id = Long.parseLong(idString);
            return dao.getForumCategory(id);
        } else {
            /*
            // create groups
            Set groups = new HashSet();
            Group g = dao.getGroup(2);
            groups.add(g);

            User u = dao.getUser(1); // get owner
            */

            // init category
            ForumCategory fc = new ForumCategory();

            //fc.setOwner(u);
            //fc.setGroups(groups);

            fc.setCreatedDate(new Date());
            return fc;
        }
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
