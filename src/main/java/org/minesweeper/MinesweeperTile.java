package org.minesweeper;

public class MinesweeperTile {
    private int type; // 0: safe, 1: mine
    private boolean revealed = false;
    private int minesSurrounding; // 4 digit number, 0 or 1 for first digit, 0 or 1 for true or
                                  // false, # of mines third digit, flagged 0 or 1 flagged
    private boolean flagged = false;

    public MinesweeperTile(int type) {
        this.type = type;
        this.minesSurrounding = 0;
    }

    public int getType() {
        return type;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed() {
        this.revealed = true;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged() {
        this.flagged = !flagged;
    }

    public int getMinesSurrounding() {
        return minesSurrounding;
    }

    public void setMinesSurrounding(int minesSurrounding) {
        this.minesSurrounding = minesSurrounding;
    }

}
