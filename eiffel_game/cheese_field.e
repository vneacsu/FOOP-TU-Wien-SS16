note
	description: "Summary description for {CHEESE_FIELD}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	CHEESE_FIELD

inherit
	FIELD

redefine
	is_target
end

feature

	color: EV_COLOR
			-- the color of the cheese field
		do
			Result := create {EV_COLOR}.make_with_8_bit_rgb (255, 255, 0)
		end

	is_target: BOOLEAN
			-- returns true, since this type of field is a target field
		do
			Result := true
		end
end
