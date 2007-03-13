package no.kantega.osuser.config;

import com.opensymphony.user.util.ConfigLoader;
import com.opensymphony.user.UserManager;
import com.opensymphony.user.provider.UserProvider;
import com.opensymphony.workflow.loader.ClassLoaderUtil;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 11.sep.2005
 * Time: 23:00:51
 * To change this template use File | Settings | File Templates.
 */
public class SpringConfigLoader implements ConfigLoader {

    private Authenticator authenticator;
    private List providers;

    public void load(UserManager userManager) {

        try {
            System.out.println("SpringCOnfigloader loading configuration");
            com.opensymphony.user.authenticator.Authenticator a = (com.opensymphony.user.authenticator.Authenticator) ClassLoaderUtil.loadClass(authenticator.getClassName(), this.getClass()).newInstance();
            a.init(authenticator.getProperties());
            userManager.setAuthenticator(a);

            for (int i = 0; i < providers.size(); i++) {
                Provider provider = (Provider) providers.get(i);

                UserProvider up  = (UserProvider) ClassLoaderUtil.loadClass(provider.getClassName(), this.getClass()).newInstance();
                up.init(provider.getProperties());
                userManager.addProvider(up);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    public void setProviders(List providers) {
        this.providers = providers;
    }


}
