package no.kantega.forum.tags;

import org.apache.log4j.Logger;
import org.springframework.web.util.ExpressionEvaluationUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.context.ApplicationContext;

import javax.servlet.jsp.jstl.core.LoopTagSupport;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import no.kantega.publishing.spring.RootContext;
import no.kantega.publishing.common.data.Content;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.forum.permission.Permissions;
import no.kantega.forum.model.Post;
import no.kantega.modules.user.UserResolver;
import no.kantega.modules.user.ResolvedUser;

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

            long fId = -1;
            if (forumId != null) {
                try {
                    forumId = (String) ExpressionEvaluationUtils.evaluate("forumId", forumId, String.class, pageContext);
                    if (forumId != null) {
                        try {
                            fId = Long.parseLong(forumId, 10);
                        } catch (NumberFormatException e) {

                        }
                    }
                } catch (JspException e) {
                    log.error(e);
                    throw new JspTagException(e.getMessage());
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
            if (fId == -1) {
                posts = dao.getLastPosts(maxPosts*2);
            } else {
                posts = dao.getLastPostsInForum(fId, maxPosts*2);
            }

            List authorizedPosts = new ArrayList();
            for (int j = 0; j < posts.size(); j++) {
                Post p =  (Post)posts.get(j);
                if (authorizedPosts.size() < maxPosts && permissionsManager.hasPermission(username, Permissions.VIEW, p)) {
                    authorizedPosts.add(p);
                }
            }


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
