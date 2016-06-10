note
	description: "Summary description for {POSITION}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	POSITION

create
	make_at_row_and_col

feature

	row: INTEGER
	col: INTEGER

	make_at_row_and_col(r, c: INTEGER)
			-- Initialization for `Current'.
		do
			row := r
			col := c
		end

end
