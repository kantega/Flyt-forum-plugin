package no.kantega.forum.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.kantega.forum.model.Post;
import no.kantega.search.api.IndexableDocument;
import no.kantega.search.api.provider.DocumentTransformerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ForumpostTransformer extends DocumentTransformerAdapter<Post> {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String HANDLED_DOCUMENT_TYPE = "aksess-forumpost";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${forum.forumPostUrl:/forum/listPosts?forumId=%s&threadId=%s}")
    private String forumPostUrl;

    public ForumpostTransformer() {
        super(Post.class);
    }

    @Override
    public IndexableDocument transform(Post post) {
        IndexableDocument indexableDocument = new IndexableDocument(generateUniqueID(post));
        indexableDocument.setContentType(HANDLED_DOCUMENT_TYPE);
        indexableDocument.setId(String.valueOf(post.getId()));
        indexableDocument.setParentId((int) post.getThread().getId());
        indexableDocument.addAttribute(ForumFields.FORUM, post.getThread().getForum().getId());
        indexableDocument.setTitle(post.getSubject());
        indexableDocument.addAttribute(ForumFields.POST_BODY, post.getBody());
        indexableDocument.addAttribute(ForumFields.POST_AUTHOR, post.getAuthor());
        indexableDocument.addAttribute(ForumFields.POST_OWNER, post.getOwner());
        indexableDocument.addAttribute(ForumFields.POST_REPLY_TO, post.getReplyToId());
        indexableDocument.addAttribute("lastModified", post.getPostDate());
        indexableDocument.setLanguage("nbo");
        indexableDocument.addAttribute("url", String.format(forumPostUrl, post.getThread().getForum().getId(), post.getThread().getId()));
        if (post.getEmbed() != null) {
            try {
                Object instance = objectMapper.readValue(post.getEmbed(), Object.class);
                if (instance instanceof List) {
                    List list = (List) instance;
                    instance = list.isEmpty() ? null : list.get(0);
                }
                if (instance instanceof Map) {
                    Map<String,Object> embed = (Map<String,Object>) instance;
                    if (embed.containsKey("description")) {
                        instance = embed.get("description");
                        if (instance instanceof String) {
                            indexableDocument.addAttribute(ForumFields.POST_BODY, (String)instance);
                        }
                    }
                    if (embed.containsKey("title")) {
                        instance = embed.get("title");
                        if (instance instanceof String) {
                            indexableDocument.setTitle((String)instance);
                        }
                    }
                }
            } catch (IOException e) {
                log.error("Error transforming post " + post.getId(), e);
            }
        }
        return indexableDocument;
    }

    @Override
    public String generateUniqueID(Post document) {
        return HANDLED_DOCUMENT_TYPE + "-" + document.getId();
    }


}
