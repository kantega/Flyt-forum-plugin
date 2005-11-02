package no.kantega.projectweb.conf;

import org.springframework.core.io.Resource;

public interface ConfigurationResourceProvider {

    public Resource[] getConfigResources();

}
