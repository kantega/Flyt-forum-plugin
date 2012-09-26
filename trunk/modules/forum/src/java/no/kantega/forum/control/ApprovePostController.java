package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Post;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Anders Skar, Kantega AS
 * Date: Mar 8, 2007
 * Time: 1:14:49 PM
 */
public class ApprovePostController  implements Controller {
    private ForumDao dao;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long id = Long.parseLong(request.getParameter("postId"));
        Post p = dao.getPopulatedPost(id);
        dao.approve(p);
        return new ModelAndView(new RedirectView("listunapproved"));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
