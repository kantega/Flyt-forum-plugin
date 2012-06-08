package no.kantega.forum.tags.wall;


import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.ForumCategory;
import no.kantega.publishing.spring.RootContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Map;

public class RenderWallTag extends SimpleTagSupport{

    private boolean sharebox = false;
    private boolean showforumtabs = false;
    private int forumId = -1;
    private int forumCategoryId = -1;
    private int hiddenForumId = -1;
    private int defaultPostForumId = -1;
    private int maxthreads = 20;
    private String userId = null;
    private int threadId = -1;

    public void doTag() throws JspException, IOException {
        try {
            PageContext pageContext = ((PageContext)getJspContext());
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();


            String forumListPostsUrl = request.getContextPath() + "/forum/listPosts";
            if (forumId > 0) {
                forumListPostsUrl = forumListPostsUrl + "?forumId=" + forumId;
            } else {
                forumListPostsUrl = forumListPostsUrl + "?forumCategoryId=" + forumCategoryId;
            }
            forumListPostsUrl += "&numberOfPostsToShow=" + maxthreads;

            if (userId != null) {
                forumListPostsUrl += "&username=" + userId;
            }
            if (threadId != -1) {
                forumListPostsUrl += "&threadId=" + threadId;
            }

            if (hiddenForumId != -1) {
                forumListPostsUrl += "&hiddenForumId=" + hiddenForumId;
            }

            if (forumCategoryId != -1) {
                Map daos = RootContext.getInstance().getBeansOfType(ForumDao.class);
                if(daos.size() > 0) {
                    ForumDao dao = (ForumDao) daos.values().iterator().next();
                    ForumCategory category = dao.getForumCategory(forumCategoryId);
                    pageContext.getRequest().setAttribute("forumCategory", category);
                }
            }

            request.setAttribute("showSharebox", sharebox);
            request.setAttribute("showForumTabs", showforumtabs);
            request.setAttribute("forumId", forumId);
            request.setAttribute("hiddenForumId", hiddenForumId);
            request.setAttribute("defaultPostForumId", defaultPostForumId);
            request.setAttribute("forumCategoryId", forumCategoryId);
            request.setAttribute("userid", userId);
            request.setAttribute("forumListPostsUrl", forumListPostsUrl);

            pageContext.include("/WEB-INF/jsp/forum/wall/wall.jsp");


            request.removeAttribute("forumListPostsUrl");
            request.removeAttribute("userId");
        } catch (ServletException e) {
            throw new JspException(e);
        } finally {
            forumId = -1;
            forumCategoryId = -1;
            defaultPostForumId = -1;
            maxthreads = 20;
            userId = null;
            sharebox = false;
            showforumtabs = false;
        }
    }

    public void setShowforumtabs(boolean showforumtabs) {
        this.showforumtabs = showforumtabs;
    }

    public void setSharebox(boolean sharebox) {
        this.sharebox = sharebox;
    }

    public void setForumid(int forumId) {
        this.forumId = forumId;
    }

    public void setDefaultpostforumid(int defaultPostForumId) {
        this.defaultPostForumId = defaultPostForumId;
    }

    public void setHiddenforumid(int hiddenForumId) {
        this.hiddenForumId = hiddenForumId;
    }

    public void setForumcategoryid(int forumCategoryId) {
        this.forumCategoryId = forumCategoryId;
    }


    public void setUserid(String userid) {
        this.userId = userid;
    }

    public void setMaxthreads(int maxthreads) {
        this.maxthreads = maxthreads;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }
}

