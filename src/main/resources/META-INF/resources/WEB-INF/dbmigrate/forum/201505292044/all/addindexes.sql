CREATE INDEX idx_forum_categoriId on forum_forum(forumCategoryId);
CREATE INDEX idx_forum_id_categoriId on forum_forum(forumId, forumCategoryId);
CREATE INDEX idx_forum_thread_approved_forumId on forum_thread(approved, forumId);
CREATE INDEX idx_forum_thread_create_owner on forum_thread(createdDate, owner);
CREATE INDEX idx_forum_group_forumId on forum_forum_groups(forumId);
CREATE INDEX idx_forum_post_threadId on forum_post(threadId);
CREATE INDEX idx_forum_attachment_postId on forum_attachment (postId);
