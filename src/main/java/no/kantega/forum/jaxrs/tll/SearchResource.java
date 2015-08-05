package no.kantega.forum.jaxrs.tll;

import no.kantega.forum.jaxrs.bll.AuthorizationBl;
import no.kantega.forum.jaxrs.bll.ForumBl;
import no.kantega.forum.jaxrs.bll.PostBl;
import no.kantega.forum.jaxrs.bll.ThreadBl;
import no.kantega.forum.jaxrs.tol.PostTo;
import no.kantega.forum.search.ForumPostSearchHit;
import no.kantega.forum.search.ForumpostTransformer;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.modules.user.UserResolver;
import no.kantega.openaksess.search.query.AksessSearchContextCreator;
import no.kantega.search.api.search.GroupResultResponse;
import no.kantega.search.api.search.SearchContext;
import no.kantega.search.api.search.SearchQuery;
import no.kantega.search.api.search.SearchResponse;
import no.kantega.search.api.search.SearchResult;
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

import static no.kantega.forum.jaxrs.tll.Util.*;

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

    private ForumBl forumBl;
    private PostBl postBl;
    private ThreadBl threadBl;
    private UserResolver userResolver;
    private AuthorizationBl authorizationBl;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpServletRequest request;

    @Inject
    public SearchResource(@Named("solrSearcher") Searcher searchService, @Named("aksessSearchContextCreator") AksessSearchContextCreator aksessSearchContextCreator, ForumBl forumBl, PostBl postBl, ThreadBl threadBl, @Named("userResolver") UserResolver userResolver, AuthorizationBl authorizationBl) {
        this.searchService = searchService;
        this.aksessSearchContextCreator = aksessSearchContextCreator;
        this.forumBl = forumBl;
        this.postBl = postBl;
        this.threadBl = threadBl;
        this.userResolver = userResolver;
        this.authorizationBl = authorizationBl;
    }

    @GET
    public List<PostTo> byQuery(@QueryParam("query") String query, @QueryParam("forumId") List<Long> forumId) {
        log.trace("get(Long)");
        ResolvedUser resolvedUser = userResolver.resolveUser(request);
        String user = resolveUser(resolvedUser);

        List<Long> forumIds = null;
        if (!authorizationBl.isAdministrator(user)) {
            forumIds = forumBl.getForumIds(resolveRoles(resolvedUser));
            if (forumId != null && !forumId.isEmpty()) {
                forumIds.retainAll(forumId);
            }
        } else if (forumId != null && !forumId.isEmpty()) {
            forumIds = new ArrayList<>(forumId);
        }
        if (forumIds != null && forumIds.isEmpty()) {
            return new ArrayList<>();
        }

        SearchQuery searchQuery = createSearchQuery(query);
        withFilterQuery(searchQuery, indexedContentType(ForumpostTransformer.HANDLED_DOCUMENT_TYPE));
        withFilterQuery(searchQuery, forumId(forumIds));

        if (query != null) {
            SearchResponse result = searchService.search(searchQuery);
            List<Long> postIds = new ArrayList<>();
            for (GroupResultResponse groupResultResponse : result.getGroupResultResponses()) {
                for (SearchResult searchResult : groupResultResponse.getSearchResults()) {
                    Long postId = (long) searchResult.getId();
                    if (searchResult instanceof ForumPostSearchHit) {
                        try {
                            postId = Long.parseLong(((ForumPostSearchHit)searchResult).getPostId());
                        } catch (Exception cause) {}
                    }
                    postIds.add(postId);
                }
            }
            return postBl.getPostsByPostIds(postIds).stream().map(postBo -> new PostTo(postBo, toThreadReference(postBo.getThreadId(), "read", "Read thread", "GET", uriInfo), null, null, null)).collect(Collectors.toList());
        }
        return null;
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

    private String forumId(List<Long> threadIds) {
        return threadIds != null ? "forum_l:(" + threadIds.stream().map(String::valueOf).collect(Collectors.joining(" OR ")) + ")" : null;
    }

    private SearchContext getSearchContext(HttpServletRequest request) {
        return aksessSearchContextCreator.getSearchContext(request);
    }
}
