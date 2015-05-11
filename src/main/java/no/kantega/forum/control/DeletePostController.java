package no.kantega.forum.control;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Post;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionObject;
import no.kantega.forum.service.ForumPostService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeletePostController extends AbstractForumFormController {
    private ForumDao dao;
    private ForumPostService service;

    public PermissionObject[] getRequiredPermissions(HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("postId"));
        return permissions(Permission.EDIT_POST, dao.getPost(id));
    }

    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!request.getMethod().equals("POST")) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return null;
        }
        long id = Long.parseLong(request.getParameter("postId"));
        Post p = dao.getPopulatedPost(id);
        long threadId = p.getThread().getId();

        // Vis tr�d etter sletting
        String view =request.getParameter("redirect");
        if(view == null || view.length() < 1){
            view = "viewthread?threadId=" + threadId;
        }

        if (!p.isApproved()) {
            // Vis ikke godkjente innlegg ved sletting av ikke godkjent post
            view = "listunapproved";
        }


        String deletedText = getApplicationContext().getMessage("post.deletedText", new Object[0], RequestContextUtils.getLocale(request));
        service.deletePost(p, deletedText);

        if(isAjaxRequest(request)) {
            return new ModelAndView("ajax-deletepost");
        }
        return new ModelAndView(new RedirectView(view));
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }

    public void setForumPostService(ForumPostService forumPostService) {
        this.service = forumPostService;
    }
}
