USE loanapound;

DROP TABLE IF EXISTS applicant_credit_scores;

CREATE TABLE applicant_credit_scores (
	user_id  				smallint unsigned not null,
    credit_score			varchar(100),
    credit_score_source		varchar(100),
    FOREIGN KEY (user_id) REFERENCES applicants(user_id)
);
