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
			io.put_string ("Maze controller launched%N")
			from until maze.game_over loop
				maze.step
				sleep_one_second
			end
		end

	sleep_one_second
		do
			sleep (1 * 1000000000)
		end
end
