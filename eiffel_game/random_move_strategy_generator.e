note
	description: "Random generator for mouse move strategies"
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	RANDOM_MOVE_STRATEGY_GENERATOR

create
	make

feature {NONE}

	random: RANDOM

	move_strategies: LIST [FUNCTION [POSITION, POSITION]]

	make
			-- Initializes the move strategies
		do
			create random.make

			move_strategies := create {ARRAYED_LIST [FUNCTION [POSITION, POSITION]]}.make (4)
			move_strategies.extend (agent (position: POSITION): POSITION do Result := create {POSITION}.make_at_row_and_col (position.row - 1, position.col) end)
			move_strategies.extend (agent (position: POSITION): POSITION do Result := create {POSITION}.make_at_row_and_col (position.row, position.col + 1) end)
			move_strategies.extend (agent (position: POSITION): POSITION do Result := create {POSITION}.make_at_row_and_col (position.row + 1, position.col) end)
			move_strategies.extend (agent (position: POSITION): POSITION do Result := create {POSITION}.make_at_row_and_col (position.row, position.col - 1) end)
		end

feature
	
	get_random_move_strategy: FUNCTION [POSITION, POSITION]
			-- Returns a random move strategy
		do
			random.forth
			Result := move_strategies.at((random.item \\ 4) + 1)
		end

end
