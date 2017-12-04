USE loanapound;

# Created as a view incase more information was added later
CREATE OR REPLACE VIEW v_underwriters AS (
		SELECT 
		u.user_id user_id, 
        u.name name
	FROM
		users u
	WHERE
		u.user_type = "C"
)