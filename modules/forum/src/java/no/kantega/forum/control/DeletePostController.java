package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Post;
import no.kantega.forum.permission.PermissionObject;
import no.kantega.forum.permission.Permissions;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 21.des.2005
 * Time: 12:38:26
 * To change this template use File | Settings | File Templates.
 */
public class DeletePostController extends AbstractForumFormController {
    private ForumDao dao;

    public PermissionObject[] getRequiredPermissions(HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("postId"));
        return permissions(Permissions.EDIT_POST, dao.getPost(id));
    }

    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!request.getMethod().equals("POST")) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return null;
        }
        long id = Long.parseLong(request.getParameter("postId"));
        Post p = dao.getPopulatedPost(id);
        long threadId = p.getThread().getId();

        // Vis tråd etter sletting
        String view = "viewthread?threadId=" + threadId;
        if (!p.isApproved()) {
            // Vis ikke godkjente innlegg ved sletting av ikke godkjent post
            view = "listunapproved";
        }
        dao.delete(p);
        return new ModelAndView(new RedirectView(view));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
