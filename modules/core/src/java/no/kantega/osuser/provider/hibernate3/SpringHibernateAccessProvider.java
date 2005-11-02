package no.kantega.osuser.provider.hibernate3;

import com.opensymphony.user.provider.AccessProvider;
import com.opensymphony.user.provider.hibernate.entity.HibernateGroup;
import com.opensymphony.user.provider.hibernate.entity.HibernateUser;
import com.opensymphony.user.provider.hibernate.impl.HibernateGroupImpl;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 12.sep.2005
 * Time: 21:28:44
 * To change this template use File | Settings | File Templates.
 */
public class SpringHibernateAccessProvider extends SpringHibernateBaseProvider implements AccessProvider {
    public boolean addToGroup(String username, String groupname) {
        HibernateGroup group = dao.getGroupByName(groupname);
        HibernateUser user = dao.getUserByName(username);

        if(group != null && user != null) {
            group.addUser(user);
            dao.saveOrUpdate(group);
            return true;
        } else {
            return false;
        }
    }

    public boolean create(String name) {
        HibernateGroup group = new HibernateGroupImpl();
        group.setName(name);
        dao.saveOrUpdate(group);
        return true;
    }

    public boolean inGroup(String user, String name) {
        return dao.isUserInGroup(user, name);
    }

    public List listGroupsContainingUser(String username) {
        return dao.listGroupsContainingUser(username);
    }

    public List listUsersInGroup(String group) {
        return dao.listUsersInGroup(group);
    }

    public boolean removeFromGroup(String username, String groupname) {
        dao.removeFromGroup(username, groupname);
        return true;
    }

}
