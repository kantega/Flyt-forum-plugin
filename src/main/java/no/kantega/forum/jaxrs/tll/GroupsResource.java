package no.kantega.forum.jaxrs.tll;

import no.kantega.forum.jaxrs.bol.GroupDo;
import no.kantega.forum.jaxrs.dal.GroupDao;
import no.kantega.forum.jaxrs.tol.GroupTo;
import no.kantega.forum.jaxrs.tol.GroupsTo;
import no.kantega.forum.jaxrs.tol.ResourceReferenceTo;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-06
 */
@Path("/group")
@Consumes({"application/json"})
@Produces({"application/json"})
public class GroupsResource {
    
    private GroupDao groupDao;

    @Inject
    public GroupsResource(@Named("flytForumGroupDao") GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Context
    private UriInfo uriInfo;

    @GET
    public GroupsTo getAll() {
        GroupsTo groupsTo = new GroupsTo(new ArrayList<>(), null);
        for (GroupDo groupBo : groupDao.getAllGroups()) {
            groupsTo.getGroups().add(new GroupTo(
                    groupBo.getId(),
                    Arrays.asList(new ResourceReferenceTo(uriInfo.getBaseUriBuilder().path("forum").path("{forumId}").build(groupBo.getForumId()),
                            "forum",
                            "Read forum",
                            null,
                            null,
                            "GET",
                            null,
                            null))
            ));
        }
        return groupsTo;
    }
}
