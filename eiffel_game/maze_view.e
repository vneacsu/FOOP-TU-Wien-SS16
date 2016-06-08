note
	description: "Summary description for {MAZE_VIEW}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	MAZE_VIEW

inherit
	EV_VERTICAL_BOX

create
	make_for_maze

feature

	draw_area: EV_PIXMAP
	status_bar: EV_STATUS_BAR
	status_label: EV_LABEL

	make_for_maze(maze: MAZE)
		do
			create draw_area
			create status_label
			create status_bar
			default_create

			initialize_draw_area(maze)
			initialize_status_bar

			maze.add_on_update_listener (agent on_maze_update)
		end

	initialize_draw_area(maze: MAZE)
		do
			extend (draw_area)
			on_maze_update (maze)
		end

	initialize_status_bar
		do
			status_bar.set_border_width (2)
			extend (status_bar)
			disable_item_expand (status_bar)

			status_label.set_text ("Game running")
			status_label.align_text_left
			status_bar.extend (status_label)
		end

	on_maze_update(maze: MAZE)
		local
			new_draw_area_content: EV_PIXMAP
		do
			io.put_string ("Maze updated%N")

			new_draw_area_content := create_pixmap_for(maze)

			draw_area.set_size (new_draw_area_content.width, new_draw_area_content.height)
			draw_area.draw_pixmap (0, 0, new_draw_area_content)
			draw_area.flush
		end

	create_pixmap_for (maze: MAZE): EV_PIXMAP
		local
			pixmap: EV_PIXMAP
		do
			create pixmap.make_with_size (maze.fields.width * 16, maze.fields.height * 16)

			paint_fields (maze, pixmap)

			Result := pixmap
		end

	paint_fields(maze: MAZE; pixmap: EV_PIXMAP)
		local
			x: INTEGER
			y: INTEGER
		do
			from x := 1
			until x > maze.fields.width
			loop
				from y := 1
				until y > maze.fields.height
				loop
					pixmap.set_foreground_color (maze.fields.item (x, y).color)
					pixmap.fill_rectangle ((x - 1) * 16, (y - 1) * 16, 16, 16)

					y := y + 1
				end

				x:= x + 1
			end
		end
end
