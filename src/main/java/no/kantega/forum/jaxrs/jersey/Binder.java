package no.kantega.forum.jaxrs.jersey;

import no.kantega.forum.jaxrs.dal.CategoryDao;
import no.kantega.forum.jaxrs.dal.ForumDao;
import no.kantega.forum.jaxrs.dal.GroupDao;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
public class Binder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(CategoryDao.class).to(CategoryDao.class).named("flytForumCategoryDao");
        bind(ForumDao.class).to(ForumDao.class).named("flytForumForumDao");
        bind(GroupDao.class).to(GroupDao.class).named("flytForumGroupDao");
    }
}

