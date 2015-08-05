package no.kantega.forum.jaxrs.bll;

import no.kantega.forum.jaxrs.bol.PostBo;
import no.kantega.forum.jaxrs.dal.PostDao;
import no.kantega.forum.jaxrs.dal.jdbc.Jdbc;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-08-05
 */
public class PostBl {

    private DataSource dataSource;
    private PostDao postDao;

    @Inject
    public PostBl(@Named("forumDataSource") DataSource dataSource, PostDao postDao) {
        this.dataSource = dataSource;
        this.postDao = postDao;
    }

    public List<PostBo> getPostsByPostIds(List<Long> postIds) {
        return Jdbc.readOnly(dataSource, connection ->
            postDao.getPostsByPostIds(connection, postIds)
        );
    }
}
