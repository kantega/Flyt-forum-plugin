package no.kantega.forum.jaxrs.tll;

import no.kantega.forum.jaxrs.tol.ResourceReferenceTo;
import no.kantega.forum.jaxrs.tol.ResourceReferencesTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
@Path("/")
@Consumes({"application/json"})
@Produces({"application/json"})
public class RootResource {

    private final Logger log = LoggerFactory.getLogger(RootResource.class);

    @Context
    private UriInfo uriInfo;

    @GET
    public ResourceReferencesTo getAll() {
        log.trace("getAll()");
        return new ResourceReferencesTo(Arrays.asList(
                new ResourceReferenceTo(
                        uriInfo.getAbsolutePathBuilder().path("category").build(),
                        "category",
                        null,
                        null,
                        null,
                        "GET",
                        null,
                        null
                ),
                new ResourceReferenceTo(
                        uriInfo.getAbsolutePathBuilder().path("forum").build(),
                        "forum",
                        null,
                        null,
                        null,
                        "GET",
                        null,
                        null
                ),
                new ResourceReferenceTo(
                        uriInfo.getAbsolutePathBuilder().path("thread").build(),
                        "thread",
                        null,
                        null,
                        null,
                        "GET",
                        null,
                        null
                )
        ));
    }
}

