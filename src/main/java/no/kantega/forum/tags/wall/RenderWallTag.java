package no.kantega.forum.tags.wall;


import no.kantega.commons.util.LocaleLabels;
import no.kantega.forum.control.EditPostController;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.dao.ThreadSortOrder;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumCategory;
import no.kantega.publishing.common.Aksess;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static org.apache.commons.lang3.StringUtils.join;

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
	private String shareBoxPlaceholder = null;
    private ThreadSortOrder sortBy = ThreadSortOrder.SORT_BY_DEFAULT;

    private static ForumDao forumDao;

	public void doTag() throws JspException, IOException {
		try {
			PageContext pageContext = ((PageContext) getJspContext());
            if(forumDao == null) {
                forumDao = WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext()).getBean(ForumDao.class);
            }
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

            String forumIdStr = "-1";

			StringBuilder forumListPostsUrl = new StringBuilder(request.getContextPath() + "/forum/listPosts");
			if (forumId > 0 || (forumIds != null && forumIds.size() > 0)) {
				forumListPostsUrl.append("?forumId=");
                if (forumId > 0) {
					forumListPostsUrl.append(String.valueOf(forumId));
                } else {
					forumListPostsUrl.append(join(forumIds, ','));
                }
            } else {
				forumListPostsUrl.append("?forumCategoryId=").append(forumCategoryId);
			}

			forumListPostsUrl.append(String.format("&numberOfPostsToShow=%s&expandThreads=%s", maxthreads, String.valueOf(expandthreads)));

			if (userId != null) {
				forumListPostsUrl.append("&username=").append(userId);
			}
			if (threadId != -1) {
				forumListPostsUrl.append("&threadId=").append(threadId);
			}
			if (hiddenForumId != -1) {
				forumListPostsUrl.append("&hiddenForumId=").append(hiddenForumId);
			}

			if (forumCategoryId != -1) {
                ForumCategory category = forumDao.getForumCategory(forumCategoryId);
                request.setAttribute("forumCategory", category);
			}

            if(sortBy == ThreadSortOrder.SORT_BY_DATE_CREATED){
                forumListPostsUrl.append("&sortBy=").append(sortBy.getId());
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
			request.setAttribute("allowedFileextensions", Aksess.getConfiguration().getString(EditPostController.allowedFileextensionKey, EditPostController.defaultAllowedFileextensionsString));

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
		if (shareBoxPlaceholder != null && !shareBoxPlaceholder.isEmpty()) return shareBoxPlaceholder;
		final String defaultLabelKey = "forum.share.inputfield.label.default";
		final String knownLabelKey = "forum.share.inputfield.label.named";

		final String forumBundleName = "forum";
		final Locale locale = req.getAttribute("aksess_locale") != null ? (Locale) req.getAttribute("aksess_locale") : new Locale("no", "NO");
		final String defaultLabel = LocaleLabels.getLabel(defaultLabelKey, forumBundleName, locale, new HashMap<String, Object>());
		//If the forum is undefined return the default label
		if (fId <= 0)
			return defaultLabel;

		//We have a forumId, now lets get the name, and return the label based on that.
			try {
				Forum forum = forumDao.getForum(fId);
				final String template = LocaleLabels.getLabel(knownLabelKey, forumBundleName, locale, new HashMap<String, Object>());
				return String.format(template, forum.getName());
			} catch (Exception e) {
				//Could not load from dao or could not format based on the template
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

	public void setShareboxPlaceholder(String shareBoxPlaceholder) {
		this.shareBoxPlaceholder = shareBoxPlaceholder;
	}

    public void setSortBy(ThreadSortOrder sortBy) {
        this.sortBy = sortBy;
    }
}
