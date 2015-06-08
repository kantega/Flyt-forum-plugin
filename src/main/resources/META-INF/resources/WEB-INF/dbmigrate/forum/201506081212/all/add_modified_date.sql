UPDATE forum_thread SET modifiedDate = createdDate WHERE modifiedDate IS NULL;
UPDATE forum_post SET modifiedDate = postDate WHERE modifiedDate IS NULL;