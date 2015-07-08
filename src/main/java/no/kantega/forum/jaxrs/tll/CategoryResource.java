package no.kantega.forum.jaxrs.tll;

import no.kantega.forum.jaxrs.bol.CategoryBo;
import no.kantega.forum.jaxrs.bol.Fault;
import no.kantega.forum.jaxrs.bol.ForumBo;
import no.kantega.forum.jaxrs.bol.GroupDo;
import no.kantega.forum.jaxrs.dal.CategoryDao;
import no.kantega.forum.jaxrs.dal.ForumDao;
import no.kantega.forum.jaxrs.dal.GroupDao;
import no.kantega.forum.jaxrs.dal.jdbc.Jdbc;
import no.kantega.forum.jaxrs.tol.CategoryReferenceTo;
import no.kantega.forum.jaxrs.tol.CategoryReferencesTo;
import no.kantega.forum.jaxrs.tol.CategoryTo;
import no.kantega.forum.jaxrs.tol.ForumReferenceTo;
import no.kantega.forum.jaxrs.tol.ResourceReferenceTo;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.modules.user.UserResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static no.kantega.forum.jaxrs.tll.Util.resolveUser;

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
    private CategoryDao categoryDao;
    private GroupDao groupDao;
    private DataSource dataSource;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpServletRequest request;

    @Inject
    public CategoryResource(ForumDao forumDao, @Named("forumPermissionManager") PermissionManager permissionManager, @Named("userResolver") UserResolver userResolver, CategoryDao categoryDao, GroupDao groupDao, @Named("aksessDataSource") DataSource dataSource) {
        this.forumDao = forumDao;
        this.permissionManager = permissionManager;
        this.userResolver = userResolver;
        this.categoryDao = categoryDao;
        this.groupDao = groupDao;
        this.dataSource = dataSource;
    }

    @GET
    public CategoryReferencesTo getAll() {
        log.trace("getAll()");
        CategoryReferencesTo categoryReferenceTos = new CategoryReferencesTo();
        String user = resolveUser(userResolver, request);
        return Jdbc.readOnly(dataSource, connection -> {
            for (CategoryBo categoryBo : categoryDao.read(connection)) {
                if (permissionManager.hasPermission(user, Permission.VIEW, categoryBo)) {
                    categoryReferenceTos.add(new CategoryReferenceTo(
                            categoryBo.getId(),
                            categoryBo.getName(),
                            categoryBo.getDescription(),
                            uriInfo.getAbsolutePathBuilder().path(String.format("%d", categoryBo.getId())).build(),
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
        });
    }

    @Path("{categoryId}")
    @GET
    public CategoryTo get(@PathParam("categoryId") final Long categoryId) {
        log.trace("get(Long)");
        return Jdbc.readOnly(dataSource, connection -> {
            CategoryBo categoryBo = categoryDao.read(connection, categoryId);
            if (categoryBo == null) {
                throw new Fault(404, "Not found");
            }
            String user = resolveUser(userResolver, request);
            if (!permissionManager.hasPermission(user, Permission.VIEW, categoryBo)) {
                throw new Fault(403, "Not authorized");
            }
            List<ResourceReferenceTo> actions = getActions(categoryBo, user, permissionManager, uriInfo);
            List<ForumBo> forumBos = forumDao.readByCategory(connection, categoryId);

            Map<Long,List<GroupDo>> groupsGroupedByForumId = groupDao.readByForums(connection, forumBos.stream().map(ForumBo::getId).collect(Collectors.toList()));


            List<ForumReferenceTo> forumReferenceTos = forumBos.stream()
                    .filter(forumBo -> permissionManager.hasPermission(user, Permission.VIEW, forumBo, groupsGroupedByForumId.get(forumBo.getId())))
                    .map(forumBo -> new ForumReferenceTo(
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
                    ))
                    .collect(Collectors.toList());
            return new CategoryTo(categoryBo.getId(), categoryBo.getName(), categoryBo.getDescription(), categoryBo.getNumForums(), categoryBo.getCreatedDate(), categoryBo.getOwner(), forumReferenceTos, actions);
        });

    }

    public ForumDao getForumDao() {
        return forumDao;
    }

    public static List<ResourceReferenceTo> getActions(CategoryBo categoryBo, String user, PermissionManager permissionManager, UriInfo uriInfo) {
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

}
