package no.kantega.forum.search;

import no.kantega.forum.model.Post;
import no.kantega.search.api.IndexableDocument;
import no.kantega.search.api.provider.DocumentTransformerAdapter;

public class ForumpostTransformer extends DocumentTransformerAdapter<Post> {
    public static final String HANDLED_DOCUMENT_TYPE = "aksess-forumpost";

    public ForumpostTransformer() {
        super(Post.class);
    }

    @Override
    public IndexableDocument transform(Post post) {
        IndexableDocument indexableDocument = new IndexableDocument(generateUniqueID(post));
        indexableDocument.setParentId((int) post.getThread().getId());
        indexableDocument.setTitle(post.getSubject());
        indexableDocument.addAttribute(ForumFields.POST_BODY, post.getBody());
        indexableDocument.addAttribute(ForumFields.POST_AUTHOR, post.getAuthor());
        indexableDocument.addAttribute(ForumFields.POST_OWNER, post.getOwner());
        indexableDocument.addAttribute("lastModified", post.getPostDate());

        return indexableDocument;
    }

    @Override
    public String generateUniqueID(Post document) {
        return HANDLED_DOCUMENT_TYPE + document.getId();
    }


}
