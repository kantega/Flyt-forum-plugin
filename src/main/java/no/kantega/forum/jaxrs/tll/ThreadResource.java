package no.kantega.forum.jaxrs.tll;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.jaxrs.bol.Fault;
import no.kantega.forum.jaxrs.tol.ForumReferenceTo;
import no.kantega.forum.jaxrs.tol.PostTo;
import no.kantega.forum.jaxrs.tol.ResourceReferenceTo;
import no.kantega.forum.jaxrs.tol.ThreadsTo;
import no.kantega.forum.jaxrs.tol.ThreadTo;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
@Path("/thread")
@Consumes({"application/json", "application/xml"})
@Produces({"application/json", "application/xml"})
public class ThreadResource {

    public static final int DEFAULT_NUMBER_OF_THREADS = 5;
    private final Logger log = LoggerFactory.getLogger(ThreadResource.class);

    private ForumDao forumDao;
    private PermissionManager permissionManager;
    private UserResolver userResolver;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpServletRequest request;

    @Inject
    public ThreadResource(@Named("forumDao") ForumDao forumDao, @Named("forumPermissionManager") PermissionManager permissionManager, @Named("userResolver") UserResolver userResolver) {
        this.forumDao = forumDao;
        this.permissionManager = permissionManager;
        this.userResolver = userResolver;
    }

