note
	description: "Maze view component"
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	MAZE_VIEW

inherit
	EV_VERTICAL_BOX

create
	make_for_maze

feature {NONE} -- Initialization

	draw_area: EV_PIXMAP
	status_bar: EV_STATUS_BAR
	status_label: EV_LABEL
	maze: MAZE

	make_for_maze(maze_model: MAZE)
			-- Initializes the view with the given maze model
		do
			maze := maze_model
			create draw_area
			create status_label
			create status_bar
			default_create

			initialize_draw_area
			initialize_status_bar

			maze.add_on_update_listener (agent on_maze_update)
		end

	initialize_draw_area
			-- Initializes the draw area
		do
			extend (draw_area)
			repaint_maze
		end

	initialize_status_bar
			-- Initializes the status bar
		do
			status_bar.set_border_width (2)
			extend (status_bar)
			disable_item_expand (status_bar)

			status_label.set_text ("Game running")
			status_label.align_text_left
			status_bar.extend (status_label)
		end

	on_maze_update
			-- Listener for maze model changes
		do
			if attached (create {EV_ENVIRONMENT}).application as app then
				app.do_once_on_idle (agent repaint_maze)
			end
		end

	repaint_maze
			-- Repaints the maze based on the current maze model state
		local
			new_draw_area_content: EV_PIXMAP
		do
			new_draw_area_content := create_new_pixmap

			draw_area.set_size (new_draw_area_content.width, new_draw_area_content.height)
			draw_area.draw_pixmap (0, 0, new_draw_area_content)
			draw_area.flush

			if maze.game_over then
				status_label.set_text ("Game over")
			end
		end

	create_new_pixmap: EV_PIXMAP
			-- Creates a new pixmap with the view on the current maze model state
		local
			pixmap: EV_PIXMAP
		do
			create pixmap.make_with_size (maze.fields.width * 16, maze.fields.height * 16)

			paint_fields (pixmap)
			paint_mice (pixmap)

			Result := pixmap
		end

	paint_fields(pixmap: EV_PIXMAP)
			-- Paints the fields of the maze on the given pixmap
		local
			x: INTEGER
			y: INTEGER
		do
			from y := 1
			until y > maze.fields.height
			loop
				from x := 1
				until x > maze.fields.width
				loop
					pixmap.set_foreground_color (maze.fields.item (y, x).color)
					pixmap.fill_rectangle ((x - 1) * 16, (y - 1) * 16, 16, 16)

					x := x + 1
				end

				y:= y + 1
			end
		end

	paint_mice (pixmap: EV_PIXMAP)
			-- Paints the mice of the maze on the given pixmap
		local
			i: INTEGER
			mouse: MOUSE
		do
			from i := 1
			until i > maze.get_mice.count
			loop
				mouse := maze.get_mice.at (i)

				pixmap.set_foreground_color (mouse.color)
				pixmap.fill_rectangle ((mouse.get_maze_position.col - 1) * 16, (mouse.get_maze_position.row - 1) * 16, 16, 16)

				i := i + 1
			end
		end
end
