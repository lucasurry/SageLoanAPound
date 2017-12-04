USE loanapound;

DROP TABLE IF EXISTS personal_loan_applications;

CREATE TABLE personal_loan_applications (
	application_id			smallint unsigned not null auto_increment,
    applicant_id 			smallint unsigned not null,
    loan_id 				smallint unsigned not null,
    loan_value 				double,
    credit_score_used 		int,
    application_status 		varchar(1),
    CONSTRAINT pk_loan_application_id PRIMARY KEY (application_id)
);