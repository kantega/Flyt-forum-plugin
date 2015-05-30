ALTER TABLE forum_attachment ADD created DATETIME;
UPDATE forum_attachment set created = '2014-09-12 23:59:00.000';
