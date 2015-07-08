package no.kantega.forum.jaxrs.tll;

import no.kantega.forum.jaxrs.bol.GroupDo;
import no.kantega.forum.jaxrs.dal.GroupDao;
import no.kantega.forum.jaxrs.dal.jdbc.Jdbc;
import no.kantega.forum.jaxrs.tol.GroupTo;
import no.kantega.forum.jaxrs.tol.GroupsTo;
import no.kantega.forum.jaxrs.tol.ResourceReferenceTo;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
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
    private DataSource dataSource;

    @Inject
    public GroupsResource(@Named("flytForumGroupDao") GroupDao groupDao, @Named("aksessDataSource") DataSource dataSource) {
        this.groupDao = groupDao;
        this.dataSource = dataSource;
    }

    @Context
    private UriInfo uriInfo;

    @GET
    public GroupsTo getAll() {
        return Jdbc.readOnly(dataSource, connection -> {
            GroupsTo groupsTo = new GroupsTo(new ArrayList<>(), null);
            for (GroupDo groupBo : groupDao.getAllGroups(connection)) {
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
        });
    }
}
