package no.kantega.forum.jaxrs.tll;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.jaxrs.bol.Fault;
import no.kantega.forum.jaxrs.tol.PostTo;
import no.kantega.forum.model.Post;
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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.Date;

import static no.kantega.forum.jaxrs.tll.Util.getActions;
import static no.kantega.forum.jaxrs.tll.Util.resolveUser;
import static no.kantega.forum.jaxrs.tll.Util.toReference;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
@Path("/post")
@Consumes({"application/json", "application/xml"})
@Produces({"application/json", "application/xml"})
public class PostResource {

    private final Logger log = LoggerFactory.getLogger(PostResource.class);

    private ForumDao forumDao;
    private PermissionManager permissionManager;
    private UserResolver userResolver;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpServletRequest request;

    @Inject
    public PostResource(@Named("forumDao") ForumDao forumDao, @Named("forumPermissionManager") PermissionManager permissionManager, @Named("userResolver") UserResolver userResolver) {
        this.forumDao = forumDao;
        this.permissionManager = permissionManager;
        this.userResolver = userResolver;
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
        return new PostTo(postBo, toReference(postBo.getThread(), "read", "Read thread", "GET", uriInfo), /*TODO*/ null, getActions(postBo, user, permissionManager, uriInfo));
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
        forumDao.saveOrUpdate(replyPostBo);
        return new PostTo(replyPostBo, toReference(postBo.getThread(), "read", "Read thread", "GET", uriInfo), /*TODO*/ null, getActions(postBo, user, permissionManager, uriInfo));
    }

    public ForumDao getForumDao() {
        return forumDao;
    }



}
