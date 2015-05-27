package no.kantega.forum.control;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.ForumCategory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Collections.singletonMap;

public class ListForumsController implements Controller {
    private ForumDao dao;
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long id = Long.parseLong(request.getParameter("id"));
        ForumCategory fc = dao.getPopulatedForumCategory(id);

        return new ModelAndView("listforums", singletonMap("forumcategory", fc));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
