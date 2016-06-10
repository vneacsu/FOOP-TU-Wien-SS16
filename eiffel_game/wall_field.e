note
	description: "Summary description for {WALL_FIELD}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	WALL_FIELD

inherit
	FIELD

redefine
	can_walk_in
end

feature

	color: EV_COLOR
		do
			Result := create {EV_COLOR}.make_with_8_bit_rgb (0, 0, 0)
		end

	can_walk_in: BOOLEAN
		do
			Result := false
		end
end
