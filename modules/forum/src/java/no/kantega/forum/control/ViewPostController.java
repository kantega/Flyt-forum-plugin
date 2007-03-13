 package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Post;
import no.kantega.commons.util.StringHelper;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 20.des.2005
 * Time: 14:11:51
 * To change this template use File | Settings | File Templates.
 */
public class ViewPostController implements Controller {
    private ForumDao dao;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long id = Long.parseLong(request.getParameter("postId"));
        Post p = dao.getPopulatedPost(id);

        Map map = new HashMap();
        if (p != null) {
            String body = p.getBody();
            body = StringHelper.makeLinks(body);
            body = StringHelper.replace(body, "\n", "<br>");
            p.setBody(body);
        }

        map.put("post", p);
        map.put("gotchildren", String.valueOf(dao.postGotChildren(p)));
        return new ModelAndView("viewpost", map);
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
