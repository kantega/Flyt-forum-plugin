package no.kantega.osuser.provider.hibernate3;

import com.opensymphony.user.provider.CredentialsProvider;
import com.opensymphony.user.provider.hibernate.entity.HibernateUser;
import com.opensymphony.user.provider.hibernate.impl.HibernateUserImpl;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 12.sep.2005
 * Time: 21:29:56
 * To change this template use File | Settings | File Templates.
 */
public class SpringHibernateCredentialsProvider extends SpringHibernateBaseProvider implements CredentialsProvider {

    public boolean authenticate(String name, String password) {
        HibernateUser user = dao.getUserByName(name);
        if(user != null) {
            return user.authenticate(password);
        } else {
            return false;
        }
    }

    public boolean changePassword(String name, String password) {
        HibernateUser user = dao.getUserByName(name);
        if(user != null) {
            user.setPassword(password);
            dao.saveOrUpdate(user);
            return true;
        } else {
            return false;
        }
    }

    public boolean create(String name) {
        HibernateUser user = new HibernateUserImpl();
        user.setName(name);
        dao.saveOrUpdate(user);
        return true;
    }

    public boolean handles(String name) {
        return dao.getUserByName(name) != null;
    }

    public boolean remove(String name) {
        return dao.deleteUserByName(name);
    }

    public List list() {
        return dao.getUserNameList();
    }
}
