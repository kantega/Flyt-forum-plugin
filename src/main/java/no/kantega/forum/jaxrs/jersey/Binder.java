package no.kantega.forum.jaxrs.jersey;

import no.kantega.forum.jaxrs.bll.AuthorizationBl;
import no.kantega.forum.jaxrs.bll.ForumBl;
import no.kantega.forum.jaxrs.bll.GroupBl;
import no.kantega.forum.jaxrs.bll.ThreadBl;
import no.kantega.forum.jaxrs.dal.ForumDao;
import no.kantega.forum.jaxrs.dal.GroupDao;
import no.kantega.forum.jaxrs.dal.ThreadDao;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
public class Binder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(GroupDao.class).to(GroupDao.class);
        bind(ThreadDao.class).to(ThreadDao.class);
        bind(ForumDao.class).to(ForumDao.class);
        bind(ForumBl.class).to(ForumBl.class);
        bind(GroupBl.class).to(GroupBl.class);
        bind(ThreadBl.class).to(ThreadBl.class);
        bind(AuthorizationBl.class).to(AuthorizationBl.class);
    }
}

