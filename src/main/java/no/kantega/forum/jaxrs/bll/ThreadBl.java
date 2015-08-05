package no.kantega.forum.jaxrs.bll;

import no.kantega.forum.jaxrs.dal.ForumDao;
import no.kantega.forum.jaxrs.dal.ThreadDao;
import no.kantega.forum.jaxrs.dal.jdbc.Jdbc;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-08-04
 */
public class ThreadBl {

    private DataSource dataSource;
    private ForumDao forumDao;
    private ThreadDao threadDao;

    @Inject
    public ThreadBl(@Named("forumDataSource") DataSource dataSource, ForumDao forumDao, ThreadDao threadDao) {
        this.dataSource = dataSource;
        this.forumDao = forumDao;
        this.threadDao = threadDao;
    }

    public List<Long> getThreadIdsByForumIds(List<Long> forumIds) {
        return Jdbc.readOnly(dataSource, connection ->
                threadDao.getThreadIdsForForumIds(connection, forumIds)
        );
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public ForumDao getForumDao() {
        return forumDao;
    }

    public ThreadDao getThreadDao() {
        return threadDao;
    }
}
