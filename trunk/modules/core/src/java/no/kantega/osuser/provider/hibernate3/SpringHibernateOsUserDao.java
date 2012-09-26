package no.kantega.osuser.provider.hibernate3;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import com.opensymphony.user.provider.hibernate.entity.HibernateGroup;
import com.opensymphony.user.provider.hibernate.entity.HibernateUser;
import com.opensymphony.user.provider.hibernate.impl.HibernateGroupImpl;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 12.sep.2005
 * Time: 21:13:58
 * To change this template use File | Settings | File Templates.
 */
public class SpringHibernateOsUserDao {

    private SessionFactory sessionFactory;
    private HibernateTemplate template;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setTemplate(HibernateTemplate template) {
        this.template = template;
    }


    public HibernateGroup getGroupByName(String groupname) {
        return (HibernateGroup) template.find("from HibernateGroupImpl group where group.name like ?", groupname).get(0);
    }

    public HibernateUser getUserByName(String username) {
        return (HibernateUser) template.find("from HibernateUserImpl user where user.name like ?");
    }

    public void saveOrUpdate(HibernateGroup group) {
        template.saveOrUpdate(group);
    }

    public boolean isUserInGroup(String user, String group) {
        List list = template.find("from HibernateUserImpl user left join user.groups group where user.name=? and group.name=?", new String[] {user, group});
        return list.size() > 0;
    }


    public List listGroupsContainingUser(String username) {
        List groups = template.find("select group.name from HibernateGroupImpl group left join groups.user where user.name=?", username);
        return groups;
    }

    public void saveOrUpdate(HibernateUser user) {
        template.saveOrUpdate(user);
    }

    public boolean deleteUserByName(String name) {
        HibernateUser user  = getUserByName(name);
        template.delete(user);
        return true;
    }

    public List listUsersInGroup(String group) {
        return null;
    }

    public void removeFromGroup(String username, String groupname) {
    }

    public List getUserNameList() {
        return null;
    }
}
