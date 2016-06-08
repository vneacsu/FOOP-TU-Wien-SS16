note
	description: "Maze View"
	author: "vneacsu"
	date: "$Date$"
	revision: "$Revision$"

class
	MAZE

create
	make_with_fields

feature

	fields: ARRAY2 [FIELD]
	update_listeners: ARRAYED_LIST [PROCEDURE [MAZE]]
	game_over: BOOLEAN

	make_with_fields(maze_fields: ARRAY2 [FIELD])
		do
			fields := maze_fields
			create update_listeners.make (1)
			game_over := false
		end

	add_on_update_listener(listener: PROCEDURE [MAZE])
		do
			update_listeners.extend (listener)
		end

	notify_updated(listener: PROCEDURE [MAZE])
		do
			listener.call (Current)
		end

	step
		do
			io.put_string ("Maze step%N")
			update_listeners.do_all (agent notify_updated)
		end

	is_game_over: BOOLEAN
		do
			Result := game_over
		end

	get_fields: ARRAY2 [FIELD]
		do
			Result := fields
		end
end
