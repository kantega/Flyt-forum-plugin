package no.kantega.forum.control;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import no.kantega.forum.util.ForumUtil;
import no.kantega.forum.util.ForumPostReadStatus;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 20.des.2005
 * Time: 10:01:58
 * To change this template use File | Settings | File Templates.
 */
public class ViewForumController extends AbstractForumViewController {
    private ForumDao dao;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        long id = Long.parseLong(request.getParameter("forumId"));
        Forum f = dao.getForum(id);

        if (!isAuthorized(request, f)) {
            return new ModelAndView("closedforum", null);
        } else {

            int maxThreads = 50;

            int startIndex = 0;
            try {
                startIndex = Integer.parseInt(request.getParameter("startIndex"));
            } catch (Exception e) {

            }
            List startIndexes = new ArrayList();
            for(int i = 0; i < f.getNumThreads(); i+= maxThreads) {
                startIndexes.add(new Integer(i));
            }

            map.put("current", new Integer(startIndex/maxThreads));
            map.put("startindex", new Integer(startIndex));
            map.put("startindexes", startIndexes);
            map.put("pages", new Integer(startIndexes.size()));

            map.put("forum", f);

            // Hent nye poster siden siste besøk
            ForumPostReadStatus readStatus = new ForumPostReadStatus(request);
            List unreadPosts = null;

            Date lastVisit = ForumUtil.getLastVisit(request, response, true);
            if (lastVisit != null) {
                unreadPosts = dao.getPostsAfterDate(lastVisit);
            }


            List threads = dao.getThreadsInForum(f.getId(), startIndex, maxThreads);
            for (int i = 0; i < threads.size(); i++) {
                ForumThread thread = (ForumThread) threads.get(i);
                List last = dao.getLastPostsInThread(thread.getId(), 1);
                if(last.size() > 0) {
                    thread.setLastPost((Post)last.get(0));
                }
                if (unreadPosts != null) {
                    readStatus.updateUnreadPostsInThread(unreadPosts, thread);
                }

            }
            map.put("threads", threads);

            return new ModelAndView("viewforum", map);
        }
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
