package no.kantega.forum.jaxrs.tll;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.jaxrs.bol.Fault;
import no.kantega.forum.jaxrs.tol.PostTo;
import no.kantega.forum.model.Post;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.modules.user.UserResolver;
import no.kantega.publishing.api.rating.Rating;
import no.kantega.publishing.api.rating.RatingService;
import no.kantega.publishing.rating.util.RatingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
@Path("/post")
@Consumes({"application/json"})
@Produces({"application/json"})
public class PostResource {

    private final Logger log = LoggerFactory.getLogger(PostResource.class);

    private ForumDao forumDao;
    private PermissionManager permissionManager;
    private UserResolver userResolver;
    private RatingService ratingService;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpServletRequest request;

    @Context
    private HttpServletResponse response;

    @Inject
    public PostResource(@Named("forumDao") ForumDao forumDao, @Named("forumPermissionManager") PermissionManager permissionManager, @Named("userResolver") UserResolver userResolver, @Named("ratingService") RatingService ratingService) {
        this.forumDao = forumDao;
        this.permissionManager = permissionManager;
        this.userResolver = userResolver;
        this.ratingService = ratingService;
    }

    @Path("{postId}")
    @GET
    public PostTo get(@PathParam("postId") Long postId) {
        log.trace("get(Long)");
        Post postBo = forumDao.getPost(postId);
        if (postBo == null) {
            throw new Fault(404, "Not found");
        }
        String user = resolveUser(userResolver, request);
        if (!permissionManager.hasPermission(user, Permission.VIEW, postBo)) {
            throw new Fault(403, "Not authorized");
        }
        List<Rating> ratings = getRatings(ratingService, postBo);
        return new PostTo(postBo, toReference(postBo.getThread(), "read", "Read thread", "GET", uriInfo), getLikes(request, ratings, postBo), /*TODO*/ null, getActions(postBo, user, permissionManager, uriInfo, ratingService, request));
    }

    @Path("{postId}/like")
    @GET
    public PostTo like(@PathParam("postId") Long postId) {
        log.trace("get(Long)");
        Post postBo = forumDao.getPost(postId);
        if (postBo == null) {
            throw new Fault(404, "Not found");
        }
        String user = resolveUser(userResolver, request);
        if (!permissionManager.hasPermission(user, Permission.VIEW, postBo)) {
            throw new Fault(403, "Not authorized");
        }
        if (hasRated(request, postId.toString(), RATING_CONTEXT, ratingService)) {
            throw new Fault(409, "Already liked");
        }
        Rating rating = new Rating();
        rating.setContext(RATING_CONTEXT);
        rating.setDate(new Date());
        rating.setObjectId(postId.toString());
        rating.setRating(1);
        rating.setUserid(RatingUtil.getUserId(request));
        ratingService.saveOrUpdateRating(rating);
        setRatingCookie(response, rating.getObjectId(), rating.getContext(), String.valueOf(rating.getRating()));
        postBo = forumDao.getPost(postId);
        List<Rating> ratings = getRatings(ratingService, postBo);
        return new PostTo(postBo, toReference(postBo.getThread(), "read", "Read thread", "GET", uriInfo), getLikes(request, ratings, postBo), /*TODO*/ null, getActions(postBo, user, permissionManager, uriInfo, ratingService, request));
    }

