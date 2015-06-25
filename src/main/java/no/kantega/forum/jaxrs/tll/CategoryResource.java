package no.kantega.forum.jaxrs.tll;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.jaxrs.bol.Fault;
import no.kantega.forum.jaxrs.tol.CategoryReferenceTo;
import no.kantega.forum.jaxrs.tol.CategoryReferencesTo;
import no.kantega.forum.jaxrs.tol.CategoryTo;
import no.kantega.forum.jaxrs.tol.ForumReferenceTo;
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
import java.util.Set;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
@Path("/category")
@Consumes({"application/json", "application/xml"})
@Produces({"application/json", "application/xml"})
public class CategoryResource {

    private final Logger log = LoggerFactory.getLogger(CategoryResource.class);

    private ForumDao forumDao;
    private PermissionManager permissionManager;
    private UserResolver userResolver;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpServletRequest request;

    @Inject
    public CategoryResource(@Named("forumDao") ForumDao forumDao, @Named("forumPermissionManager") PermissionManager permissionManager, @Named("userResolver") UserResolver userResolver) {
        this.forumDao = forumDao;
        this.permissionManager = permissionManager;
        this.userResolver = userResolver;
    }

    @GET
    public CategoryReferencesTo getAll() {
        log.trace("getAll()");
        CategoryReferencesTo categoryReferenceTos = new CategoryReferencesTo();
        String user = resolveUser();
        for (ForumCategory forumCategoryBo : forumDao.getForumCategories()) {
            if (permissionManager.hasPermission(user, Permission.VIEW, forumCategoryBo)) {
                categoryReferenceTos.add(new CategoryReferenceTo(
                        forumCategoryBo.getId(),
                        forumCategoryBo.getName(),
                        forumCategoryBo.getDescription(),
                        uriInfo.getAbsolutePathBuilder().path(String.format("%d", forumCategoryBo.getId())).build(),
                        "read",
                        "Read category",
                        null,
                        null,
                        "GET",
                        null,
                        null
                ));
            }
        }
        return categoryReferenceTos;
    }

    @Path("{categoryId}")
    @GET
    public CategoryTo get(@PathParam("categoryId") Long categoryId) {
        log.trace("get(Long)");
        ForumCategory categoryBo = forumDao.getForumCategory(categoryId);
        if (categoryBo == null) {
            throw new Fault(404, "Not found");
        }
        String user = resolveUser();
        if (!permissionManager.hasPermission(user, Permission.VIEW, categoryBo)) {
            throw new Fault(403, "Not authorized");
        }
        List<ResourceReferenceTo> actions = getActions(categoryBo, user);
        return new CategoryTo(categoryBo, forumReferenceTos(categoryBo.getForums()), actions);
    }

    private List<ResourceReferenceTo> getActions(ForumCategory categoryBo, String user) {
        List<ResourceReferenceTo> actions = new ArrayList<>();
        if (categoryBo != null) {
            if (permissionManager.hasPermission(user, Permission.VIEW, categoryBo)) {
                actions.add(new ResourceReferenceTo(
                        uriInfo.getBaseUriBuilder().path("category").path("{categoryId}").build(categoryBo.getId()),
                        "read",
                        "Read category",
                        null,
                        null,
                        "GET",
                        null,
                        null
                ));
            }
            if (permissionManager.hasPermission(user, Permission.EDIT_CATEGORY, categoryBo)) {
                actions.add(new ResourceReferenceTo(
                        uriInfo.getBaseUriBuilder().path("category").path("{categoryId}").build(categoryBo.getId()),
                        "update",
                        "Update category",
                        null,
                        null,
                        "PUT",
                        null,
                        null
                ));
                actions.add(new ResourceReferenceTo(
                        uriInfo.getBaseUriBuilder().path("category").path("{categoryId}").build(categoryBo.getId()),
                        "delete",
                        "Delete category",
                        null,
                        null,
                        "DELETE",
                        null,
                        null
                ));
            }
        }
        return actions;
    }

    public ForumDao getForumDao() {
        return forumDao;
    }

    private List<ForumReferenceTo> forumReferenceTos(Set<Forum> forumBos) {
        List<ForumReferenceTo> forumReferencesTo = null;
        if (forumBos != null) {
            forumReferencesTo = new ArrayList<>(forumBos.size());
            for (Forum forumBo : forumBos) {
                forumReferencesTo.add(new ForumReferenceTo(
                        forumBo.getId(),
                        forumBo.getName(),
                        forumBo.getDescription(),
                        uriInfo.getBaseUriBuilder().path("forum").path("{forumId}").build(forumBo.getId()),
                        "forum",
                        "Forum",
                        null,
                        null,
                        "GET",
                        null,
                        null

                ));
            }
        }
        return forumReferencesTo;
    }

    private String resolveUser() {
        ResolvedUser resolvedUser = userResolver.resolveUser(request);
        return resolvedUser != null ? resolvedUser.getUsername() : null;
    }
}
