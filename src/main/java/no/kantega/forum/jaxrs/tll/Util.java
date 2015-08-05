package no.kantega.forum.jaxrs.tll;

import no.kantega.commons.exception.SystemException;
import no.kantega.forum.jaxrs.tol.AttachmentTo;
import no.kantega.forum.jaxrs.tol.CategoryReferenceTo;
import no.kantega.forum.jaxrs.tol.ForumReferenceTo;
import no.kantega.forum.jaxrs.tol.LikeTo;
import no.kantega.forum.jaxrs.tol.PostTo;
import no.kantega.forum.jaxrs.tol.ResourceReferenceTo;
import no.kantega.forum.model.Attachment;
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
import no.kantega.publishing.common.Aksess;
import no.kantega.publishing.common.service.TopicMapService;
import no.kantega.publishing.security.SecuritySession;
import no.kantega.publishing.security.data.Role;
import no.kantega.publishing.security.data.User;
import no.kantega.publishing.security.realm.SecurityRealm;
import no.kantega.publishing.topicmaps.data.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-25
 */
public class Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

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

    public static List<PostTo> postsTo(ForumThread threadBo, String user, PermissionManager permissionManager, UriInfo uriInfo, RatingService ratingService, HttpServletRequest request, List<Rating> ratings) {
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
                    postsTo.add(new PostTo(postBo, toReference(threadBo, "read", "Read thread", "GET", uriInfo), getLikes(request, ratings, postBo), getAttachments(postBo, uriInfo), getActions(postBo, user, permissionManager, uriInfo, ratingService, request)));
                }
            }
        }
        return postsTo;
    }

    public static List<LikeTo> getLikes(HttpServletRequest request, List<Rating> ratingBos, Post postBo) {
        List<LikeTo> likes = new ArrayList<>();
        for (Rating ratingBo : ratingBos) {
            if (ratingBo.getObjectId().equalsIgnoreCase(String.valueOf(postBo.getId()))) {

                likes.add(new LikeTo(ratingBo, getUser(request, ratingBo.getUserid(), false, false)));
            }
        }
        return likes;
    }

    public static List<AttachmentTo> getAttachments(Post postBo, UriInfo uriInfo) {
        List<Attachment> attachmentBos = null;
        List<AttachmentTo> attachmentTos = null;
        try {
            attachmentBos = new ArrayList<>(postBo.getAttachments());
        } catch (Exception cause) {}
        if (attachmentBos != null) {
            attachmentTos = new ArrayList<>(attachmentBos.size());
            for (Attachment attachment : attachmentBos) {
                attachmentTos.add(new AttachmentTo(attachment, /*TODO*/null, toReference(postBo, "read", "Read post", "GET", uriInfo)));
            }
        }
        return attachmentTos;
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
        return resolveUser(resolvedUser);
    }

    public static String resolveUser(ResolvedUser resolvedUser) {
        return resolvedUser != null ? resolvedUser.getUsername() : null;
    }

    public static List<String> resolveRoles(UserResolver userResolver, HttpServletRequest request) {
        ResolvedUser resolvedUser = userResolver.resolveUser(request);
        return resolveRoles(resolvedUser);
    }

    public static List<String> resolveRoles(ResolvedUser resolvedUser) {
        if (resolvedUser != null) {
            String[] resolvedRoles = resolvedUser.getRoles();
            if (resolvedRoles != null) {
                List<String> roles = new ArrayList<>(resolvedRoles.length);
                for (String resolvedRole : resolvedRoles) {
                    if (resolvedRole != null) {
                        roles.add(resolvedRole);
                    }
                }
                return roles;
            }
        } return null;
    }

    public static List<ResourceReferenceTo> getActions(String username, Long startAtThreadId, Long endAtThreadId, Integer numberOfThreads, Boolean includePosts, UriInfo uriInfo) {
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


    public static List<Rating> getRatings(RatingService ratingService, Post postBo) {
        List<Rating> ratings = null;
        if (postBo != null) {
            ratings = ratingService.getRatingsForObject(String.valueOf(postBo.getId()), RATING_CONTEXT);
        }
        return ratings;
    }

    public static List<Rating> getRatings(RatingService ratingService, ForumThread threadBo) {
        List<Rating> ratings = null;
        List<Post> postBos = null;
        try {
            postBos = new ArrayList<>(threadBo.getPosts());
        } catch (Exception cause) {}
        if (postBos != null) {
            List<String> postIds = new ArrayList<>(postBos.size());
            for (Post postBo : postBos) {
                postIds.add(String.valueOf(postBo.getId()));
            }
            ratings = ratingService.getRatingsForObjects(postIds, RATING_CONTEXT);
        }
        return ratings;
    }

    public static List<Rating> getRatings(RatingService ratingService, String... objectIds) {
        return ratingService.getRatingsForObjects(Arrays.asList(objectIds), RATING_CONTEXT);
    }

    public static User getUser(HttpServletRequest request, String username, boolean getRoles, boolean getRoleTopics) {
        User user = null;
        try {
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession(true).getServletContext());
            if(!isBlank(username)) {
                try {
                    SecurityRealm realm = null;
                    Map<String,SecurityRealm> beansOfType = webApplicationContext.getBeansOfType(SecurityRealm.class);
                    for (SecurityRealm r : beansOfType.values()) {
                        user = r.lookupUser(username);
                        if(user != null){
                            realm = r;
                            break;

                        }
                    }

                    if (user != null) {
                        if (getRoles || getRoleTopics) {
                            List<Role> roles = realm.lookupRolesForUser(user.getId());
                            for (Role role : roles) {
                                user.addRole(role);
                            }
                            if (getRoleTopics && Aksess.isTopicMapsEnabled()) {
                                // Hent topics for bruker
                                TopicMapService topicService = new TopicMapService(request);

                                if (user.getRoles() != null) {
                                    for (Role role : roles) {
                                        List<Topic> tmp = topicService.getTopicsBySID(role);
                                        for (Topic aTmp : tmp) {
                                            user.addTopic(aTmp);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (SystemException e) {
                    user = null;
                }
            }
        } catch (Exception cause) {

        }
        return user;
    }
}
