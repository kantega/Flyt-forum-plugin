package no.kantega.forum.tags;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Post;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.modules.user.UserResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.core.LoopTagSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class ForEachNewPostTag extends LoopTagSupport {

    private Iterator i = null;
    private int maxPosts = 5;
    private String forumId = null;

    private Logger log = LoggerFactory.getLogger(ForEachNewPostTag.class);
    private ForumDao dao;
    private PermissionManager permissionsManager;
    private UserResolver userResolver;

    @Override
    public void setPageContext(PageContext pageContext) {
        super.setPageContext(pageContext);
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext());
        dao = context.getBean(ForumDao.class);
        permissionsManager = context.getBean("forumPermissionManager", PermissionManager.class);
        userResolver = context.getBean("userResolver", UserResolver.class);
    }

    protected Object next() throws JspTagException {
        return i == null ? null : i.next();
    }

    protected boolean hasNext() throws JspTagException {
        return i != null && i.hasNext();
    }

    protected void prepare() throws JspTagException {
        i = null;
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
                log.error("Error prepare", e);
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
        List<Post> posts;
        if (fId == null || fId.length == 0) {
            posts = dao.getLastPosts(maxPosts*2);
        } else {
            posts = dao.getLastPostsInForums(fId, maxPosts*2);
        }

        List<Post> authorizedPosts = new ArrayList<>();
        for (Post p : posts) {
            if (authorizedPosts.size() < maxPosts && permissionsManager.hasPermission(username, Permission.VIEW, p)) {
                authorizedPosts.add(p);
            }
        }

        forumId = null;

        i = authorizedPosts.iterator();
    }

    public void setMaxPosts(int maxPosts) {
        this.maxPosts = maxPosts;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

}
