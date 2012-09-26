package no.kantega.osuser.config;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 11.sep.2005
 * Time: 23:10:04
 * To change this template use File | Settings | File Templates.
 */
public class Provider {
    private String className;
    private Properties properties;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
