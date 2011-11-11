package no.kantega.forum.search;

import no.kantega.publishing.search.service.SearchServiceImpl;
import no.kantega.search.criteria.Criterion;

import java.util.Collections;
import java.util.List;

public class ForumServiceService extends SearchServiceImpl {
    @Override
    protected List<Criterion> getDefaultFilters() {
        return Collections.emptyList();
    }
}
