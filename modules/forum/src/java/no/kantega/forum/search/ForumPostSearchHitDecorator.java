package no.kantega.forum.search;

import no.kantega.forum.dao.ForumDao;
import no.kantega.search.api.provider.SearchResultDecorator;
import no.kantega.search.api.search.SearchQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Map;

import static java.util.Arrays.asList;

public class ForumPostSearchHitDecorator implements SearchResultDecorator<ForumPostSearchHit> {
    @Autowired
    private ForumDao forumDao;

    @Override
    public Collection<String> handledindexedContentTypes() {
        return asList(ForumpostTransformer.HANDLED_DOCUMENT_TYPE);
    }

    @Override
    public ForumPostSearchHit decorate(Map<String, Object> resultMap, String title, String description, SearchQuery query) {
        return new ForumPostSearchHit(forumDao.getPost((Integer) resultMap.get("id")), (String) resultMap.get("url"), description);
    }
}
