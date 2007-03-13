package no.kantega.forum.util;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: May 23, 2006
 * Time: 1:25:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class ForumUtil {

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
}
