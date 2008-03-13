package no.kantega.exchange.util;

import com.intrinsyc.cdo.Session;
import com.linar.jintegra.AutomationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import no.kantega.commons.exception.ConfigurationException;
import no.kantega.commons.exception.InvalidCredentialsException;
import no.kantega.commons.configuration.Configuration;
import no.kantega.commons.log.Log;
import no.kantega.publishing.common.Aksess;
import no.kantega.publishing.security.SecuritySession;
import no.kantega.publishing.security.data.User;
import no.kantega.publishing.security.realm.SecurityRealm;
import no.kantega.publishing.security.realm.SecurityRealmFactory;

import java.net.NoRouteToHostException;
import java.io.IOException;

import org.springframework.web.util.ExpressionEvaluationUtils;

/**
 * User: Espen A. Fossen @ Kantega
 * Date: Oct 9, 2007
 * Time: 12:12:35 PM
 */
public class ExchangeSession {
    private static String SOURCE = "ExchangeSession";
    private static Session session = null;
    private String id = null;
    User user = null;

    public Session getInstance(String userid, HttpServletRequest request, PageContext pageContext) throws ConfigurationException {

        boolean mailboxreuse = false;

        Configuration config = Aksess.getConfiguration();

        // the logon parameters of the service account you used to configure CDO
        String domain = config.getString("jintegra.exchange.domain");
        String username = config.getString("jintegra.exchange.admin.user");
        String password = config.getString("jintegra.exchange.admin.pass");

        // the DNS name or IP address of the machine where you installed CDO
        String CDOmachine = config.getString("jintegra.exchange.cdoserver");

        // the DNS name or IP address of the Exchange Server
        String exchangeServer = config.getString("jintegra.exchange.server");

        if (domain != null && username != null && password != null && CDOmachine != null && exchangeServer != null) {

            try {

                // Check if user is valid before proceeding
                if (this.checkValidUser(userid, request, pageContext)) {

                    com.linar.jintegra.AuthInfo.setDefault(domain, username, password);

                    // If CDOsession is present in request, if not get it
                    if (request.getAttribute("cdosession") == null) {
                        // create a Session in the CDOmachine
                        session = new Session(CDOmachine);
                        request.setAttribute("cdosession", session);
                    } else {
                        session = (Session) request.getAttribute("cdosession");
                    }

                    String mailboxnew = "";
                    // Check if requested mailbox is same as the one being used

                    try {
                        String s = session.getName().toString();
                        String[] a = s.split("_");
                        mailboxnew = a[1];
                        if (this.id.equalsIgnoreCase(mailboxnew)) {
                            mailboxreuse = true;
                        } else {
                            mailboxreuse = false;
                            session.logoff();
                        }

                    } catch (AutomationException e) {
                        // Missing name in session, meaning there is no established session
                    } catch (Exception e) {

                    }

                    if (!mailboxreuse) {
                        // logon to the Exchange Server
                        session.logon(null, null, new Boolean(false), new Boolean(true),
                                new Boolean(false), new Boolean(false),
                                exchangeServer + "\n" + this.id);
                    }
                }else{
                    throw new InvalidCredentialsException("No credentials or userid given", SOURCE);                    
                }
            } catch (NoRouteToHostException e) {
                Log.error(SOURCE, e, null, null);//

            } catch (AutomationException e) {
                Log.error(SOURCE, e, null, null);//

                /* Error codes
                *
                * 5 = Access denied
                * 2147746065 = exchange server DNS/IP is wrong, or mailbox username is wrong or does not exist
                */
                // System.out.println(new Long(e.getCode()).toString()+"\n");
                // System.out.println(e.getDescription());
            } catch (Exception e) {
                Log.error(SOURCE, e, null, null);
            }
            return session;

        } else {
            throw new ConfigurationException("Configuration of ExchangeSession (J-Integra) is not correct");
        }
    }


    /*
     *
     *
     *
     */
    public boolean checkValidUser(String userid, HttpServletRequest request, PageContext pageContext) {

        try {
            // Check users credentials
            SecuritySession session = SecuritySession.getInstance(request);
            if (userid != null) {
                this.id = ExpressionEvaluationUtils.evaluateString("userid", userid, pageContext);
                SecurityRealm realm = SecurityRealmFactory.getInstance();
                try {
                    realm.lookupUser(userid);
                    this.id = userid;
                    return true;
                } catch (no.kantega.commons.exception.SystemException e) {
                    this.id = "";
                    throw new InvalidCredentialsException("No credentials or userid given", SOURCE);
                }
            } else {
                try {
                    user = session.getUser();
                    String uid = user.getId();
                    if (uid.indexOf(":") != -1) {
                        uid = uid.substring(uid.indexOf(":") + 1);
                    }
                    this.id = uid;
                    return true;
                } catch (Exception e) {
                    this.id = "";
                    throw new InvalidCredentialsException("No credentials or userid given", SOURCE);
                }
            }

        } catch (Exception e) {
            Log.error(SOURCE, e, null, null);
        }
        this.id = "";
        return false;
    }


    public void logout() {
        try {
            // log off from the session
            session.logoff();

        } catch (IOException e) {
            Log.error(SOURCE, e, null, null);
        } finally {
            // clean up and release all references
            com.linar.jintegra.Cleaner.releaseAll();
        }
    }

    public String getUserid() {
        return this.id;
    }
}
