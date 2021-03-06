package no.kantega.forum.util;

import no.kantega.publishing.common.Aksess;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private static String NOSPAM_KEY = "567-234-NoSpam-Please";

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
     * @param update - oppdater ja / nei
     * @return siste besøk
     */
    public static Date getLastVisit(HttpServletRequest request, HttpServletResponse response, boolean update) {
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
        }
        
        if (update) {
            // Lagre nytt tidspunkt i cookie
            Cookie cookie = new Cookie(lastVisitCookieName, cookieDateFormat.format(new Date()));
            cookie.setMaxAge(3*30*24*60*60);//Ca 3 m�neder
            String path = Aksess.getContextPath();
            if (path == null || path.equals("")) {
                path = "/";
            }
            cookie.setPath(path);//Setter cookien tilgjengelig for hele applikasjonen, ikke bare forum.
            response.addCookie(cookie);
        }

        return lastVisit;
    }

    public static void getNoSpamCode(JspWriter out) throws IOException {
        for (int i = 0; i < NOSPAM_KEY.length(); i++) {
            out.println("a += '" + NOSPAM_KEY.charAt(i) + "';\n");
        }
    }

    public static boolean isSpam(HttpServletRequest request) {
        String code = request.getParameter("nospam");
        if ((code == null) || (code.equals(NOSPAM_KEY))) {
            return false;
        }

        return true;
    }
}
