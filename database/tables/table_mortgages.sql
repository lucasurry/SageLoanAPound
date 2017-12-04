USE loanapound;

DROP TABLE IF EXISTS mortgages;

CREATE TABLE mortgages (
	mortgage_id				smallint unsigned not null auto_increment,
  	lender					varchar(20) not null,
    repayment_months		smallint not null,
	minimum_value			double not null,
    maximum_value			double not null,
    interest_rate			double not null,
    min_deposit_is_percent  boolean not null,
    minimum_deposit			double not null,
    minimum_credit_score	smallint,  
	fees					double not null,
    rule 					varchar(20) not null,
    first_time_buyer_only	boolean not null,
	CONSTRAINT pk_user_id PRIMARY KEY (mortgage_id)
) auto_increment = 1000;

INSERT INTO mortgages (lender, repayment_months, minimum_value, maximum_value, interest_rate, min_deposit_is_percent, minimum_deposit, minimum_credit_score, fees, rule, first_time_buyer_only) VALUES ("HSBC", 360, 80000, 200000, 5.4, true, 15.0, 500, 1200, "lowest", false);
INSERT INTO mortgages (lender, repayment_months, minimum_value, maximum_value, interest_rate, min_deposit_is_percent, minimum_deposit, minimum_credit_score, fees, rule, first_time_buyer_only) VALUES ("Nationwide", 360, 80000, 220000, 5.8, false, 50000, 560, 8000, "highest", false);
INSERT INTO mortgages (lender, repayment_months, minimum_value, maximum_value, interest_rate, min_deposit_is_percent, minimum_deposit, minimum_credit_score, fees, rule, first_time_buyer_only) VALUES ("Barclays", 300, 100000, 300000, 5.1, false, 50000, 630, 8000, "average", false);
INSERT INTO mortgages (lender, repayment_months, minimum_value, maximum_value, interest_rate, min_deposit_is_percent, minimum_deposit, minimum_credit_score, fees, rule, first_time_buyer_only) VALUES ("NatWest", 240, 80000, 180000, 6.0, false, 50000, 560, 8000, "creditsite", false);

COMMIT;