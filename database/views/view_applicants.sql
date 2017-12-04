USE loanapound;

CREATE OR REPLACE VIEW v_applicants AS (
	SELECT 
		u.user_id user_id, 
        u.name name,
        a.email_address email_address,
		a.last_cs_update last_cs_update
	FROM
		users u
	INNER JOIN
		applicants a
	ON
		u.user_id = a.user_id
	WHERE
		u.user_type = "B"
);