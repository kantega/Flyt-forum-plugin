package no.kantega.forum.control;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumCategory;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionObject;
import no.kantega.modules.user.GroupManager;
import no.kantega.modules.user.UserProfileManager;
import no.kantega.publishing.common.service.TopicMapService;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

        return new PermissionObject[] {new PermissionObject(Permission.EDIT_FORUM, category)};

    }


    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception{
        Map<String, Object> referenceData = new HashMap<>();

        Forum forum = (Forum)command;
        String moderator = forum.getModerator();
        if (moderator != null && moderator.length() > 0) {
            referenceData.put("moderator", userProfileManager.getUserProfile(moderator));
        }

        referenceData.put("groups", groupManager.getAllGroups());

        TopicMapService topicService = new TopicMapService(request);
        referenceData.put("topicMaps", topicService.getTopicMaps());

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
        return new ModelAndView(new RedirectView("viewforum"), "forumId", f.getId());
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
