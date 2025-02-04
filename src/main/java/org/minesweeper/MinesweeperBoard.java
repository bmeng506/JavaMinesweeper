package org.minesweeper;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class MinesweeperBoard extends JPanel {
    private Minesweeper ms;
    private JLabel status;

    public MinesweeperBoard(JLabel status, int difficulty) {
        this.status = status;
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFocusable(true);

        ms = new Minesweeper(difficulty);
        setPreferredSize(new Dimension(800, 600));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!ms.isGameOver()) {
                    int col = e.getY() / 30;
                    int row = e.getX() / 30;

                    if (row >= 0 && row < ms.getBoard().length &&
                            col >= 0 && col < ms.getBoard()[0].length) {

                        if (e.getButton() == MouseEvent.BUTTON1) {
                            ms.revealTile(row, col);
                            checkGameStatus();
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            boolean flag = ms.placeFlagValid(row, col);
                            if (flag) {
                                status.setText("Minesweeper! Flags left: " + ms.getFlagsLeft());
                            }
                        }
                        repaint();
                    }
                }
            }
        });

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < ms.getBoard().length; i++) {
            for (int j = 0; j < ms.getBoard()[0].length; j++) {
                int x = i * 30;
                int y = j * 30;
                MinesweeperTile tile = ms.getBoard()[i][j];

                if (tile.isRevealed()) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                }
                g.fillRect(x, y, 30, 30);

                if (tile.isRevealed()) {
                    if (tile.getType() == 1) {
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Arial", Font.BOLD, 16));
                        g.fillOval(x + 5, y + 4, 20, 20);
                    } else if (tile.getMinesSurrounding() > 0) {
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Arial", Font.BOLD, 16));
                        g.drawString(Integer.toString(tile.getMinesSurrounding()), x + 10, y + 20);
                    }
                } else if (tile.isFlagged()) {
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 16));
                    String flag = "▶";
                    g.drawString(flag, x + 9, y + 20);
                }

                g.setColor(Color.BLACK);
                g.drawRect(x, y, 30, 30);
            }
        }
    }

    public Minesweeper getMs() {
        return ms;
    }

    public void checkGameStatus() {
        if (ms.isGameOver()) {
            status.setText("Game Over! You hit a mine. Press Reset to try again!");
        }

        int rows = ms.getBoard().length;
        int cols = ms.getBoard()[0].length;

        if (ms.getTilesRevealed() == rows * cols - ms.getNumberOfMinesOnBoard()) {
            status.setText("You Win! All mines are swept!");
            ms.revealMines();
        }
    }

    public void updateGameFromSaved(Minesweeper savedData) {

        ms = new Minesweeper(savedData.getDifficulty());

        MinesweeperTile[][] loadedBoard = ms.getBoard();
        MinesweeperTile[][] savedBoard = savedData.getBoard();

        ms.setGameOver(savedData.isGameOver());
        ms.setFlagsLeft(savedData.getFlagsLeft());

        if (ms.isGameOver()) {
            status.setText("Game Over! Press Reset to start again!");
            ms.revealMines();
        }

        for (int i = 0; i < ms.getBoard().length; i++) {
            for (int j = 0; j < ms.getBoard()[0].length; j++) {
                MinesweeperTile savedTile = savedBoard[i][j];
                loadedBoard[i][j] = new MinesweeperTile(savedTile.getType());
                loadedBoard[i][j].setMinesSurrounding(savedTile.getMinesSurrounding());
                if (savedTile.isRevealed()) {
                    loadedBoard[i][j].setRevealed();
                }
                if (savedTile.isFlagged()) {
                    loadedBoard[i][j].setFlagged();
                }
            }
        }

        status.setText("Welcome Back! Right click to flag tiles!");
        ms.setFirstClick(false);

        repaint();
        requestFocusInWindow();
    }

    public void reset(int difficulty) {
        // internal state reset
        ms.reset(difficulty);
        int rows = ms.getBoard().length;
        int cols = ms.getBoard()[0].length;
        setPreferredSize(new Dimension(cols * 30, rows * 30));
        status.setText("Minesweeper! Flags left " + ms.getFlagsLeft());

        MinesweeperSaver saver;
        try {
            saver = new MinesweeperSaver("Minesweeper_Save_Data");
            saver.clearSavedData();
            saver.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        repaint();
        requestFocusInWindow();
    }
}