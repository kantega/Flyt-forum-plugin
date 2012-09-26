package no.kantega.modules.commons.conf;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.BeansException;
import org.apache.log4j.Logger;

public class SpringPropertyReplacer implements BeanFactoryPostProcessor {
    private ConfigurationResourceProvider provider;

    private Logger log = Logger.getLogger(getClass());
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
        cfg.setLocations(provider.getConfigResources());
        cfg.postProcessBeanFactory(configurableListableBeanFactory);
    }

    public void setProvider(ConfigurationResourceProvider provider) {
        this.provider = provider;
    }
}
