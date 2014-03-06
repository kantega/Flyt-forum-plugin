package no.kantega.modules.commons.conf;

import no.kantega.commons.configuration.Configuration;
import no.kantega.commons.exception.ConfigurationException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Nov 2, 2005
 * Time: 5:20:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class AksessConfigurationResourceProvider implements ConfigurationResourceProvider {

    private Logger log = Logger.getLogger(getClass());

    public Resource[] getConfigResources() {
        try {
            log.info("Forum: using configuration from Aksess");
            return new Resource[] {new FileSystemResource(Configuration.getConfigDirectory() +"/aksess.conf")};
        } catch (Exception e) {
            log.error(e);
            return new Resource[0];
        }
    }
}
