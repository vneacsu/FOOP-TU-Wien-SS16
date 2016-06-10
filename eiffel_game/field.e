note
	description: "A maze field"
	author: "vneacsu"
	date: "$Date$"
	revision: "$Revision$"

deferred class
	FIELD

feature -- Maze graphics

	color: EV_COLOR
			-- The color of the field
		deferred
		end

	is_target: BOOLEAN
		do
			Result := false
		end

	can_walk_in: BOOLEAN
		do
			Result := true
		end
end
