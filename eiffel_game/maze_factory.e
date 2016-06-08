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
			Result := create {MAZE}.make_with_fields (create_fields)
		end

	create_fields: ARRAY2 [FIELD]
		local
			fields: ARRAY2 [FIELD]
			lines: LIST [STRING]
			x: INTEGER
			y: INTEGER
		do
			lines := maze_string.split ('%N')
			create fields.make_filled (create {WALL_FIELD}, lines.count, lines.count)

			from x := 1
			until x > lines.count
			loop
				from y := 1
				until y > lines[x].count
				loop
					fields.put (toField(lines.at (x).at (y)), x, y)
					y := y + 1
				end

				x := x + 1
			end

			Result := fields
		end

	maze_string: STRING = "[
		fffff@@@@@@@fff
		@@f@f@@@@@@@ff@
		@@f@fff@@@@@f@@
		@@f@@@f@@@@ff@@
		@@f@@@fffffff@@
		@@ff@@f@@ffff@@
		@@@fffffff@@f@@
		@@@@@ffcff@@f@@
		@@@@@ffff@@ff@@
		@@@@@f@@@@ff@@@
		@@@@@f@@@@f@@@@
		fffffffffff@@@@
		f@@@@@@@f@@@@@@
		f@@@@@@@fffffff
		f@@@@@@@@@@@@ff
	]"

	toField(chr: CHARACTER): FIELD
		do
			if chr = 'f' then
				Result := create {FREE_FIELD}
			elseif chr = 'c' then
				Result := create {CHEESE_FIELD}
			else
				Result := create {WALL_FIELD}
			end
		end
end
