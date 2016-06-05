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
	make

feature {NONE} -- Initialization

	make
			-- Initialization for `Current'.
		do
			default_create
			
			set_size (200, 200)
		end

feature -- Game graphics

	repaint
			-- Repaints the maze game
		do
			clear
			draw_text_top_left (10, 10, "Game running")
		end

end
