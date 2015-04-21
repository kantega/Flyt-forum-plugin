package no.kantega.forum.tags;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.dao.ThreadSortOrder;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import no.kantega.forum.util.ForumPostReadStatus;
import no.kantega.forum.util.ForumUtil;
import no.kantega.publishing.spring.RootContext;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.jstl.core.LoopTagSupport;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ForEachThreadInForumTag extends LoopTagSupport {

    private long forumId;
    private int startIndex;
    private int maxThreads = Integer.MAX_VALUE;
    private Iterator i = null;
    private Logger log = Logger.getLogger(ForEachThreadInForumTag.class);


    protected Object next() throws JspTagException {
        return i == null ? null : i.next();
    }

    protected boolean hasNext() throws JspTagException {
        return i != null && i.hasNext();
    }

    protected void prepare() throws JspTagException {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse)pageContext.getResponse();

        Map daos = RootContext.getInstance().getBeansOfType(ForumDao.class);
        if(daos.size() > 0) {
            ForumDao dao = (ForumDao) daos.values().iterator().next();

            // Hent nye poster siden siste besøk
            ForumPostReadStatus readStatus = new ForumPostReadStatus(request);
            List unreadPosts = null;

            Date lastVisit = ForumUtil.getLastVisit(request, response, true);
            if (lastVisit != null) {
                unreadPosts = dao.getPostsAfterDate(lastVisit);
            }

            List<ForumThread> threads = dao.getThreadsInForum(forumId, startIndex, maxThreads, ThreadSortOrder.SORT_BY_DEFAULT);
            for (ForumThread thread : threads) {
                List last = dao.getLastPostsInThread(thread.getId(), 1);
                if (last.size() > 0) {
                    thread.setLastPost((Post) last.get(0));
                }
                if (unreadPosts != null) {
                    readStatus.updateUnreadPostsInThread(unreadPosts, thread);
                }

            }
            i = threads.iterator();
        }

    }

    public void setForumId(long forumId) {
        this.forumId = forumId;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }
}