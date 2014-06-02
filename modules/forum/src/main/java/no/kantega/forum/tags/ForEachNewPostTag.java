package no.kantega.forum.tags;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Post;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.forum.permission.Permissions;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.modules.user.UserResolver;
import no.kantega.publishing.spring.RootContext;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.jstl.core.LoopTagSupport;
import java.util.*;

/**
 * User: Anders Skar, Kantega AS
 * Date: Jan 28, 2007
 * Time: 1:21:19 AM
 */
public class ForEachNewPostTag extends LoopTagSupport {

    private Iterator i = null;
    private int maxPosts = 5;
    private String forumId = null;

    private Logger log = Logger.getLogger(ForEachNewPostTag.class);


    protected Object next() throws JspTagException {
        return i == null ? null : i.next();
    }

    protected boolean hasNext() throws JspTagException {
        return i != null && i.hasNext();
    }

    protected void prepare() throws JspTagException {
        WebApplicationContext context = (WebApplicationContext) pageContext.getRequest().getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        PermissionManager permissionsManager = (PermissionManager) context.getBean("forumPermissionManager");
        UserResolver userResolver = (UserResolver) context.getBean("userResolver");

        i = null;
        Map daos = RootContext.getInstance().getBeansOfType(ForumDao.class);
        if(daos.size() > 0) {
            ForumDao dao = (ForumDao) daos.values().iterator().next();

            long fId[] = null;
            if (forumId != null) {
                try {
                        StringTokenizer tokens = new StringTokenizer(forumId, ",");
                        fId = new long[tokens.countTokens()];
                        int i = 0;
                        while (tokens.hasMoreTokens()) {
                            String tmp = tokens.nextToken();
                            fId[i++] = Integer.parseInt(tmp);
                        }

                } catch (NumberFormatException e) {
                    log.error(e);
                }
            }

            String username = null;
            ResolvedUser user = userResolver.resolveUser((HttpServletRequest)pageContext.getRequest());
            if (user != null) {
                username = user.getUsername();
            }

            /*
                Vi gjør et lite triks: Fordi brukeren kanskje ikke har tilgang til alle forum henter vi flere enn
                det vi trenger og viser bare de første.  Er ikke perfekt... men bør fungere i de fleste tilfeller
             */
            List posts;
            if (fId == null || fId.length == 0) {
                posts = dao.getLastPosts(maxPosts*2);
            } else {
                posts = dao.getLastPostsInForums(fId, maxPosts*2);
            }

            List authorizedPosts = new ArrayList();
            for (int j = 0; j < posts.size(); j++) {
                Post p =  (Post)posts.get(j);
                if (authorizedPosts.size() < maxPosts && permissionsManager.hasPermission(username, Permissions.VIEW, p)) {
                    authorizedPosts.add(p);
                }
            }

            forumId = null;

            i = authorizedPosts.iterator();
        }
    }

    public void setMaxPosts(int maxPosts) {
        this.maxPosts = maxPosts;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

}