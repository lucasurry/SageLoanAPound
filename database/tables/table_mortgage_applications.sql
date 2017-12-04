DROP TABLE IF EXISTS mortgage_applications;

CREATE TABLE mortgage_applications (
	application_id 			smallint unsigned not null auto_increment,
    applicant_id 			smallint unsigned not null,
    mortgage_id 			smallint unsigned not null,
    loan_value 				double,
    deposit_amount 			double,
    first_time_buyer 		boolean,
    credit_score_used 		int,
    application_status 		varchar(1),
    CONSTRAINT pk_loan_application_id PRIMARY KEY (application_id)
);