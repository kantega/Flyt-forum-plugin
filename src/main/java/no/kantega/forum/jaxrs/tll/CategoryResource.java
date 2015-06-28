package no.kantega.forum.jaxrs.tll;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.jaxrs.bol.Fault;
import no.kantega.forum.jaxrs.tol.CategoryReferenceTo;
import no.kantega.forum.jaxrs.tol.CategoryReferencesTo;
import no.kantega.forum.jaxrs.tol.CategoryTo;
import no.kantega.forum.jaxrs.tol.ResourceReferenceTo;
import no.kantega.forum.model.ForumCategory;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionManager;
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
import java.util.List;

import static no.kantega.forum.jaxrs.tll.Util.*;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
@Path("/category")
@Consumes({"application/json"})
@Produces({"application/json"})
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
        String user = resolveUser(userResolver, request);
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
        String user = resolveUser(userResolver, request);
        if (!permissionManager.hasPermission(user, Permission.VIEW, categoryBo)) {
            throw new Fault(403, "Not authorized");
        }
        List<ResourceReferenceTo> actions = getActions(categoryBo, user, permissionManager, uriInfo);
        return new CategoryTo(categoryBo, forumReferenceTos(categoryBo.getForums(), uriInfo), actions);
    }

    public ForumDao getForumDao() {
        return forumDao;
    }
}
