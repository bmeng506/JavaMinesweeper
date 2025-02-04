package org.minesweeper;

public class Minesweeper {
    private MinesweeperTile[][] board;
    private int difficulty; // 0: easy, 1: medium, 2: hard
    private boolean gameOver;
    private int numberOfMinesOnBoard; // number of mines on the board
    private int tilesRevealed;
    private boolean firstClick = true;
    private int flagsLeft;

    public Minesweeper(int difficulty) {
        reset(difficulty);
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public Minesweeper(MinesweeperTile[][] board, int numberOfMinesOnBoard) {
        this.board = board;
        gameOver = false;
        this.numberOfMinesOnBoard = numberOfMinesOnBoard;
        tilesRevealed = 0;
        this.flagsLeft = numberOfMinesOnBoard;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int getNumberOfMinesOnBoard() {
        return numberOfMinesOnBoard;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getTilesRevealed() {
        return tilesRevealed;
    }

    public boolean getFirstClick() {
        return firstClick;
    }

    public void setFirstClick(boolean firstClick) {
        this.firstClick = firstClick;
    }

    public MinesweeperTile[][] getBoard() {
        return board;
    }

    public int getFlagsLeft() {
        return flagsLeft;
    }

    public void setFlagsLeft(int flagsLeft) {
        this.flagsLeft = flagsLeft;
    }

    public void changeFlags(boolean flagged) {
        if (flagged) {
            flagsLeft++;
        } else {
            flagsLeft--;
        }
    }

    public boolean placeFlagValid(int row, int col) {
        if (firstClick || gameOver) {
            return false;
        }

        MinesweeperTile tile = board[row][col];
        if (!tile.isRevealed()) {
            boolean flagged = tile.isFlagged();
            if (flagged || flagsLeft > 0) {
                tile.setFlagged();
                changeFlags(flagged);
                return true;
            }
        }
        return false;
    }

    private void generateMinesAfterFirstClick(int row, int col) {
        int mines = 0;
        int rows = board.length;
        int cols = board[0].length;

        while (mines < numberOfMinesOnBoard) {
            int r = (int) (rows * Math.random());
            int c = (int) (cols * Math.random());

            boolean isAdjacent = Math.abs(r - row) <= 1 && Math.abs(c - col) <= 1;

            if (!isAdjacent && board[r][c].getType() != 1) {
                board[r][c] = new MinesweeperTile(1);
                mines++;
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                changeMinesSurrounding(i, j);
            }
        }
    }

    public void changeMinesSurrounding(int row, int col) {
        if (board[row][col].getType() == 1 || board[row][col].getType() == 2) {
            return;
        }

        int minesSurrounding = 0;
        for (int i = Math.max(row - 1, 0); i <= Math.min(row + 1, board.length - 1); i++) {
            for (int j = Math.max(col - 1, 0); j <= Math.min(col + 1, board[0].length - 1); j++) {
                if (board[i][j].getType() == 1) {
                    minesSurrounding++;
                }
            }
        }

        board[row][col].setMinesSurrounding(minesSurrounding);
    }

    public void revealMines() {
        for (MinesweeperTile[] minesweeperTiles : board) {
            for (int j = 0; j < board[0].length; j++) {
                if (minesweeperTiles[j].getType() == 1) {
                    minesweeperTiles[j].setRevealed();
                }
            }
        }
        gameOver = true;
    }

    public void revealTile(int row, int col) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return;
        }

        if (gameOver || board[row][col].isRevealed()) {
            return;
        }

        if (board[row][col].isFlagged()) {
            return;
        }

        if (firstClick) {
            generateMinesAfterFirstClick(row, col);
            firstClick = false;
        }

        if (board[row][col].getType() == 1) {
            revealMines();
            tilesRevealed = 0;
            return;
        }

        board[row][col].setRevealed();
        tilesRevealed++;

        if (board[row][col].getMinesSurrounding() == 0) {
            for (int i = Math.max(row - 1, 0); i <= Math.min(row + 1, board.length - 1); i++) {
                for (int j = Math.max(col - 1, 0); j <= Math
                        .min(col + 1, board[0].length - 1); j++) {
                    if (board[i][j].getType() != 1 && !board[i][j].isRevealed()) {
                        revealTile(i, j);
                    }
                }
            }
        }
    }

    public void reset(int difficulty) {
        this.difficulty = difficulty;
        if (difficulty == 1) {
            board = new MinesweeperTile[8][8];
            this.numberOfMinesOnBoard = 10;
            this.flagsLeft = 10;
        } else if (difficulty == 2) {
            board = new MinesweeperTile[16][12];
            this.numberOfMinesOnBoard = 24;
            this.flagsLeft = 24;
        } else {
            board = new MinesweeperTile[24][16];
            this.numberOfMinesOnBoard = 48;
            this.flagsLeft = 48;
        }

        // resetting variables
        this.firstClick = true;
        this.gameOver = false;
        this.tilesRevealed = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = new MinesweeperTile(0);
            }
        }

        // setting numbers for tiles
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                changeMinesSurrounding(i, j);
            }
        }
    }
}
