package no.kantega.forum.control;

import no.kantega.commons.util.StringHelper;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import no.kantega.forum.util.ForumPostReadStatus;
import no.kantega.forum.util.ForumUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 20.des.2005
 * Time: 13:28:18
 * To change this template use File | Settings | File Templates.
 */
public class ViewThreadController extends AbstractForumViewController {
    private ForumDao dao;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long id = Long.parseLong(request.getParameter("threadId"));
        ForumThread t = dao.getThread(id);
        int maxPosts = 50;

        if (!isAuthorized(request, t)) {
            return new ModelAndView("closedforum", null);
        } else {
            String postId = request.getParameter("postId");
            if(postId != null) {
                long pid = Long.parseLong(postId);
                int index = dao.getPostCountBefore(pid);
                index -= index % maxPosts;
                Map map = new HashMap();
                map.put("threadId", new Long(t.getId()));
                map.put("startIndex", new Long(index));
                return new ModelAndView(new RedirectView("viewthread#post_" +pid), map);
            }
            Map map = new HashMap();
            map.put("thread", t);

            int startIndex = 0;
            try {
                startIndex = Integer.parseInt(request.getParameter("startIndex"));
            } catch (Exception e) {

            }
            List startIndexes = new ArrayList();
            for(int i = 0; i < t.getNumPosts(); i+= maxPosts) {
                startIndexes.add(new Integer(i));
            }

            map.put("current", new Integer(startIndex/maxPosts));
            map.put("startindex", new Integer(startIndex));
            map.put("startindexes", startIndexes);
            map.put("pages", new Integer(startIndexes.size()));

            // Legg inn tidspunkt for siste besøk
            Date lastVisit = ForumUtil.getLastVisit(request, response, true);

            List posts = dao.getPostsInThread(t.getId(), startIndex, maxPosts);
            for (int i = 0; i < posts.size(); i++) {
                Post p = (Post)posts.get(i);
                if (p.getBody() != null) {
                    String body = p.getBody();
                    body = StringHelper.makeLinks(body);
                    body = StringHelper.replace(body, "\n", "<br>");
                    p.setBody(body);

                    // Legg til at denne posten er lest siden siste besøk
                    if (lastVisit != null && p.getPostDate().getTime() > lastVisit.getTime()) {
                        new ForumPostReadStatus(request).addPost(p);
                    }
                }
            }


            map.put("posts", posts);
            map.put("lastVisit", lastVisit);

            return new ModelAndView("viewthread", map);
        }
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }

}
