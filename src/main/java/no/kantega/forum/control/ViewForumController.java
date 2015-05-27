package no.kantega.forum.control;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.dao.ThreadSortOrder;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import no.kantega.forum.util.ForumPostReadStatus;
import no.kantega.forum.util.ForumUtil;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class ViewForumController extends AbstractForumViewController {
    private ForumDao dao;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
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
            List<Integer> startIndexes = new ArrayList<>(f.getNumThreads());
            for(int i = 0; i < f.getNumThreads(); i+= maxThreads) {
                startIndexes.add(i);
            }

            map.put("current", startIndex / maxThreads);
            map.put("startindex", startIndex);
            map.put("startindexes", startIndexes);
            map.put("pages", startIndexes.size());

            map.put("forum", f);

            // Hent nye poster siden siste besøk
            ForumPostReadStatus readStatus = new ForumPostReadStatus(request);
            List<Post> unreadPosts = null;

            Date lastVisit = ForumUtil.getLastVisit(request, response, true);
            if (lastVisit != null) {
                unreadPosts = dao.getPostsAfterDate(lastVisit);
            }

            List<ForumThread> threads = dao.getThreadsInForum(f.getId(), startIndex, maxThreads, ThreadSortOrder.SORT_BY_DEFAULT);
            for (ForumThread thread : threads) {
                List<Post> last = dao.getLastPostsInThread(thread.getId(), 1);
                if (last.size() > 0) {
                    thread.setLastPost(last.get(0));
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
