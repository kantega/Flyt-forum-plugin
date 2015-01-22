package no.kantega.forum.search;

import no.kantega.forum.model.Post;
import no.kantega.search.api.IndexableDocument;
import no.kantega.search.api.provider.DocumentTransformerAdapter;
import org.springframework.beans.factory.annotation.Value;

public class ForumpostTransformer extends DocumentTransformerAdapter<Post> {
    public static final String HANDLED_DOCUMENT_TYPE = "aksess-forumpost";

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
        return indexableDocument;
    }

    @Override
    public String generateUniqueID(Post document) {
        return HANDLED_DOCUMENT_TYPE + "-" + document.getId();
    }


}
