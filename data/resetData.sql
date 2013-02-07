USE htp;
TRUNCATE Upload;
TRUNCATE Execution;
TRUNCATE UserRoleRequest;
TRUNCATE News;
TRUNCATE SharpNews;
UPDATE Category set count = 0;
DELETE FROM User WHERE username != 'admin';