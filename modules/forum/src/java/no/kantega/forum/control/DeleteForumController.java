package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Forum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 13.jan.2006
 * Time: 12:40:31
 * To change this template use File | Settings | File Templates.
 */
public class DeleteForumController implements Controller {
    private ForumDao dao;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
