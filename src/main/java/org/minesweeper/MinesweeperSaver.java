package org.minesweeper;

import java.io.*;

public class MinesweeperSaver extends BufferedReader {
    private String fileName;

    public MinesweeperSaver(String fileName) throws IOException {
        super(new FileReader(fileName));
        this.fileName = fileName;

        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public void saveFile(Minesweeper ms) {
        if (ms.getFirstClick()) {
            return;
        }

        try {
            BufferedWriter saver = new BufferedWriter(new FileWriter(fileName));

            saver.write(Integer.toString(ms.getBoard().length));
            saver.newLine();
            saver.write(Integer.toString(ms.getBoard()[0].length));
            saver.newLine();

            saver.write(Integer.toString(ms.getDifficulty()));
            saver.newLine();

            saver.write(Boolean.toString(ms.isGameOver()));
            saver.newLine();

            saver.write(Integer.toString(ms.getNumberOfMinesOnBoard()));
            saver.newLine();

            saver.write(Integer.toString(ms.getFlagsLeft()));
            saver.newLine();

            saver.write(Boolean.toString(ms.getFirstClick()));
            saver.newLine();

            MinesweeperTile[][] board = ms.getBoard();

            for (int i = 0; i < board.length; i++) {
                StringBuilder row = new StringBuilder();
                for (int j = 0; j < board[i].length; j++) {
                    MinesweeperTile tile = board[i][j];
                    int type = tile.getType() * 1000;
                    int reveal = 0;
                    if (tile.isRevealed()) {
                        reveal = 100;
                    }
                    int mines = tile.getMinesSurrounding() * 10;
                    int flag = 0;
                    if (tile.isFlagged()) {
                        flag = 1;
                    }

                    int state = type + reveal + mines + flag;
                    row.append(state);
                    if (j < board[i].length - 1) {
                        row.append(",");
                    }
                }
                saver.write(row.toString());
                saver.newLine();
            }

            if (ms.isGameOver()) {
                saver.write(1);
            }
            saver.close();
        } catch (IOException e) {
            System.out.println("Error saving Minesweeper");
        }
    }

    public Minesweeper readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            int rows = Integer.parseInt(reader.readLine());
            int cols = Integer.parseInt(reader.readLine());

            int difficulty = Integer.parseInt(reader.readLine());
            boolean gameOver = Boolean.parseBoolean(reader.readLine());
            int numberOfMines = Integer.parseInt(reader.readLine());
            int flagsLeft = Integer.parseInt(reader.readLine());
            boolean firstClick = Boolean.parseBoolean(reader.readLine());

            MinesweeperTile[][] board = new MinesweeperTile[rows][cols];

            boolean anyMines = false;

            for (int i = 0; i < board.length; i++) {
                String[] encodedTiles = reader.readLine().split(",");
                for (int j = 0; j < board[0].length; j++) {
                    int state = Integer.parseInt(encodedTiles[j]);

                    int type = state / 1000;
                    int reveal = (state % 1000) / 100;
                    int mines = (state % 100) / 10;
                    int flag = state % 10;

                    board[i][j] = new MinesweeperTile(type);

                    if (type == 1) {
                        anyMines = true;
                    }

                    if (reveal == 1) {
                        board[i][j].setRevealed();
                    }

                    board[i][j].setMinesSurrounding(mines);

                    if (flag == 1) {
                        board[i][j].setFlagged();
                    }
                }
            }

            reader.close();

            if (!anyMines && firstClick) {
                return new Minesweeper(difficulty);
            }

            Minesweeper ms = new Minesweeper(board, numberOfMines);

            if (gameOver) {
                ms.setGameOver(true);
            }
            ms.setFlagsLeft(flagsLeft);
            ms.setFirstClick(firstClick);
            ms.setDifficulty(difficulty);
            return ms;

        } catch (IOException e) {
            System.out.println("Error reading Minesweeper");
            return new Minesweeper(1);
        }
    }

    public void clearSavedData() throws IOException {
        BufferedWriter saver = new BufferedWriter(new FileWriter(fileName));
        saver.write("");
        saver.close();
    }

    // store game state, store bombs (indices), store difficulty (how big array),
    // which squares are revealed, and numbers, start the game again,
    // BufferedReader, BufferedWriter, Iterators
}
