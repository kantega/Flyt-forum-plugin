package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Post;
import no.kantega.forum.util.ForumUtil;
import no.kantega.commons.util.StringHelper;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 20.des.2005
 * Time: 14:11:51
 * To change this template use File | Settings | File Templates.
 */
public class ViewPostController extends AbstractForumViewController {
    private ForumDao dao;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long id = Long.parseLong(request.getParameter("postId"));
        Post p = dao.getPopulatedPost(id);

        if (!isAuthorized(request, p)) {
            return new ModelAndView("closedforum", null);
        } else {
            Map map = new HashMap();
            if (p != null) {
                String body = p.getBody();
                body = StringHelper.makeLinks(body);
                body = StringHelper.replace(body, "\n", "<br>");
                p.setBody(body);
            }

            map.put("post", p);
            map.put("gotchildren", String.valueOf(dao.postGotChildren(p)));

            // Legg inn tidspunkt for siste besøk
            Date lastVisit = ForumUtil.updateLastVisit(request, response);
            map.put("lastVisit", lastVisit);
            
            return new ModelAndView("viewpost", map);
        }
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
