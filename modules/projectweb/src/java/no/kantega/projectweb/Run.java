package no.kantega.projectweb;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 16:36:00
 * To change this template use File | Settings | File Templates.
 */
public class Run {
    public static void main(String[] args) {

       FileSystemXmlApplicationContext context  = new FileSystemXmlApplicationContext("../webapp/src/webapp/WEB-INF/projectweb-servlet.xml");


    }
}
