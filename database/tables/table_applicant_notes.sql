USE loanapound;

DROP TABLE IF EXISTS applicant_notes;

CREATE TABLE applicant_notes (
	user_id  	smallint unsigned not null,
    note		varchar(100),
    FOREIGN KEY (user_id) REFERENCES applicants(user_id)
)