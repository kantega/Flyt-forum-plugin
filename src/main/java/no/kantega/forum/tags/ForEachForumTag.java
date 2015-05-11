package no.kantega.forum.tags;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Forum;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.forum.util.ForumComparator;
import no.kantega.forum.util.ForumUtil;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.modules.user.UserResolver;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.jstl.core.LoopTagSupport;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * User: Anders Skar, Kantega AS
 * Date: Jun 14, 2007
 * Time: 1:13:29 PM
 */
public class ForEachForumTag extends LoopTagSupport {

    private String relevance = "";

    private Iterator i = null;

    private Logger log = Logger.getLogger(ForEachForumTag.class);


    protected Object next() throws JspTagException {
        return i == null ? null : i.next();
    }

    protected boolean hasNext() throws JspTagException {
        return i != null && i.hasNext();
    }

    protected void prepare() throws JspTagException {
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
        PermissionManager permissionsManager = context.getBean("forumPermissionManager", PermissionManager.class);
        UserResolver userResolver = context.getBean("userResolver", UserResolver.class);

        i = null;
        Map<String, ForumDao> daos = context.getBeansOfType(ForumDao.class);
        if(daos.size() > 0) {
            ForumDao dao = daos.values().iterator().next();

            String username = null;
            ResolvedUser user = userResolver.resolveUser((HttpServletRequest)pageContext.getRequest());
            if (user != null) {
                username = user.getUsername();
            }

            Map<String, Forum> result = new HashMap<>();

            // Hent alle forum
            List<Forum> allForums = dao.getForums();
            for (Forum forum : allForums) {
                // Legg til alle forum eller kun de som er relevante
                if (permissionsManager.hasPermission(username, Permission.VIEW, forum)) {
                    // Legg til forum dersom relevance ikke angitt eller forumet har en eller flere roller som gjør det relevant for brukeren
                    if (!relevance.contains("group") || (forum.getGroups() != null && !forum.getGroups().isEmpty())) {
                        result.put(String.valueOf(forum.getId()), forum);
                    }
                }
            }

            if (relevance.contains("user") && isNotBlank(username)) {
                // Legg til forum som brukeren har postet i
                List<Forum> userForums = dao.getForumsWithUserPostings(username);
                for (Forum forum : userForums) {
                    if (permissionsManager.hasPermission(username, Permission.VIEW, forum)) {
                        // Legg til kun i resultat dersom det ikke ligger der
                        if (result.get(Long.toString(forum.getId())) == null) {
                            result.put(Long.toString(forum.getId()), forum);
                        }
                    }
                }
            }

            ForumComparator comparator = new ForumComparator();

            List<Forum> forums = new ArrayList<>(result.values());
            Collections.sort(forums, comparator);

            Date lastVisit = ForumUtil.getLastVisit((HttpServletRequest)pageContext.getRequest(), (HttpServletResponse)pageContext.getResponse(), false);


            // Finn antall nye innlegg
            for (Forum forum : forums) {
                int num = dao.getNewPostCountInForum(forum.getId(), lastVisit);
                forum.setNumNewPosts(num);
            }

            i = forums.iterator();

            relevance = "";
        }
    }

    public void setRelevance(String relevance) {
        this.relevance = relevance;
    }
}