    @GET
    public ThreadsTo getAll(@QueryParam("username") String username, @QueryParam("startAtThreadId") Long startAtThreadId, @QueryParam("endAtThreadId") Long endAtThreadId, @QueryParam("numberOfThreads") Integer numberOfThreads, @QueryParam("includePosts") Boolean includePosts) {
        log.trace("getAll(String,Long,Long,Integer,Boolean)");
        if (startAtThreadId != null && endAtThreadId != null) {
            throw new Fault(400, "Mutually exclusive: startAtThreadId, endAtThreadId");
        }
        String user = resolveUser();
        username = getOrDefault(username, user);
        numberOfThreads = getOrDefault(numberOfThreads, DEFAULT_NUMBER_OF_THREADS);
        includePosts = getOrDefault(includePosts, false);

        List<ForumThread> threadsBo = null;
        if (startAtThreadId != null) {
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
                threadTos.add(new ThreadTo(threadBo, forumReferenceTo(threadBo.getForum()), includePosts ? postsTo(threadBo) : null, getActions(threadBo, user)));
            }
        }
        return new ThreadsTo(threadTos, getActions(username, startAtThreadId, endAtThreadId, numberOfThreads, includePosts));
    }

    @Path("{threadId}")
    @GET
    public ThreadTo get(@PathParam("threadId") Long threadId) {
        log.trace("get(Long)");
        ForumThread threadBo = forumDao.getThread(threadId, true);
        if (threadBo == null) {
            throw new Fault(404, "Not found");
        }
        String user = resolveUser();
        if (!permissionManager.hasPermission(user, Permission.VIEW, threadBo)) {
            throw new Fault(403, "Not authorized");
        }
        return new ThreadTo(threadBo, forumReferenceTo(threadBo.getForum()), postsTo(threadBo), getActions(threadBo, user));
    }

    public ForumDao getForumDao() {
        return forumDao;
    }

    private List<ResourceReferenceTo> getActions(@QueryParam("username") String username, @QueryParam("startAtThreadId") Long startAtThreadId, @QueryParam("endAtThreadId") Long endAtThreadId, @QueryParam("numberOfThreads") Integer numberOfThreads, @QueryParam("includePosts") Boolean includePosts) {
        List<ResourceReferenceTo> actions = new ArrayList<>();
        if (endAtThreadId != null) {
            actions.add(new ResourceReferenceTo(
                    uriInfo.getBaseUriBuilder().path("thread").queryParam("username", "{username}").queryParam("endAtThreadId", "{endAtThreadId}").queryParam("numberOfThreads", "{numberOfThreads}").queryParam("includePosts", "{includePosts}").build(username, endAtThreadId, numberOfThreads, includePosts),
                    "younger",
                    "Read younger threads",
                    null,
                    null,
                    "GET",
                    null,
                    null
            ));
        }
        if (startAtThreadId != null) {
            actions.add(new ResourceReferenceTo(
                    uriInfo.getBaseUriBuilder().path("thread").queryParam("username", "{username}").queryParam("startAtThreadId", "{startAtThreadId}").queryParam("numberOfThreads", "{numberOfThreads}").queryParam("includePosts", "{includePosts}").build(username, startAtThreadId, numberOfThreads, includePosts),
                    "older",
                    "Read older threads",
                    null,
                    null,
                    "GET",
                    null,
                    null
            ));
        }
        if (actions.isEmpty()) {
            actions.add(new ResourceReferenceTo(
                    uriInfo.getBaseUriBuilder().path("thread").queryParam("username", "{username}").queryParam("numberOfThreads", "{numberOfThreads}").queryParam("includePosts", "{includePosts}").build(username, numberOfThreads, includePosts),
                    "refresh",
                    "Refresh threads",
                    null,
                    null,
                    "GET",
                    null,
                    null
            ));
        }
        return actions;
    }

    private List<ResourceReferenceTo> getActions(ForumThread threadBo, String user) {
        List<ResourceReferenceTo> actions = new ArrayList<>();
        if (threadBo != null) {
            if (permissionManager.hasPermission(user, Permission.VIEW, threadBo)) {
                actions.add(toReference(threadBo, "read", "Read thread", "GET"));
            }
            if (permissionManager.hasPermission(user, Permission.EDIT_THREAD, threadBo)) {
                actions.add(toReference(threadBo, "update", "Update thread", "PUT"));
            }
            if (permissionManager.hasPermission(user, Permission.DELETE_THREAD, threadBo)) {
                actions.add(toReference(threadBo, "delete", "Delete thread", "DELETE"));
            }
            if (permissionManager.hasPermission(user, Permission.POST_IN_THREAD, threadBo)) {
                actions.add(toReference(threadBo, "create", "Create post", "POST"));
            }
        }
        return actions;
    }

    private ResourceReferenceTo toReference(ForumThread threadBo, String rel, String title, String method) {
        return new ResourceReferenceTo(
                uriInfo.getBaseUriBuilder().path("thread").path("{threadId}").build(threadBo.getId()),
                rel,
                title,
                null,
                null,
                method,
                null,
                null
        );
    }

    private List<PostTo> postsTo(ForumThread threadBo) {
        List<PostTo> postsTo = null;
        if (threadBo != null) {
            Set<Post> postsBo = null;
            try {
                threadBo.getPosts().size();
                postsBo = threadBo.getPosts();
            } catch (Throwable cause) {}
            if (postsBo != null) {
                postsTo = new ArrayList<>(postsBo.size());
                for (Post postBo : postsBo) {
                    postsTo.add(new PostTo(postBo, toReference(threadBo, "read", "Read thread", "GET")));
                }
            }
        }
        return postsTo;
    }

    private ForumReferenceTo forumReferenceTo(Forum forumBo) {
        ForumReferenceTo forumReferenceTo = null;
        if (forumBo != null) {
            forumReferenceTo = new ForumReferenceTo(
                    forumBo.getId(),
                    forumBo.getName(),
                    forumBo.getDescription(),
                    uriInfo.getBaseUriBuilder().path("forum").path(String.format("%d", forumBo.getId())).build(),
                    "forum",
                    "Forum",
                    null,
                    null,
                    "GET",
                    null,
                    null

            );
        }
        return forumReferenceTo;
    }

    private <T> T getLastIfAny(List<T> list) {
        T item = null;
        if (list != null && !list.isEmpty()) {
            item = list.get(list.size() - 1);
        }
        return item;
    }

    private String resolveUser() {
        ResolvedUser resolvedUser = userResolver.resolveUser(request);
        return resolvedUser != null ? resolvedUser.getUsername() : null;
    }

    private <T> T getOrDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
}
