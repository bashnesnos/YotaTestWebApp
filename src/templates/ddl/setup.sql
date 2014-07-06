CREATE DATABASE `@dbName@` DEFAULT CHARACTER SET utf8;

CREATE USER '@dbUser@'@'%' IDENTIFIED BY '@dbPassword@';

GRANT CREATE ON @dbName@.* TO '@dbUser@'@'%';
GRANT ALTER ON @dbName@.* TO '@dbUser@'@'%';
GRANT DROP ON @dbName@.* TO '@dbUser@'@'%';
GRANT INDEX ON @dbName@.* TO '@dbUser@'@'%';
GRANT DELETE ON @dbName@.* TO '@dbUser@'@'%';
GRANT INSERT ON @dbName@.* TO '@dbUser@'@'%';
GRANT SELECT ON @dbName@.* TO '@dbUser@'@'%';
GRANT UPDATE ON @dbName@.* TO '@dbUser@'@'%';
GRANT LOCK TABLES ON @dbName@.* TO '@dbUser@'@'%';
GRANT CREATE TEMPORARY TABLES ON @dbName@.* TO '@dbUser@'@'%';

FLUSH PRIVILEGES;
