note
	description: "Main window for this application"
	author: "Generated by the New Vision2 Application Wizard."
	date: "$Date: 2016/6/4 15:53:42 $"
	revision: "1.0.0"

class
	MAIN_WINDOW

inherit
	EV_TITLED_WINDOW

create
	make_with_maze_view

feature {NONE} -- Initialization

	make_with_maze_view(maze_view: MAZE_VIEW)
			-- Initializes the main window
		do
			make_with_title ("Maze game")
			set_size (400, 400)
			extend (maze_view)

			close_request_actions.extend (agent destroy_and_exit_if_last)
		end

end
