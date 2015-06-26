package no.kantega.forum.jaxrs.jersey;

import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
public class ResourceConfig extends org.glassfish.jersey.server.ResourceConfig {

    public ResourceConfig() {
        register(RequestContextFilter.class);
        register(PostToMessageBodyReader.class);
        register(ThrowableMapper.class);
        register(FaultMapper.class);
        register(new Binder());
        packages(
                "no.kantega.forum.jaxrs.tll");
    }
}

