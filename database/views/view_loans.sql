USE loanapound;

CREATE OR REPLACE VIEW v_personal_loans AS (
	SELECT 
		* 
	FROM 
		personal_loans loan
);