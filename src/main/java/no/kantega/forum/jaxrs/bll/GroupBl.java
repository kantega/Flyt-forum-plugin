package no.kantega.forum.jaxrs.bll;

import no.kantega.forum.jaxrs.bol.GroupBo;
import no.kantega.forum.jaxrs.dal.GroupDao;
import no.kantega.forum.jaxrs.dal.jdbc.Jdbc;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-08-04
 */
public class GroupBl {

    private DataSource dataSource;
    private GroupDao groupDao;

    @Inject
    public GroupBl(@Named("forumDataSource") DataSource dataSource, GroupDao groupDao) {
        this.dataSource = dataSource;
        this.groupDao = groupDao;
    }

    public List<GroupBo> getGroups() {
        return Jdbc.readOnly(dataSource, groupDao::getAllGroups);
    }

    public List<GroupBo> getGroupsByForumId(final Long forumId) {
        return Jdbc.readOnly(dataSource, connection ->
            groupDao.getGroups(connection, forumId)
        );
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public GroupDao getGroupDao() {
        return groupDao;
    }
}
