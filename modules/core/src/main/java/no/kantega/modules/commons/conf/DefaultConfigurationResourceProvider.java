package no.kantega.modules.commons.conf;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.context.ResourceLoaderAware;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Nov 2, 2005
 * Time: 3:42:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultConfigurationResourceProvider implements ConfigurationResourceProvider, ResourceLoaderAware {

    private List resources;

    private ResourceLoader resourceLoader;

    public Resource[] getConfigResources() {
        Resource[] res = new Resource[resources.size()];

        for (int i = 0; i < res.length; i++) {
            res[i] = resourceLoader.getResource((String)resources.get(i));

        }
        return res;
    }

    public void setResources(List resources) {
        this.resources = resources;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
