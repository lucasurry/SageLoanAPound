USE loanapound;

CREATE OR REPLACE VIEW v_mortgages AS (
	SELECT 
		* 
	FROM 
		mortgages mort
);