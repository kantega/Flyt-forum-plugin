package no.kantega.forum.search;

import no.kantega.openaksess.search.query.AksessSearchContextCreator;
import no.kantega.publishing.controls.AksessController;
import no.kantega.search.api.search.SearchContext;
import no.kantega.search.api.search.SearchQuery;
import no.kantega.search.api.search.SearchResponse;
import no.kantega.search.api.search.Searcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


public class ForumSearchController implements AksessController {

    @Autowired
    private Searcher searchService;

    @Autowired
    private AksessSearchContextCreator aksessSearchContextCreator;

    static final String INVALIDQUERY = "invalidquery";

    public String getDescription() {
        return "forumSearchController";
    }

    public Map<String, Object> handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<>();

        SearchQuery query = createSearchServiceQuery(request);

        SearchResponse result = searchService.search(query);

        if (result == null){
            model.put("error", INVALIDQUERY);
        } else{
            model.put("forumresult", result);
        }
        return model;
    }

    protected SearchQuery createSearchServiceQuery(HttpServletRequest request) {
        String q = ServletRequestUtils.getStringParameter(request, "q", null);
        if (q != null) {
            return new SearchQuery(getContext(request), q, "indexedContentType:" + ForumpostTransformer.HANDLED_DOCUMENT_TYPE);
        } else {
            return null;
        }
    }

    private SearchContext getContext(HttpServletRequest request) {
        return aksessSearchContextCreator.getSearchContext(request);
    }

}
