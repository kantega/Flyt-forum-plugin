package no.kantega.exchange.util;

import com.intrinsyc.cdo.Session;

import java.util.concurrent.Callable;
import java.net.UnknownHostException;
import java.net.ConnectException;

import no.kantega.commons.log.Log;

/**
 * User: tarkil
 * Date: Jul 18, 2008
 * Time: 12:29:50 PM
 */
public class JIntegraCreateCdoSessionWrapper implements Callable {

    private static final String SOURCE = JIntegraCreateCdoSessionWrapper.class.toString();

    private String cdoServerAddress;


    public JIntegraCreateCdoSessionWrapper(String cdoServerAddress) {
        this.cdoServerAddress = cdoServerAddress;
    }

    public Object call() throws Exception {
        long start = System.nanoTime();
        Session session = new Session(cdoServerAddress);
        Log.debug(SOURCE, "Created session with server: \"" + cdoServerAddress + "\" in " + ((System.nanoTime() - start) / 1000000.0) + " millisecs", null, null);
        return session;
    }

}
