package no.kantega.forum.jaxrs.tll;

import no.kantega.forum.jaxrs.bll.AuthorizationBl;
import no.kantega.forum.jaxrs.bll.ThreadBl;
import no.kantega.forum.search.ForumpostTransformer;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.modules.user.UserResolver;
import no.kantega.openaksess.search.query.AksessSearchContextCreator;
import no.kantega.search.api.search.SearchContext;
import no.kantega.search.api.search.SearchQuery;
import no.kantega.search.api.search.SearchResponse;
import no.kantega.search.api.search.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static no.kantega.forum.jaxrs.tll.Util.resolveRoles;
import static no.kantega.forum.jaxrs.tll.Util.resolveUser;

/**
 * @author Kristian Myrhaug
 * @since 2015-08-04
 */
@Path("/search")
@Consumes({"application/json"})
@Produces({"application/json"})
public class SearchResource {

    private final Logger log = LoggerFactory.getLogger(SearchResource.class);

    static final String INVALIDQUERY = "invalidquery";

    private Searcher searchService;
    private AksessSearchContextCreator aksessSearchContextCreator;

    private ThreadBl threadBl;
    private UserResolver userResolver;
    private AuthorizationBl authorizationBl;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpServletRequest request;

    @Inject
    public SearchResource(@Named("solrSearcher") Searcher searchService, @Named("aksessSearchContextCreator") AksessSearchContextCreator aksessSearchContextCreator, ThreadBl threadBl, @Named("userResolver") UserResolver userResolver, AuthorizationBl authorizationBl) {
        this.searchService = searchService;
        this.aksessSearchContextCreator = aksessSearchContextCreator;
        this.threadBl = threadBl;
        this.userResolver = userResolver;
        this.authorizationBl = authorizationBl;
    }

    @GET
    public SearchResponse byQuery(@QueryParam("query") String query) {
        log.trace("get(Long)");
        ResolvedUser resolvedUser = userResolver.resolveUser(request);
        String user = resolveUser(resolvedUser);

        List<String> roles = null;
        if (!authorizationBl.isAdministrator(user)) {
            roles = resolveRoles(resolvedUser);
        }

        List<Long> threadIds = roles != null ? threadBl.getThreadIdsByRoles(roles) : null;

        SearchQuery searchQuery = createSearchQuery(query);
        withFilterQuery(searchQuery, indexedContentType(ForumpostTransformer.HANDLED_DOCUMENT_TYPE));
        withFilterQuery(searchQuery, parentIdFilterQuery(threadIds));

        SearchResponse result = null;
        if (query != null) {
            result = searchService.search(searchQuery);
        }
        return result;
    }

    protected SearchQuery createSearchQuery(String query) {
        if (query != null) {
            SearchQuery searchQuery = new SearchQuery(getSearchContext(request), query);
            searchQuery.setFilterQueries(new ArrayList<>(searchQuery.getFilterQueries()));
            return searchQuery;
        } else {
            return null;
        }
    }

    private SearchQuery withFilterQuery(SearchQuery searchQuery, String filterQuery) {
        if (filterQuery != null) {
            searchQuery.getFilterQueries().add(filterQuery);
        }
        return searchQuery;
    }

    private String indexedContentType(String indexedContentType) {
        return "indexedContentType:" + indexedContentType;
    }

    private String parentIdFilterQuery(List<Long> threadIds) {
        return threadIds != null ? "parentId:(" + threadIds.stream().map(String::valueOf).collect(Collectors.joining(" OR ")) + ")" : null;
    }

    private SearchContext getSearchContext(HttpServletRequest request) {
        return aksessSearchContextCreator.getSearchContext(request);
    }
}
