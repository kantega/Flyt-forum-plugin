package no.kantega.forum.search;

import no.kantega.publishing.controls.AksessController;
import no.kantega.publishing.search.service.SearchService;
import no.kantega.publishing.search.service.SearchServiceQuery;
import no.kantega.publishing.search.service.SearchServiceResultImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


public class ForumSearchController  implements AksessController {

    private SearchService searchService;
    static final String INVALIDQUERY = "invalidquery";

    public String getDescription() {
        return "forumSearchController";
    }

    public Map handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();

        SearchServiceQuery query = createSearchServiceQuery(request);

        SearchServiceResultImpl result = (SearchServiceResultImpl)searchService.search(query);

        if (result == null){
            model.put("error", INVALIDQUERY);
        } else{
            model.put("forumresult", result);
        }
        return model;
    }

    protected SearchServiceQuery createSearchServiceQuery(HttpServletRequest request) {
        SearchServiceQuery query = new SearchServiceQuery(request, null);
        query.putSearchParam(SearchServiceQuery.PARAM_DOCTYPE, "forumPost");
        return query;
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }


}
