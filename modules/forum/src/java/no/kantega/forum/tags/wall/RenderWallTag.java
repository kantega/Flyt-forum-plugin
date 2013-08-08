package no.kantega.forum.tags.wall;


import no.kantega.commons.util.LocaleLabels;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumCategory;
import no.kantega.publishing.spring.RootContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RenderWallTag extends SimpleTagSupport {

	private boolean sharebox = false;
	private boolean showforumtabs = false;
	private boolean expandthreads = false;
	private int forumId = -1;
    private List<Integer> forumIds = null;
	private int forumCategoryId = -1;
	private int hiddenForumId = -1;
	private int defaultPostForumId = -1;
	private int maxthreads = 20;
	private String userId = null;
	private int threadId = -1;

	public void doTag() throws JspException, IOException {
		try {
			PageContext pageContext = ((PageContext) getJspContext());
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();


            String forumIdStr = "-1";

			String forumListPostsUrl = request.getContextPath() + "/forum/listPosts";
			if (forumId > 0 || (forumIds != null && forumIds.size() > 0)) {
                if (forumId > 0) {
                    forumIdStr = "" + forumId;
                } else {
                    forumIdStr = "";
                    for (int i = 0; i < forumIds.size(); i++) {
                        if (i > 0) {
                            forumIdStr += ",";
                        }
                        forumIdStr += forumIds.get(i);
                    }
                }
				forumListPostsUrl = forumListPostsUrl + "?forumId=" + forumIdStr;
            } else {
				forumListPostsUrl = forumListPostsUrl + "?forumCategoryId=" + forumCategoryId;
			}

			forumListPostsUrl += String.format("&numberOfPostsToShow=%s&expandThreads=%s", maxthreads, String.valueOf(expandthreads));

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
				if (daos.size() > 0) {
					ForumDao dao = (ForumDao) daos.values().iterator().next();
					ForumCategory category = dao.getForumCategory(forumCategoryId);
					pageContext.getRequest().setAttribute("forumCategory", category);
				}
			}

			request.setAttribute("showSharebox", sharebox);
			request.setAttribute("showForumTabs", showforumtabs);
			request.setAttribute("forumId", forumIdStr);
            request.setAttribute("selectedForumId", forumId);
			request.setAttribute("hiddenForumId", hiddenForumId);
			request.setAttribute("defaultPostForumId", defaultPostForumId);
			request.setAttribute("forumCategoryId", forumCategoryId);
			request.setAttribute("userid", userId);
			request.setAttribute("forumListPostsUrl", forumListPostsUrl);


			final String label = getShareHelpText(request, forumId);
			request.setAttribute("helptextLabel", label);

			pageContext.include("/WEB-INF/jsp/forum/wall/wall.jsp");


			request.removeAttribute("forumListPostsUrl");
			request.removeAttribute("userId");
		} catch (ServletException e) {
			throw new JspException(e);
		} finally {
			forumId = -1;
            forumIds = null;
			forumCategoryId = -1;
			defaultPostForumId = -1;
			maxthreads = 20;
			userId = null;
			sharebox = false;
			showforumtabs = false;
			expandthreads = false;
		}
	}

	//Get placeholder text for the new forum entry. If not present, get the default placeholder.
	private String getShareHelpText(final HttpServletRequest req, final int fId) {
		final String defaultLabelKey = "forum.share.inputfield.label.default";
		final String knownLabelKey = "forum.share.inputfield.label.named";

		final String forumBundleName = "forum";
		final Locale locale = req.getAttribute("aksess_locale") != null ? (Locale) req.getAttribute("aksess_locale") : new Locale("no", "NO");
		final String defaultLabel = LocaleLabels.getLabel(defaultLabelKey, forumBundleName, locale, new HashMap());
		//If the forum is undefined return the default label
		if (fId <= 0)
			return defaultLabel;

		//We have a forumId, now lets get the name, and return the label based on that.
		Map daos = RootContext.getInstance().getBeansOfType(ForumDao.class);
		if (daos.size() > 0) {
			try {
				ForumDao dao = (ForumDao) daos.values().iterator().next();
				Forum forum = dao.getForum(fId);
				final String template = LocaleLabels.getLabel(knownLabelKey, forumBundleName, locale, new HashMap());
				return String.format(template, forum.getName());
			} catch (Exception e) {
				//Could not load from dao or could not format based on the template
				return defaultLabel;
			}
		} else {
			//No dao present, we get the default label afterall
			return defaultLabel;
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

    public void setForumids(List<Integer> forumIds) {
        this.forumIds = forumIds;
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

	public void setExpandthreads(boolean expandthreads) {
		this.expandthreads = expandthreads;
	}
}
