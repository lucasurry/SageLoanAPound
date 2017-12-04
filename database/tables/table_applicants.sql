USE loanapound;

DROP TABLE IF EXISTS applicants;

CREATE TABLE applicants (
	user_id			smallint unsigned not null,
    email_address   varbinary(40),
    last_cs_update	datetime,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT unique_applicants UNIQUE(user_id)
);

INSERT INTO applicants VALUES (2, AES_ENCRYPT("lucasurrytest2@gmail.com","hampster"), null);
INSERT INTO applicants VALUES (3, AES_ENCRYPT("lucasurrytest2@gmail.com","hampster"), null);

COMMIT;