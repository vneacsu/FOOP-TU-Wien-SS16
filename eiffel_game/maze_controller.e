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

	maze_proxy: MAZE

	make_for_maze(m: MAZE)
		do
			make

			maze_proxy := m
		end

	execute
		do
			register_key_listener

			from until maze_proxy.game_over loop
				maze_proxy.step
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
		local
			i: INTEGER
			mouse: MOUSE
		do
			from i := 1
			until i > maze_proxy.mice.count
			loop
				mouse := maze_proxy.mice[i]
				if mouse.reacts_on_key (key.text) then
					mouse.change_direction_randomly
				end

				i := i + 1
			end
		end

	sleep_one_second
		do
			sleep (1 * 1000000000)
		end
end
