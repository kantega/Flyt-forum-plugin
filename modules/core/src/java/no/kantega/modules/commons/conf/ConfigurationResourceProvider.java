package no.kantega.modules.commons.conf;

import org.springframework.core.io.Resource;

public interface ConfigurationResourceProvider {

    public Resource[] getConfigResources();

}
