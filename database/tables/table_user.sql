USE loanapound;

DROP TABLE IF EXISTS users;

CREATE TABLE users (
		user_id			smallint unsigned not null auto_increment, 
        name			varchar(20) not null, 
        password		varbinary(40) not null,
        user_type		varchar(1) not null,
        CONSTRAINT pk_user_id PRIMARY KEY (user_id),
        CONSTRAINT user_name UNIQUE (name)
	);
    
INSERT INTO users (name, password, user_type) VALUES ("admin", AES_ENCRYPT("secret","hampster"), "A");
INSERT INTO users (name, password, user_type) VALUES ("applicant", AES_ENCRYPT("secret","hampster"), "B");
INSERT INTO users (name, password, user_type) VALUES ("applicant_cs", AES_ENCRYPT("secret","hampster"), "B");
INSERT INTO users (name, password, user_type) VALUES ("underwriter", AES_ENCRYPT("secret","hampster"), "C");

COMMIT;