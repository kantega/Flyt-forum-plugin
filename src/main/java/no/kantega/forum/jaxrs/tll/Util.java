package no.kantega.forum.jaxrs.tll;

import no.kantega.forum.jaxrs.tol.CategoryReferenceTo;
import no.kantega.forum.jaxrs.tol.ForumReferenceTo;
import no.kantega.forum.jaxrs.tol.PostTo;
import no.kantega.forum.jaxrs.tol.ResourceReferenceTo;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumCategory;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.modules.user.UserResolver;
import no.kantega.publishing.api.rating.Rating;
import no.kantega.publishing.api.rating.RatingService;
import no.kantega.publishing.security.SecuritySession;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-25
 */
public class Util {

    public static final String RATING_CONTEXT = "forum";

    private Util(){}

    public static List<ResourceReferenceTo> getActions(ForumCategory categoryBo, String user, PermissionManager permissionManager, UriInfo uriInfo) {
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

    public static List<ForumReferenceTo> forumReferenceTos(Set<Forum> forumBos, UriInfo uriInfo) {
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

    public static CategoryReferenceTo categoryReferenceTo(ForumCategory categoryBo, UriInfo uriInfo) {
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

    public static List<ResourceReferenceTo> getActions(Forum forumBo, String user, PermissionManager permissionManager, UriInfo uriInfo) {
        List<ResourceReferenceTo> actions = new ArrayList<>();
        if (forumBo != null) {
            if (permissionManager.hasPermission(user, Permission.VIEW, forumBo)) {
                actions.add(toReference(forumBo, "read", "Read forum", "GET", uriInfo));
            }
            if (permissionManager.hasPermission(user, Permission.EDIT_FORUM, forumBo)) {
                actions.add(toReference(forumBo, "update", "Update forum", "PUT", uriInfo));
                actions.add(toReference(forumBo, "delete", "Delete forum", "DELETE", uriInfo));
            }
            if (permissionManager.hasPermission(user, Permission.ADD_THREAD, forumBo)) {
                actions.add(new ResourceReferenceTo(
                        uriInfo.getBaseUriBuilder().path("forum").path("{forumId}").build(forumBo.getId()),
                        "create",
                        "Create thread",
                        null,
                        null,
                        "POST",
                        null,
                        null
                ));
            }
        }
        return actions;
    }

    public static List<ResourceReferenceTo> getActions(ForumThread threadBo, String user, PermissionManager permissionManager, UriInfo uriInfo) {
        List<ResourceReferenceTo> actions = new ArrayList<>();
        if (threadBo != null) {
            if (permissionManager.hasPermission(user, Permission.VIEW, threadBo)) {
                actions.add(toReference(threadBo, "read", "Read thread", "GET", uriInfo));
            }
            if (permissionManager.hasPermission(user, Permission.EDIT_THREAD, threadBo)) {
                actions.add(toReference(threadBo, "update", "Update thread", "PUT", uriInfo));
            }
            if (permissionManager.hasPermission(user, Permission.DELETE_THREAD, threadBo)) {
                actions.add(toReference(threadBo, "delete", "Delete thread", "DELETE", uriInfo));
            }
            if (permissionManager.hasPermission(user, Permission.POST_IN_THREAD, threadBo)) {
                actions.add(toReference(threadBo, "create", "Create post", "POST", uriInfo));
            }
        }
        return actions;
    }

    public static ResourceReferenceTo toReference(Post postBo, String rel, String title, String method, UriInfo uriInfo) {
        return new ResourceReferenceTo(
                uriInfo.getBaseUriBuilder().path("post").path("{postId}").build(postBo.getId()),
                rel,
                title,
                null,
                null,
                method,
                null,
                null
        );
    }

    public static ForumReferenceTo toReference(Forum forumBo, String rel, String title, String method, UriInfo uriInfo) {
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

    public static ResourceReferenceTo toReference(ForumThread threadBo, String rel, String title, String method, UriInfo uriInfo) {
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

    public static List<PostTo> postsTo(ForumThread threadBo, String user, PermissionManager permissionManager, UriInfo uriInfo, RatingService ratingService, HttpServletRequest request) {
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
                    postsTo.add(new PostTo(postBo, toReference(threadBo, "read", "Read thread", "GET", uriInfo), /*TODO*/ null, getActions(postBo, user, permissionManager, uriInfo, ratingService, request)));
                }
            }
        }
        return postsTo;
    }

    public static ForumReferenceTo forumReferenceTo(Forum forumBo, UriInfo uriInfo) {
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

    public static List<ResourceReferenceTo> getActions(Post postBo, String user, PermissionManager permissionManager, UriInfo uriInfo, RatingService ratingService, HttpServletRequest request) {
        List<ResourceReferenceTo> actions = new ArrayList<>();
        if (postBo != null) {
            if (permissionManager.hasPermission(user, Permission.VIEW, postBo)) {
                actions.add(toReference(postBo, "read", "Read post", "GET", uriInfo));
            }
            if (permissionManager.hasPermission(user, Permission.EDIT_POST, postBo)) {
                actions.add(toReference(postBo, "update", "Update post", "PUT", uriInfo));
            }
            if (permissionManager.hasPermission(user, Permission.DELETE_POST, postBo)) {
                actions.add(toReference(postBo, "delete", "Delete post", "DELETE", uriInfo));
            }
            if (permissionManager.hasPermission(user, Permission.POST_IN_THREAD, postBo.getThread())) {
                actions.add(new ResourceReferenceTo(
                        uriInfo.getBaseUriBuilder().path("post").path("{postId}").build(
                                postBo.getReplyToId() != 0 ? postBo.getReplyToId() : postBo.getId()
                        ),
                        "reply",
                        "Reply to",
                        null,
                        null,
                        "POST",
                        null,
                        null
                ));
            }
            if (permissionManager.hasPermission(user, Permission.VIEW, postBo)) {
                if (!hasRated(request, String.valueOf(postBo.getId()), RATING_CONTEXT, ratingService)) {
                    actions.add(new ResourceReferenceTo(
                            uriInfo.getBaseUriBuilder().path("post").path("{postId}").path("like").build(
                                    postBo.getReplyToId() != 0 ? postBo.getReplyToId() : postBo.getId()
                            ),
                            "like",
                            "Like post",
                            null,
                            null,
                            "GET",
                            null,
                            null
                    ));
                } else {
                    actions.add(new ResourceReferenceTo(
                            uriInfo.getBaseUriBuilder().path("post").path("{postId}").path("like").build(
                                    postBo.getReplyToId() != 0 ? postBo.getReplyToId() : postBo.getId()
                            ),
                            "unlike",
                            "Unlike post",
                            null,
                            null,
                            "DELETE",
                            null,
                            null
                    ));
                }
            }
        }
        return actions;
    }

    public static String resolveUser(UserResolver userResolver, HttpServletRequest request) {
        ResolvedUser resolvedUser = userResolver.resolveUser(request);
        return resolvedUser != null ? resolvedUser.getUsername() : null;
    }public static List<ResourceReferenceTo> getActions(String username, Long startAtThreadId, Long endAtThreadId, Integer numberOfThreads, Boolean includePosts, UriInfo uriInfo) {
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

    public static boolean hasRated(HttpServletRequest request, String objectId, String context, RatingService ratingService) {
        for (Cookie cookie : request.getCookies()) {
            //The user has already rated if she has a cookie for this object.
            if (cookie.getName().equals(getCookieNameForObject(objectId, context))) {
                return true;
            }
        }
        //If she is logged in she might have rated this object even though she doesn't have a cookie.
        SecuritySession secSession = SecuritySession.getInstance(request);
        if (secSession.isLoggedIn()) {
            List<Rating> ratings = ratingService.getRatingsForObject(objectId, context);
            if (ratings == null || ratings.size() == 0 ) {
                return false;
            }
            for (Rating rating : ratings) {
                if (rating.getUserid().equals(secSession.getUser().getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void deleteRatingCookie(HttpServletResponse response, String objectId, String context) {
        Cookie cookie = new Cookie(getCookieNameForObject(objectId, context), "0");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static void setRatingCookie(HttpServletResponse response, String objectId, String context, String value) {
        response.addCookie(new Cookie(getCookieNameForObject(objectId, context), value));
    }

    public static String getCookieNameForObject(String objectId, String context) {
        return "aksess-rating-" + context + "-" + objectId;
    }
}
