note
	description: "Summary description for {MAZE_CONTROLLER}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	MAZE_CONTROLLER

inherit
	THREAD

create
	make_for_maze

feature

	maze: MAZE

	make_for_maze(m: MAZE)
		do
			make

			maze := m
		end

	execute
		do
			register_key_listener

			from until maze.game_over loop
				maze.step
				sleep_one_second
			end
		end

	register_key_listener
		do
			if attached (create {EV_ENVIRONMENT}).application as app then
				app.key_press_actions.extend (agent on_key_pressed)
			end
		end

	on_key_pressed (widget: EV_WIDGET; key: EV_KEY)
		do
			io.put_string (key.text)
		end

	sleep_one_second
		do
			sleep (1 * 1000000000)
		end
end
