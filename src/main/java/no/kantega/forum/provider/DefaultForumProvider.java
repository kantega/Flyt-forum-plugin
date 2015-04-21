package no.kantega.forum.provider;

import no.kantega.publishing.forum.ForumProvider;
import no.kantega.publishing.common.data.Content;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.ForumCategory;
import no.kantega.forum.model.Forum;

import java.util.List;
import java.util.Iterator;

public class DefaultForumProvider implements ForumProvider {
    private ForumDao dao;

    public String getForumsAsOptionList(long selected) {
        StringBuilder buffer = new StringBuilder();

        List forumcategories = dao.getForumCategories();

        for (Object forumcategory : forumcategories) {
            ForumCategory c = (ForumCategory) forumcategory;
            buffer.append("<optgroup label=\"").append(c.getName()).append("\">");

            Iterator j = c.getForums().iterator();

            while (j.hasNext()) {
                Forum forum = (Forum) j.next();

                buffer.append("<option value=\"").append(forum.getId()).append("\"");
                if (forum.getId() == selected) {
                    buffer.append(" selected");
                }
                buffer.append(">").append(forum.getName()).append("</option>");
            }
            buffer.append("</optgroup>");

        }
        return buffer.toString();
    }

    public long getThreadAboutContent(Content content) {
        return dao.getThreadAboutContent(content.getId());
    }

    public List getUserPostings(String userid) {
        return dao.getUserPostings(userid, -1);
    }

    public List getUserPostings(String userid, int max) {
        return dao.getUserPostings(userid, max);
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }

}