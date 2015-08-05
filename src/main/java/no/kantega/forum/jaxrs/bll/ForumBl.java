package no.kantega.forum.jaxrs.bll;

import no.kantega.forum.jaxrs.dal.ForumDao;
import no.kantega.forum.jaxrs.dal.jdbc.Jdbc;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-08-05
 */
public class ForumBl {

    private DataSource dataSource;
    private ForumDao forumDao;

    @Inject
    public ForumBl(@Named("forumDataSource") DataSource dataSource, ForumDao forumDao) {
        this.dataSource = dataSource;
        this.forumDao = forumDao;
    }

    public List<Long> getForumIds(List<String> roles) {
        return Jdbc.readOnly(dataSource, connection -> {
            List<Long> forumIds = forumDao.getForumIdsForGroups(connection, roles);
            forumIds.addAll(forumDao.getForumIdsWithoutGroups(connection));
            return forumIds;
        });
    }


}
