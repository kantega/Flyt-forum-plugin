package no.kantega.forum.jaxrs.tll;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.jaxrs.bol.Fault;
import no.kantega.forum.jaxrs.tol.PostTo;
import no.kantega.forum.jaxrs.tol.ThreadTo;
import no.kantega.forum.jaxrs.tol.ThreadsTo;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static no.kantega.forum.jaxrs.tll.Util.*;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
@Path("/thread")
@Consumes({"application/json"})
@Produces({"application/json"})
public class ThreadResource {

    public static final int DEFAULT_NUMBER_OF_THREADS = 5;
    private final Logger log = LoggerFactory.getLogger(ThreadResource.class);

    private ForumDao forumDao;
    private PermissionManager permissionManager;
    private UserResolver userResolver;
    private RatingService ratingService;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpServletRequest request;

    @Inject
    public ThreadResource(@Named("forumDao") ForumDao forumDao, @Named("forumPermissionManager") PermissionManager permissionManager, @Named("userResolver") UserResolver userResolver, @Named("ratingService") RatingService ratingService) {
        this.forumDao = forumDao;
        this.permissionManager = permissionManager;
        this.userResolver = userResolver;
        this.ratingService = ratingService;
    }

    @GET
    public ThreadsTo getAll(@QueryParam("username") String username, @QueryParam("startAtThreadId") Long startAtThreadId, @QueryParam("endAtThreadId") Long endAtThreadId, @QueryParam("numberOfThreads") Integer numberOfThreads, @QueryParam("includePosts") Boolean includePosts, @QueryParam("threadId") Long threadId) {
        log.trace("getAll(String,Long,Long,Integer,Boolean)");
        if (startAtThreadId != null && endAtThreadId != null) {
            throw new Fault(400, "Mutually exclusive: startAtThreadId, endAtThreadId");
        }
        String user = resolveUser(userResolver, request);
        username = getOrDefault(username, user);
        numberOfThreads = getOrDefault(numberOfThreads, DEFAULT_NUMBER_OF_THREADS);
        includePosts = getOrDefault(includePosts, false);

        List<ForumThread> threadsBo = null;
        if (threadId != null) {
            ForumThread threadBo = forumDao.getThread(threadId, true);
            if (threadBo == null) {
                throw new Fault(404, "Not found");
            }
            threadsBo = Collections.singletonList(threadBo);
        } else if (startAtThreadId != null) {
            threadsBo = forumDao.getThreadsStartingAt(username, startAtThreadId, numberOfThreads, includePosts);
        } else if (endAtThreadId != null) {
            threadsBo = forumDao.getThreadsEndingAt(username, endAtThreadId, numberOfThreads, includePosts);
        } else {
            threadsBo = forumDao.getThreads(username, numberOfThreads, includePosts);
        }
        List<ThreadTo> threadTos = new ArrayList<>();
        if (!threadsBo.isEmpty()) {
            endAtThreadId = threadsBo.get(0).getId();
            startAtThreadId = threadsBo.get(threadsBo.size() - 1).getId();
        }
        for (ForumThread threadBo : threadsBo) {
            if (permissionManager.hasPermission(user, Permission.VIEW, threadBo)) {
                List<Rating> ratings = getRatings(ratingService, threadBo);
                threadTos.add(new ThreadTo(threadBo, forumReferenceTo(threadBo.getForum(), uriInfo), includePosts ? postsTo(threadBo, user, permissionManager, uriInfo, ratingService, request, ratings) : null, getActions(threadBo, user, permissionManager, uriInfo)));
            }
        }
        return new ThreadsTo(threadTos, getActions(username, startAtThreadId, endAtThreadId, numberOfThreads, includePosts, uriInfo));
    }

    @Path("{threadId}")
    @GET
    @Consumes({"application/json", "multipart/form-data"})
    public ThreadTo get(@PathParam("threadId") Long threadId) {
        log.trace("get(Long)");
        ForumThread threadBo = forumDao.getThread(threadId, true);
        if (threadBo == null) {
            throw new Fault(404, "Not found");
        }
        String user = resolveUser(userResolver, request);
        if (!permissionManager.hasPermission(user, Permission.VIEW, threadBo)) {
            throw new Fault(403, "Not authorized");
        }
        List<Rating> ratings = getRatings(ratingService, threadBo);
        return new ThreadTo(threadBo, forumReferenceTo(threadBo.getForum(), uriInfo), postsTo(threadBo, user, permissionManager, uriInfo, ratingService, request, ratings), getActions(threadBo, user, permissionManager, uriInfo));
    }

    @Path("{threadId}")
    @DELETE
    @Consumes({"application/json", "multipart/form-data"})
    public void delete(@PathParam("threadId") Long threadId) {
        log.trace("get(Long)");
        ForumThread threadBo = forumDao.getThread(threadId, true);
        if (threadBo == null) {
            throw new Fault(404, "Not found");
        }
        String user = resolveUser(userResolver, request);
        if (!permissionManager.hasPermission(user, Permission.DELETE_THREAD, threadBo)) {
            throw new Fault(403, "Not authorized");
        }
        forumDao.delete(threadBo);
    }

    @Path("{threadId}")
    @POST
    @Consumes({"application/json", "multipart/form-data"})
    public PostTo createPost(@PathParam("threadId") Long threadId, PostTo postTo) {
        log.trace("createPost(Long,PostTo)");
        ForumThread threadBo = forumDao.getThread(threadId, true);
        if (threadBo == null) {
            throw new Fault(404, "Not found");
        }
        String user = resolveUser(userResolver, request);
        if (!permissionManager.hasPermission(user, Permission.POST_IN_THREAD, threadBo)) {
            throw new Fault(403, "Not authorized");
        }
        Post postBo = new Post();
        postBo.setOwner(user);
        postBo.setAuthor(user);
        postBo.setThread(threadBo);
        postBo.setApproved(!threadBo.getForum().isApprovalRequired());
        postBo.setBody(postTo.getBody());
        postBo.setModifiedDate(new Date());
        postBo.setNumberOfRatings(0);
        postBo.setPostDate(new Date());
        postBo.setRatingScore(0F);
        postBo.setSubject(postTo.getSubject());
        //postBo.setReplyToId();
        postBo = forumDao.saveOrUpdate(postBo);
        List<Rating> ratings = getRatings(ratingService, postBo);
        return new PostTo(postBo, toReference(threadBo, "read", "Read thread", "GET", uriInfo), getLikes(request, ratings, postBo), /*TODO*/ null, getActions(postBo, user, permissionManager, uriInfo, ratingService, request));
    }

    public ForumDao getForumDao() {
        return forumDao;
    }



    private <T> T getOrDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
}
