package no.kantega.forum.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: May 23, 2006
 * Time: 1:25:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class ForumUtil {
    private static String lastVisitCookieName = "forumLastVisit";
    private static DateFormat cookieDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public String quoteString(String quoteMark, String string) {
        try {
            StringBuffer buffer = new StringBuffer();
            BufferedReader br = new BufferedReader(new StringReader(string));

            String line = null;
            while((br.readLine() != null)) {
                buffer.append(line);
            }
        } catch (IOException e) {

        }
        return "";
    }

    /***
     * Henter tidspunkt for siste besøk fra cookie.  Lagrer nåværende tidspunkt i cookie.
     * @param request
     * @param response
     * @return siste besøk
     */
    public static Date updateLastVisit(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        Date lastVisit = (Date)session.getAttribute(lastVisitCookieName);
        if (lastVisit == null) {
            // Hent fra cookie
            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (int i = 0; i < cookies.length; i++) {
                    Cookie cookie = cookies[i];
                    if (cookie.getName().equals(lastVisitCookieName)) {
                        String val = cookie.getValue();
                        try {
                            lastVisit = cookieDateFormat.parse(val);
                        } catch (ParseException e) {

                        }
                    }
                }
            }

            if (lastVisit == null) {
                lastVisit = new Date();
            }

            // Lagre i sesjon
            session.setAttribute(lastVisitCookieName, lastVisit);

            // Lagre nytt tidspunkt i cookie
            Cookie cookie = new Cookie(lastVisitCookieName, cookieDateFormat.format(new Date()));
            response.addCookie(cookie);
        }

        return lastVisit;
    }
}
