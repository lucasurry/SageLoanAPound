USE loanapound;

DROP TABLE IF EXISTS personal_loans;

CREATE TABLE personal_loans (
	loan_id					smallint unsigned not null auto_increment,
  	lender					varchar(20) not null,
    repayment_months		smallint not null,
    minimum_value			double not null,
    maximum_value			double not null,
    interest_rate			double not null,
	rule 					varchar(20) not null,
    minimum_credit_score	smallint, 
	CONSTRAINT pk_user_id 	PRIMARY KEY (loan_id)
) auto_increment = 2000;

INSERT INTO personal_loans (lender, repayment_months, minimum_value, maximum_value, interest_rate, rule, minimum_credit_score) VALUES ("HSBC", 24, 4000, 10000, 8, "lowest", 588);
INSERT INTO personal_loans (lender, repayment_months, minimum_value, maximum_value, interest_rate, rule, minimum_credit_score) VALUES ("Nationwide", 24, 5000, 12000, 7.8, "highest", 588);
INSERT INTO personal_loans (lender, repayment_months, minimum_value, maximum_value, interest_rate, rule, minimum_credit_score) VALUES ("Barclays", 12, 3000, 8000, 7.5, "average", 610);
INSERT INTO personal_loans (lender, repayment_months, minimum_value, maximum_value, interest_rate, rule, minimum_credit_score) VALUES ("NatWest", 36, 6000, 18000, 7.8, "creditsite2", 560);

COMMIT;