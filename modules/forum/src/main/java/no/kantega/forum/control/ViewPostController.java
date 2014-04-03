package no.kantega.forum.control;

import no.kantega.commons.util.StringHelper;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Post;
import no.kantega.forum.util.ForumPostReadStatus;
import no.kantega.forum.util.ForumUtil;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
            Date lastVisit = ForumUtil.getLastVisit(request, response, true);

            // Legg til at denne posten er lest siden siste besøk
            if (lastVisit != null && p != null && p.getPostDate().getTime() > lastVisit.getTime()) {
                new ForumPostReadStatus(request).addPost(p);
            }

            map.put("lastVisit", lastVisit);
            
            return new ModelAndView("viewpost", map);
        }
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
