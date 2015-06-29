package no.kantega.forum.jaxrs.tll;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.jaxrs.bol.Fault;
import no.kantega.forum.jaxrs.tol.ForumReferencesTo;
import no.kantega.forum.jaxrs.tol.ForumTo;
import no.kantega.forum.jaxrs.tol.ThreadTo;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.modules.user.UserResolver;
import no.kantega.publishing.api.rating.Rating;
import no.kantega.publishing.api.rating.RatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.Date;
import java.util.List;

import static no.kantega.forum.jaxrs.tll.Util.*;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
@Path("/forum")

@Consumes({"application/json"})
@Produces({"application/json"})
public class ForumResource {

    private final Logger log = LoggerFactory.getLogger(ForumResource.class);

    private ForumDao forumDao;
    private PermissionManager permissionManager;
    private UserResolver userResolver;
    private RatingService ratingService;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpServletRequest request;

    @Inject
    public ForumResource(@Named("forumDao") ForumDao forumDao, @Named("forumPermissionManager") PermissionManager permissionManager, @Named("userResolver") UserResolver userResolver, @Named("ratingService") RatingService ratingService) {
        this.forumDao = forumDao;
        this.permissionManager = permissionManager;
        this.userResolver = userResolver;
        this.ratingService = ratingService;
    }

    @GET
    public ForumReferencesTo getAll() {
        log.trace("getAll()");
        ForumReferencesTo forumReferencesTo = new ForumReferencesTo();
        String user = resolveUser(userResolver, request);
        for (Forum forumBo : forumDao.getForums()) {
            if (permissionManager.hasPermission(user, Permission.VIEW, forumBo))
            forumReferencesTo.add(toReference(forumBo, "read", "Read forum", "GET", uriInfo));
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
        String user = resolveUser(userResolver, request);
        if (!permissionManager.hasPermission(user, Permission.VIEW, forumBo)) {
            throw new Fault(403, "Not authorized");
        }
        return new ForumTo(forumBo, categoryReferenceTo(forumBo.getForumCategory(), uriInfo), getActions(forumBo, user, permissionManager, uriInfo));
    }

    @Path("{forumId}")
    @POST
    public ThreadTo createThread(@PathParam("forumId") Long forumId, ThreadTo threadTo) {
        log.trace("createThread(Long,ThreadTo)");
        Forum forumBo = forumDao.getForum(forumId);
        if (forumBo == null) {
            throw new Fault(404, "Not found");
        }
        String user = resolveUser(userResolver, request);
        if (!permissionManager.hasPermission(user, Permission.ADD_THREAD, forumBo)) {
            throw new Fault(403, "Not authorized");
        }
        ForumThread threadBo = new ForumThread();
        threadBo.setModifiedDate(new Date());
        threadBo.setContentId(threadTo.getContentId());
        threadBo.setCreatedDate(new Date());
        threadBo.setDescription(threadBo.getDescription());
        threadBo.setForum(forumBo);
        threadBo.setName(threadBo.getName());
        threadBo.setNumNewPosts(0);
        threadBo.setNumPosts(0);
        threadBo.setOwner(user);
        threadBo.setApproved(!forumBo.isApprovalRequired());
        threadBo = forumDao.saveOrUpdate(threadBo);
        List<Rating> ratings = getRatings(ratingService, threadBo);
        return new ThreadTo(threadBo, forumReferenceTo(threadBo.getForum(), uriInfo), postsTo(threadBo, user, permissionManager, uriInfo, ratingService, request, ratings), getActions(threadBo, user, permissionManager, uriInfo));

    }

}
