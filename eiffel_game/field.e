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
			-- returns true when this field is the target of the game
		do
			Result := false
		end

	can_walk_in: BOOLEAN
			-- return true when one can walk inside the field
		do
			Result := true
		end
end
