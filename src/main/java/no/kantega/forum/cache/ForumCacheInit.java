package no.kantega.forum.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import no.kantega.publishing.api.configuration.SystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class ForumCacheInit implements BeanFactoryPostProcessor {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private SystemConfiguration configuration;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        log.info("Creating ForumUserProfileManager");
        Ehcache forumUserProfileCache = cacheManager.addCacheIfAbsent(new Cache(
                new CacheConfiguration()
                        .name("ForumUserProfileManager")
                        .eternal(false)
                        .statistics(true)
                        .maxEntriesLocalHeap(configuration.getInt("ForumUserProfileManager.maxEntriesLocalHeap", 300))
                        .timeoutMillis(configuration.getInt("ForumUserProfileManager.timeoutMillis", 150))
                        .timeToLiveSeconds(configuration.getInt("ForumUserProfileManager.timeToLiveSeconds", 600))
                        .timeToIdleSeconds(configuration.getInt("ForumUserProfileManager.timeToIdleSeconds", 300))
                        .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
        ));

        log.info("Creating ForumGroupCache");
        Ehcache forumGroupCache = cacheManager.addCacheIfAbsent(new Cache(
                new CacheConfiguration()
                        .name("ForumGroupCache")
                        .eternal(false)
                        .statistics(true)
                        .maxEntriesLocalHeap(configuration.getInt("ForumGroupCache.maxEntriesLocalHeap", 150))
                        .timeoutMillis(configuration.getInt("ForumGroupCache.timeoutMillis", 150))
                        .timeToLiveSeconds(configuration.getInt("ForumGroupCache.timeToLiveSeconds", 600))
                        .timeToIdleSeconds(configuration.getInt("ForumGroupCache.timeToIdleSeconds", 300))
                        .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
        ));


    }
}
