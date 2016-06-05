note
	description: "Maze View"
	author: "vneacsu"
	date: "$Date$"
	revision: "$Revision$"

class
	MAZE

inherit
	EV_PIXMAP

create
	make_with_fields

feature {NONE} -- Initialization

	make_with_fields(the_fields: ARRAY2[FIELD])
			-- Initialization of the maze.
		do
			fields := the_fields

			default_create

			set_size (fields.width * 16, fields.height * 16)
		end

feature -- Game graphics

	fields: ARRAY2[FIELD]

	repaint
			-- Repaints the maze game
		local
			x: INTEGER_32
			y: INTEGER_32
		do
			clear
			from x := 1
			until x > fields.width
			loop
				from y := 1
				until y > fields.height
				loop
					set_foreground_color (fields.item (x, y).color)
					fill_rectangle ((x - 1) * 16, (y - 1) * 16, 16, 16)

					y := y + 1
				end

				x:= x + 1
			end
		end

end