    @Path("{postId}/like")
    @DELETE
    public PostTo unlike(@PathParam("postId") Long postId) {
        log.trace("get(Long)");
        Post postBo = forumDao.getPost(postId);
        if (postBo == null) {
            throw new Fault(404, "Not found");
        }
        String user = resolveUser(userResolver, request);
        if (!permissionManager.hasPermission(user, Permission.VIEW, postBo)) {
            throw new Fault(403, "Not authorized");
        }
        if (!hasRated(request, postId.toString(), RATING_CONTEXT, ratingService)) {
            throw new Fault(409, "Not liked");
        }
        ratingService.deleteRatingsForUser(RatingUtil.getUserId(request), postId.toString(), RATING_CONTEXT);
        deleteRatingCookie(response, postId.toString(), RATING_CONTEXT);
        postBo = forumDao.getPost(postId);
        List<Rating> ratings = getRatings(ratingService, postBo);
        return new PostTo(postBo, toReference(postBo.getThread(), "read", "Read thread", "GET", uriInfo), getLikes(request, ratings, postBo), /*TODO*/ null, getActions(postBo, user, permissionManager, uriInfo, ratingService, request));
    }

    @Path("{postId}")
    @POST
    public PostTo replyToPost(@PathParam("postId") Long postId, PostTo postTo) {
        log.trace("get(Long)");
        Post postBo = forumDao.getPost(postId);
        if (postBo == null) {
            throw new Fault(404, "Not found");
        }
        String user = resolveUser(userResolver, request);
        if (!permissionManager.hasPermission(user, Permission.POST_IN_THREAD, postBo.getThread())) {
            throw new Fault(403, "Not authorized");
        }
        Post replyPostBo = new Post();
        replyPostBo.setOwner(user);
        replyPostBo.setAuthor(user);
        replyPostBo.setThread(postBo.getThread());
        replyPostBo.setApproved(!postBo.getThread().getForum().isApprovalRequired());
        replyPostBo.setBody(postTo.getBody());
        replyPostBo.setModifiedDate(new Date());
        replyPostBo.setNumberOfRatings(0);
        replyPostBo.setPostDate(new Date());
        replyPostBo.setRatingScore(0F);
        replyPostBo.setSubject(replyPostBo.getSubject());
        replyPostBo.setReplyToId(postBo.getReplyToId() != 0 ? postBo.getReplyToId() : postId);
        replyPostBo = forumDao.saveOrUpdate(replyPostBo);
        List<Rating> ratings = getRatings(ratingService, replyPostBo);
        return new PostTo(replyPostBo, toReference(postBo.getThread(), "read", "Read thread", "GET", uriInfo), getLikes(request, ratings, postBo), /*TODO*/ null, getActions(postBo, user, permissionManager, uriInfo, ratingService, request));
    }

    @Path("{postId}")
    @PUT
    public PostTo update(@PathParam("postId") Long postId, PostTo postTo) {
        log.trace("get(Long)");
        Post postBo = forumDao.getPost(postId);
        if (postBo == null) {
            throw new Fault(404, "Not found");
        }
        String user = resolveUser(userResolver, request);
        if (!permissionManager.hasPermission(user, Permission.EDIT_POST, postBo.getThread())) {
            throw new Fault(403, "Not authorized");
        }
        postBo.setBody(postTo.getBody());
        postBo.setModifiedDate(new Date());

        postBo = forumDao.saveOrUpdate(postBo);
        List<Rating> ratings = getRatings(ratingService, postBo);
        return new PostTo(postBo, toReference(postBo.getThread(), "read", "Read thread", "GET", uriInfo), getLikes(request, ratings, postBo), /*TODO*/ null,   getActions(postBo, user, permissionManager, uriInfo, ratingService, request));
    }
    @Path("{postId}")
    @DELETE
    public void delete(@PathParam("postId") Long postId) {
        log.trace("get(Long)");
        Post postBo = forumDao.getPost(postId);
        if (postBo == null) {
            throw new Fault(404, "Not found");
        }
        String user = resolveUser(userResolver, request);
        if (!permissionManager.hasPermission(user, Permission.EDIT_POST, postBo.getThread())) {
            throw new Fault(403, "Not authorized");
        }
        forumDao.delete(postBo);
    }

    public ForumDao getForumDao() {
        return forumDao;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public UserResolver getUserResolver() {
        return userResolver;
    }

    public RatingService getRatingService() {
        return ratingService;
    }
}
