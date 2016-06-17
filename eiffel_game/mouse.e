note
	description: "Summary description for {MOUSE}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	MOUSE

create
	make

feature{NONE}

	mouse_id: INTEGER
	my_color: EV_COLOR
	maze: MAZE
	maze_position: POSITION
	mouse_key: STRING

	move_strategy_generator: RANDOM_MOVE_STRATEGY_GENERATOR

	current_move_strategy: FUNCTION [POSITION, POSITION]

	make(c: EV_COLOR; key: STRING; random_move_strategy_generator: RANDOM_MOVE_STRATEGY_GENERATOR)
			-- Initialization for `Current'.
		do
			create maze.make
			create maze_position.make_at_row_and_col (1, 1)
			my_color := c
			move_strategy_generator := random_move_strategy_generator
			mouse_key := key

			change_direction_randomly
		end

feature

	change_direction_randomly
			-- changes mouse direction randomly
		do
			current_move_strategy := move_strategy_generator.get_random_move_strategy
		end

	step
			-- moves the mouse to next valid position
		local
			next_position : POSITION
		do
			next_position := current_move_strategy (maze_position)

			if maze.is_valid_position (next_position) then
				maze_position := next_position
			else
				change_direction_randomly
				step
			end
		end

	reacts_on_key(key: STRING): BOOLEAN
			-- checks if mouse reacts on given key
		do
			Result := mouse_key.same_string (key)
		end

	get_id: INTEGER
			-- get id of the mouse
		do
			Result := mouse_id
		end

	set_id (id: INTEGER)
			-- set id of the mouse
		do
			mouse_id := id
		end

	set_maze (m: MAZE)
			-- set the maze for the mouse
		do
			maze := m
		end

	set_maze_position (position: POSITION)
			-- set the posititon in maze of the mouse
		do
			maze_position := position
		end

	get_maze_position: POSITION
			-- get the position of the mouse
		do
			Result := maze_position
		end

	color: EV_COLOR
			-- get the color of the mouse
		do
			Result := my_color
		end
end
