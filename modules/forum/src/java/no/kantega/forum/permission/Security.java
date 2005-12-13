package no.kantega.forum.permission;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.User;
import no.kantega.forum.model.Role;
import no.kantega.forum.model.Group;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 12.des.2005
 * Time: 09:27:17
 * To change this template use File | Settings | File Templates.
 */
public class Security {
    private ForumDao dao;

    private boolean userHasRole(User user, Role role) {
        Set users = role.getUsers();
        if (users.contains(user)) {
            return true;
        }

        return false;
    }

    private boolean userHasRole(String userName, Role role) {
        User user = dao.getUser(userName);
        Set users = role.getUsers();
        if (users.contains(user)) {
            return true;
        }

        return false;
    }

    private boolean userHasRole(String userName, Roles r) {
        User user = dao.getUser(userName);
        Role role = dao.getRole(r);
        Set users = role.getUsers();
        if (users.contains(user)) {
            return true;
        }

        return false;
    }

    private boolean userHasAccess(User user, Group group) {
        Set users = group.getUsers();
        if (users.contains(user)) {
            return true;
        }

        return false;
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
