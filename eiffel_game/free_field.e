note
	description: "Summary description for {FREE_FIELD}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	FREE_FIELD

inherit
	FIELD

feature

	color: EV_COLOR
		do
			Result := create {EV_COLOR}.make_with_8_bit_rgb (255, 255, 255)
		end

end
