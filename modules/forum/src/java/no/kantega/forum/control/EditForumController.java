package no.kantega.forum.control;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumCategory;
import no.kantega.forum.permission.PermissionObject;
import no.kantega.forum.permission.Permissions;
import no.kantega.modules.user.UserProfileManager;
import no.kantega.modules.user.GroupManager;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 20.des.2005
 * Time: 09:38:56
 * To change this template use File | Settings | File Templates.
 */
public class EditForumController extends AbstractForumFormController {
    private ForumDao dao;
    private UserProfileManager userProfileManager;
    private GroupManager groupManager;

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


    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception{
        Map referenceData = new HashMap();

        Forum forum = (Forum)command;
        String moderator = forum.getModerator();
        if (moderator != null && moderator.length() > 0) {
            referenceData.put("moderator", userProfileManager.getUserProfile(moderator));
        }

        referenceData.put("groups", groupManager.getAllGroups());

        return referenceData;
    }


    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String forumId = request.getParameter("forumId");
        String categoryId = request.getParameter("categoryId");

        if(forumId != null) {
            long id = Long.parseLong(forumId);
            return dao.getPopulatedForum(id);
        } else {
            long id = Long.parseLong(categoryId);

            ForumCategory fc = dao.getForumCategory(id);

            Forum f = new Forum();
            f.setCreatedDate(new Date());
            f.setForumCategory(fc);
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

    public void setUserProfileManager(UserProfileManager userProfileManager) {
        this.userProfileManager = userProfileManager;
    }

    public void setGroupManager(GroupManager groupManager) {
        this.groupManager = groupManager;
    }
}
