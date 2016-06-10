note
	description: "Maze View"
	author: "vneacsu"
	date: "$Date$"
	revision: "$Revision$"

class
	MAZE

create
	make

feature

	fields: ARRAY2 [FIELD]
	mice: ARRAYED_LIST [MOUSE]
	update_listeners: ARRAYED_LIST [PROCEDURE [MAZE]]
	game_over: BOOLEAN

	make
		do
			create fields.make_filled (create {WALL_FIELD}, 0, 0)
			create mice.make(4)
			create update_listeners.make (1)
			game_over := false
		end

	set_fields (maze_fields: ARRAY2 [FIELD])
		do
			fields := maze_fields
		end

	add_mouse (mouse: MOUSE)
		do
			mice.extend (mouse)

			mouse.set_id (mice.count)
			mouse.set_maze (Current)
			position_mouse_in_maze(mouse)
		end

	position_mouse_in_maze(mouse: MOUSE)
		do
			inspect mouse.get_id
			when 1 then
				mouse.set_maze_position (create {POSITION}.make_at_row_and_col (1, 1))
			when 2 then
				mouse.set_maze_position (create {POSITION}.make_at_row_and_col (1, fields.width))
			when 3 then
				mouse.set_maze_position (create {POSITION}.make_at_row_and_col (fields.height, 1))
			else
				mouse.set_maze_position (create {POSITION}.make_at_row_and_col (fields.width, fields.height))
			end
		end

	add_on_update_listener(listener: PROCEDURE [MAZE])
		do
			update_listeners.extend (listener)
		end

	notify_update_listeners
		do
			update_listeners.do_all (agent (listener: PROCEDURE [MAZE])
				do
					listener.call (Current)
				end
			)
		end

	step
		do
			mice.do_all (agent (mouse: MOUSE)
				do
					mouse.step

					if fields.item (mouse.get_maze_position.row, mouse.get_maze_position.col).is_target then
						game_over := true
					end
				end
			)

			notify_update_listeners
		end

	is_valid_position (position: POSITION): BOOLEAN
		do
			Result := position.row > 0 and position.row <= fields.height and
				position.col > 0 and position.col <= fields.width and
				fields.item (position.row, position.col).can_walk_in
		end

	is_game_over: BOOLEAN
		do
			Result := game_over
		end

	get_fields: ARRAY2 [FIELD]
		do
			Result := fields
		end

	get_mice: LIST [MOUSE]
		do
			Result := mice
		end
end
