note
	description: "Row and Column based position"
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	POSITION

create
	make_at_row_and_col

feature {NONE}

	my_row: INTEGER
	my_col: INTEGER

	make_at_row_and_col(r, c: INTEGER)
			-- Initialization for `Current'.
		do
			my_row := r
			my_col := c
		end

feature

	row: INTEGER
			-- Returns the row position
		do
			Result := my_row
		end

	col: INTEGER
			-- Returns the column position
		do
			Result := my_col
		end
end
