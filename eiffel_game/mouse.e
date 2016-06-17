note
	description: "Summary description for {MOUSE}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	MOUSE

create
	make

feature

	mouse_id: INTEGER
	color: EV_COLOR
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
			color := c
			move_strategy_generator := random_move_strategy_generator
			mouse_key := key

			change_direction_randomly
		end

	change_direction_randomly
		do
			current_move_strategy := move_strategy_generator.get_random_move_strategy
		end

	step
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
		do
			Result := mouse_key.same_string (key)
		end

	get_color: EV_COLOR
		do
			Result := color
		end

	get_id: INTEGER
		do
			Result := mouse_id
		end

	set_id (id: INTEGER)
		do
			mouse_id := id
		end

	set_maze (m: MAZE)
		do
			maze := m
		end

	set_maze_position (position: POSITION)
		do
			maze_position := position
		end

	get_maze_position: POSITION
		do
			Result := maze_position
		end
end
