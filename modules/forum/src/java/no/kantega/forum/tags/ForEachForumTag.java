package no.kantega.forum.tags;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.jsp.jstl.core.LoopTagSupport;
import javax.servlet.jsp.JspTagException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import no.kantega.forum.permission.PermissionManager;
import no.kantega.forum.permission.Permissions;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.ForumCategory;
import no.kantega.forum.model.Forum;
import no.kantega.forum.util.ForumComparator;
import no.kantega.modules.user.UserResolver;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.publishing.spring.RootContext;

/**
 * User: Anders Skar, Kantega AS
 * Date: Jun 14, 2007
 * Time: 1:13:29 PM
 */
public class ForEachForumTag extends LoopTagSupport {

    private String relevance = "";

    private Iterator i = null;

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

            String username = null;
            ResolvedUser user = userResolver.resolveUser((HttpServletRequest)pageContext.getRequest());
            if (user != null) {
                username = user.getUsername();
            }

            Map result = new HashMap();

            // Hent alle forum
            List allForums = dao.getForums();
            for (int j = 0; j < allForums.size(); j++) {
                Forum forum = (Forum) allForums.get(j);
                // Legg til alle forum eller kun de som er relevante
                if (permissionsManager.hasPermission(username, Permissions.VIEW, forum)) {
                    // Legg til forum dersom relevance ikke angitt eller forumet har en eller flere roller som gjør det relevant for brukeren
                    if (relevance.indexOf("group") == -1 || (forum.getGroups() != null && !forum.getGroups().isEmpty())) {
                        result.put("" + forum.getId(), forum);
                    }
                }
            }

            if (relevance.indexOf("user") != -1 && username != null && username.length() > 0) {
                // Legg til forum som brukeren har postet i
                List userForums = dao.getForumsWithUserPostings(username);
                for (int j = 0; j < userForums.size(); j++) {
                    Forum forum = (Forum) userForums.get(j);
                    if (permissionsManager.hasPermission(username, Permissions.VIEW, forum)) {
                        // Legg til kun i resultat dersom det ikke ligger der
                        if (result.get("" + forum.getId()) == null) {
                            result.put("" + forum.getId(), forum);
                        }
                    }
                }
            }

            ForumComparator comparator = new ForumComparator();

            List forums = new ArrayList(result.values());
            Collections.sort(forums, comparator);

            i = forums.iterator();

            relevance = "";
        }
    }

    public void setRelevance(String relevance) {
        this.relevance = relevance;
    }
}

