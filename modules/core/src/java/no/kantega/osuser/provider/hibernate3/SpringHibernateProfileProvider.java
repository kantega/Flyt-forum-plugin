package no.kantega.osuser.provider.hibernate3;

import com.opensymphony.user.provider.ProfileProvider;
import com.opensymphony.user.Entity;

import java.util.Properties;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.hibernate.SessionFactory;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 11.sep.2005
 * Time: 23:59:01
 * To change this template use File | Settings | File Templates.
 */
public class SpringHibernateProfileProvider extends SpringHibernateBaseProvider {

    public com.opensymphony.module.propertyset.PropertySet getPropertySet(String s) {
        return null;
    }

    public boolean handles(String s) {
        return super.handles(s);
    }
}
