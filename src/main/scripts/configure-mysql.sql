# docker run --name mysqldb -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -d mysql

CREATE DATABASE sfg_dev;
CREATE DATABASE sfg_prod;

CREATE USER 'sfg_dev_user'@'localhost' IDENTIFIED BY 'guru';
CREATE USER 'sfg_prod_user'@'localhost' IDENTIFIED BY 'guru';
CREATE USER 'sfg_dev_user'@'%' IDENTIFIED BY 'guru'; # % - wildcard, from any host, needed for docker
CREATE USER 'sfg_prod_user'@'%' IDENTIFIED BY 'guru';

GRANT SELECT, INSERT, UPDATE, DELETE ON sfg_dev.* TO 'sfg_dev_user'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON sfg_prod.* TO 'sfg_prod_user'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON sfg_dev.* TO 'sfg_dev_user'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON sfg_prod.* TO 'sfg_prod_user'@'%';

