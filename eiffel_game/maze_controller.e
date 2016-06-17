note
	description: "Controller for the maze game"
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	MAZE_CONTROLLER

inherit
	THREAD

create
	make_for_maze

feature {NONE} -- Initialization

	maze: MAZE

	make_for_maze(m: MAZE)
			-- Initializes the controller with the given maze model
		do
			make

			maze := m
		end

	execute
			-- Handles main game logic
		do
			register_key_listener

			from until maze.game_over loop
				maze.step
				sleep_one_second
			end
		end

	register_key_listener
			-- Registers key listener with the application
		do
			if attached (create {EV_ENVIRONMENT}).application as app then
				app.key_press_actions.extend (agent on_key_pressed)
			end
		end

	on_key_pressed (widget: EV_WIDGET; key: EV_KEY)
			-- Listener for key strokes. Delegates to mice.
		local
			i: INTEGER
			mouse: MOUSE
		do
			from i := 1
			until i > maze.mice.count
			loop
				mouse := maze.mice[i]
				if mouse.reacts_on_key (key.text) then
					mouse.change_direction_randomly
				end

				i := i + 1
			end
		end

	sleep_one_second
			-- Delays one second (the game pace).
		do
			sleep (1 * 1000000000)
		end
end
