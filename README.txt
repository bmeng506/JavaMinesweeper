Used 2D arrays to make the internal state of the game, mapping the tiles. This 2D array consisted of
tiles of the MinesweeperTile.java class, which had fields that represented the type of tile they were, the number
mines around it, if it was revealed, and if it was flagged. 

Used File I/O to save the data of the gameboard if the player chose to leave the game and rerun the 
game. The data of the game was saved into a file called "Minesweeper_Save_Data", and all aspects of the game were
saved, such as tile type, number of mines, number of tiles flagged, etc. File I/O is used to preserve the state of
the game at any time the player chose to close the window and return. The save and load functions were triggered
when the player chose to close the window or reopen the game window.

Created a class in the test folder to test functions of the Minesweeper game. Tested basic functionality of the 
internal state of the game, but also edge cases, like if the player chose to return to the game after they lost, 
and decided to click around (which should lead to doing nothing). Tested flag functionality (clicking flagged 
tiles does nothing) and tested game-over conditions, so that players could no longer interact with the board 
after they lost (except reset or pick a different difficulty).

Used recursion in the revealTile method, which triggered whenever the player attempted to left-click on a tile. 
For example, if the tile was an empty tile with no mines surrounding it, revealTile would recurse over the tiles surrounding it, effectively revealing all tiles that are adjacent to it that are either empty or have a number of mines surrounding it. However, it is limited whenever the tile has a number of mines surrounding it, so it does not reveal too much of the board. Essentially, the function stops recursing when it recognizes that the tile it is on has at least one mine adjacent to it.
