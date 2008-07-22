package no.kantega.exchange.util;

import com.linar.jintegra.AuthInfo;

import javax.servlet.jsp.PageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletRequest;
import java.util.concurrent.*;

import no.kantega.commons.log.Log;
import no.kantega.commons.exception.ConfigurationException;
import no.kantega.commons.configuration.Configuration;
import no.kantega.publishing.security.SecuritySession;
import no.kantega.publishing.security.data.User;
import no.kantega.publishing.security.realm.SecurityRealm;
import no.kantega.publishing.security.realm.SecurityRealmFactory;
import no.kantega.publishing.common.Aksess;
import org.springframework.web.util.ExpressionEvaluationUtils;

/**
 * User: tarkil
 * Date: Jul 18, 2008
 * Time: 3:51:52 PM
 */
public class ExchangeManager {

    private static final String SOURCE = ExchangeManager.class.toString();

    private static String domain = "";
    private static String username = "";
    private static String password = "";
    private static String cdoServerAddress = "";
    private static String exchangeServerAddress = "";
    private static long timeout = 2000;
    private static final TimeUnit unit = TimeUnit.MILLISECONDS;

    private static ExecutorService executorService;

    static {
        try {
            Configuration config = Aksess.getConfiguration();
            domain = config.getString("jintegra.exchange.domain");
            username = config.getString("jintegra.exchange.admin.user");
            password = config.getString("jintegra.exchange.admin.pass");
            cdoServerAddress = config.getString("jintegra.exchange.cdoserver");
            exchangeServerAddress = config.getString("jintegra.exchange.server");
            timeout = config.getLong("jintegra.exchange.timeout.milliseconds", 2000);
            AuthInfo.setDefault(domain, username, password);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }


    /**
     * Returnerer et ferdig innlogget CdoSessionWrapper-objekt, eller null hvis det ikke kunne bli laget.
     * Lagrer det i request-en
     * @param id
     * @param pageContext
     * @return
     */
    public static CdoSessionWrapper getSession(String id, PageContext pageContext) {
        CdoSessionWrapper session = null;
        String userId = lookupUserId(id, pageContext);
        if (userId != null && !userId.equals("")) {
            try {
                session = doGetSession(userId, pageContext.getRequest());
                if (!session.isLoggedOn()) {
                    session.logon(exchangeServerAddress, userId);
                }
                pageContext.getRequest().setAttribute("cdosession", session);
            } catch (Exception e) {
                e.printStackTrace();
                session = null;
            }
        } else{
            Log.error(SOURCE, "Could not lookup user: \"" + id + "\"", null, null);
        }
        return session;
    }

    /**
     * Checks whether a connection to the exchange server can be established
     * @param pageContext the PageContext
     * @return whether a connection could be made to the cdo server
     * @throws Exception if an exception occures
     */
    public static boolean verifyConnection(PageContext pageContext) throws Exception {
        return doGetSession(null, pageContext.getRequest()) != null;
    }

    private static CdoSessionWrapper doGetSession(String userId, ServletRequest request) throws Exception {
        CdoSessionWrapper session = (CdoSessionWrapper)request.getAttribute("cdosession");

        // If CDOsession is present in request, if not get it
        if (session != null) {
            if (!validateSession(userId, session)) {
                try {
                    session.logoff();
                } catch (Exception e) {
                    session = null;
                }
            }
        }
        if (session == null) {
            // create a Session in the CDOmachine
            session = new CdoSessionWrapper(cdoServerAddress);
        }

        return session;
    }

    private static boolean validateSession(String userId, CdoSessionWrapper session) {
        boolean valid = false;
        // Check if requested mailbox is same as the one being used
        String mailboxnew = "";
        if (session.isLoggedOn() && userId != null) {
            String s = session.getName().toString();
            if (s != null) {
                String[] a = s.split("_");
                if (a.length > 1) {
                    mailboxnew = a[1];
                    if (userId.equalsIgnoreCase(mailboxnew)) {
                        valid = true;
                    }
                }
            }
        }
        return valid;
    }

    private static String lookupUserId(String userid, PageContext pageContext) {
        String id = "";

        try {
            // Check users credentials
            HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
            SecuritySession session = SecuritySession.getInstance(request);

            if (userid != null) {
                ExpressionEvaluationUtils.evaluateString("userid", userid, pageContext);
                SecurityRealm realm = SecurityRealmFactory.getInstance();
                realm.lookupUser(userid);
                id = userid;
            } else {
                User user = session.getUser();
                if (user != null) {
                    String uid = user.getId();
                    if (uid.indexOf(":") != -1) {
                        uid = uid.substring(uid.indexOf(":") + 1);
                    }
                    id = uid;
                }
            }
        } catch (Exception e) {
            Log.error(SOURCE, e, null, null);
        }
        return id;
    }

    public static long getTimeout() {
        return timeout;
    }

    public static TimeUnit getUnit() {
        return unit;
    }

}
