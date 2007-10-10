package no.kantega.exchange.util;

import com.intrinsyc.cdo.Session;
import com.linar.jintegra.AutomationException;

import javax.servlet.http.HttpServletRequest;

import no.kantega.commons.exception.ConfigurationException;
import no.kantega.commons.configuration.Configuration;
import no.kantega.commons.log.Log;
import no.kantega.publishing.common.Aksess;

import java.net.NoRouteToHostException;
import java.io.IOException;

/**
 * User: Espen A. Fossen @ Kantega
 * Date: Oct 9, 2007
 * Time: 12:12:35 PM
 */
public class ExchangeSession {
    private static String SOURCE = "ExchangeSession";
    private static Session session = null;

    public Session getInstance(String mailbox, HttpServletRequest request) throws ConfigurationException {

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


        try {
            // use credentials of service account to authenticate to Windows
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
                if(mailbox.equalsIgnoreCase(mailboxnew)){
                    mailboxreuse = true;
                }else{
                    mailboxreuse = false;
                    session.logoff();
                }

            } catch (Exception e) {
            }

            if (!mailboxreuse) {
                // logon to the Exchange Server
                session.logon(null, null, new Boolean(false), new Boolean(true),
                        new Boolean(false), new Boolean(false),
                        exchangeServer + "\n" + mailbox);
            }

        } catch (NoRouteToHostException e) {

        } catch (AutomationException e) {

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
}
