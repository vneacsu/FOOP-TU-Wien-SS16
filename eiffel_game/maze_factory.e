note
	description: "Factory for creating maze instances"
	author: "vneacsu"
	date: "$Date$"
	revision: "$Revision$"

class
	MAZE_FACTORY

create
	default_create

feature -- Maze creation

	create_maze: MAZE
			-- Creates a new maze
		local
			fields: ARRAY2[FIELD]
		do
			create fields.make_filled (create {WALL_FIELD}, 2, 2)
			fields.put (create {FREE_FIELD}, 1, 1)
			fields.put (create {CHEESE_FIELD}, 2, 2)
			Result := create {MAZE}.make_with_fields (fields)
		end

end
