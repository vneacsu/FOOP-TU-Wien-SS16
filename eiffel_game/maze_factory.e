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
		do
			Result := create {MAZE}.make
		end

end
