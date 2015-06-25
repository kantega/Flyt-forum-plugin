package no.kantega.forum.jaxrs.tll;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.jaxrs.bol.Fault;
import no.kantega.forum.jaxrs.tol.CategoryReferenceTo;
import no.kantega.forum.jaxrs.tol.ForumReferenceTo;
import no.kantega.forum.jaxrs.tol.ForumReferencesTo;
import no.kantega.forum.jaxrs.tol.ForumTo;
import no.kantega.forum.jaxrs.tol.ResourceReferenceTo;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumCategory;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.modules.user.UserResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
@Path("/forum")

@Consumes({"application/json", "application/xml"})
@Produces({"application/json", "application/xml"})
public class ForumResource {

    private final Logger log = LoggerFactory.getLogger(ForumResource.class);

    private ForumDao forumDao;
    private PermissionManager permissionManager;
    private UserResolver userResolver;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpServletRequest request;

    @Inject
    public ForumResource(@Named("forumDao") ForumDao forumDao, @Named("forumPermissionManager") PermissionManager permissionManager, @Named("userResolver") UserResolver userResolver) {
        this.forumDao = forumDao;
        this.permissionManager = permissionManager;
        this.userResolver = userResolver;
    }

    @GET
    public ForumReferencesTo getAll() {
        log.trace("getAll()");
        ForumReferencesTo forumReferencesTo = new ForumReferencesTo();
        String user = resolveUser();
        for (Forum forumBo : forumDao.getForums()) {
            if (permissionManager.hasPermission(user, Permission.VIEW, forumBo))
            forumReferencesTo.add(toReference(forumBo, "read", "Read forum", "GET"));
        }
        return forumReferencesTo;
    }

    @Path("{forumId}")
    @GET
    public ForumTo get(@PathParam("forumId") Long forumId) {
        log.trace("get(Long)");
        Forum forumBo = forumDao.getForum(forumId);
        if (forumBo == null) {
            throw new Fault(404, "Not found");
        }
        String user = resolveUser();
        if (!permissionManager.hasPermission(user, Permission.VIEW, forumBo)) {
            throw new Fault(403, "Not authorized");
        }
        return new ForumTo(forumBo, categoryReferenceTo(forumBo.getForumCategory()), getActions(forumBo, user));
    }

    private CategoryReferenceTo categoryReferenceTo(ForumCategory categoryBo) {
        CategoryReferenceTo categoryReferenceTo = null;
        if (categoryBo != null) {
            categoryReferenceTo = new CategoryReferenceTo(
                    categoryBo.getId(),
                    categoryBo.getName(),
                    categoryBo.getDescription(),
                    uriInfo.getBaseUriBuilder().path("category").path(String.format("%d", categoryBo.getId())).build(),
                    "category",
                    "Category",
                    null,
                    null,
                    "GET",
                    null,
                    null
            );
        }
        return categoryReferenceTo;
    }

    private List<ResourceReferenceTo> getActions(Forum forumBo, String user) {
        List<ResourceReferenceTo> actions = new ArrayList<>();
        if (forumBo != null) {
            if (permissionManager.hasPermission(user, Permission.VIEW, forumBo)) {
                actions.add(toReference(forumBo, "read", "Read forum", "GET"));
            }
            if (permissionManager.hasPermission(user, Permission.EDIT_FORUM, forumBo)) {
                actions.add(toReference(forumBo, "update", "Update forum", "PUT"));
                actions.add(toReference(forumBo, "delete", "Delete forum", "DELETE"));
            }
        }
        return actions;
    }

    private ForumReferenceTo toReference(Forum forumBo, String rel, String title, String method) {
        return new ForumReferenceTo(
                forumBo.getId(),
                forumBo.getName(),
                forumBo.getDescription(),
                uriInfo.getBaseUriBuilder().path("forum").path("{forumId}").build(forumBo.getId()),
                rel,
                title,
                null,
                null,
                method,
                null,
                null
        );
    }

    private String resolveUser() {
        ResolvedUser resolvedUser = userResolver.resolveUser(request);
        return resolvedUser != null ? resolvedUser.getUsername() : null;
    }

}
