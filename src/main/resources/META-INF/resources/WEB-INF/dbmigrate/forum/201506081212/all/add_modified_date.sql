ALTER TABLE forum_thread ADD modifiedDate DATETIME;
ALTER TABLE forum_post ADD modifiedDate DATETIME;
UPDATE forum_thread SET modifiedDate = createdDate WHERE modifiedDate IS NULL;
UPDATE forum_post SET modifiedDate = postDate WHERE modifiedDate IS NULL;
