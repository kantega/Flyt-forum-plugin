package no.kantega.osuser.provider.hibernate3;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.hibernate.SessionFactory;
import com.opensymphony.user.provider.UserProvider;
import com.opensymphony.user.Entity;

import java.util.Properties;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 12.sep.2005
 * Time: 21:21:27
 * To change this template use File | Settings | File Templates.
 */
public abstract class SpringHibernateBaseProvider implements UserProvider {

    protected SpringHibernateOsUserDao dao;

    public void setDao(SpringHibernateOsUserDao dao) {
        this.dao = dao;
    }


    public boolean create(String s) {
        return false;
    }

    public void flushCaches() {

    }

    public boolean handles(String s) {
        return false;
    }

    public boolean init(Properties properties) {
        return false;
    }

    public List list() {
        return null;  
    }

    public boolean load(String s, Entity.Accessor accessor) {
        return false;
    }

    public boolean remove(String s) {
        return false;
    }

    public boolean store(String s, Entity.Accessor accessor) {
        return false;
    }

}
