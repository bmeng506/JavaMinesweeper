package org.minesweeper;

import org.junit.jupiter.api.*;

import javax.swing.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MinesweeperTest {
    Minesweeper ms;

    @BeforeEach
    public void setUp() {
        ms = new Minesweeper(1);
    }

    @Test
    public void checkResetConditions() {
        ms.reset(1); // easy difficulty

        for (int row = 0; row < ms.getBoard().length; row++) {
            for (int col = 0; col < ms.getBoard()[0].length; col++) {
                assertEquals(0, ms.getBoard()[row][col].getType());
            }
        }
        assertTrue(ms.getFirstClick());
        assertFalse(ms.isGameOver());
        assertEquals(0, ms.getTilesRevealed());
        assertEquals(10, ms.getNumberOfMinesOnBoard()); // easy mode has 10 mines
    }

    @Test
    public void checkWinCondition() {
        JLabel status = new JLabel();
        MinesweeperBoard board = new MinesweeperBoard(status, 1);
        Minesweeper ms = board.getMs();
        ms.reset(1);
        ms.revealTile(0, 0);

        for (int row = 0; row < ms.getBoard().length; row++) {
            for (int col = 0; col < ms.getBoard()[0].length; col++) {
                if (ms.getBoard()[row][col].getType() == 0) {
                    ms.revealTile(row, col);
                }
            }
        }

        board.checkGameStatus();
        assertTrue(ms.isGameOver());

        assertEquals("You Win! All mines are swept!", status.getText());
    }

    @Test
    public void checkLossCondition() {
        JLabel status = new JLabel();
        MinesweeperBoard board = new MinesweeperBoard(status, 1);
        Minesweeper ms = board.getMs();
        ms.reset(1);
        ms.revealTile(0, 0);

        for (int row = 0; row < ms.getBoard().length; row++) {
            for (int col = 0; col < ms.getBoard()[0].length; col++) {
                if (ms.getBoard()[row][col].getType() == 1) {
                    ms.revealTile(row, col);
                    break;
                }
            }
        }

        board.checkGameStatus();
        assertTrue(ms.isGameOver());

        assertEquals("Game Over! You hit a mine. Press Reset to try again!", status.getText());
    }

    @Test
    public void checkAllMinesRevealed() {
        // all mines are revealed when one is hit
        JLabel status = new JLabel();
        MinesweeperBoard board = new MinesweeperBoard(status, 1);
        Minesweeper ms = board.getMs();
        ms.reset(1);
        ms.revealTile(0, 0);

        for (int row = 0; row < ms.getBoard().length; row++) {
            for (int col = 0; col < ms.getBoard()[0].length; col++) {
                if (ms.getBoard()[row][col].getType() == 1) {
                    ms.revealTile(row, col);
                    break;
                }
            }
        }

        for (int row = 0; row < ms.getBoard().length; row++) {
            for (int col = 0; col < ms.getBoard()[0].length; col++) {
                if (ms.getBoard()[row][col].getType() == 1) {
                    assertTrue(ms.getBoard()[row][col].isRevealed());
                }
            }
        }
    }

    @Test
    public void testFunctionalityOfFlags() {
        MinesweeperTile[][] board = new MinesweeperTile[3][3];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                board[row][col] = new MinesweeperTile(0);
            }
        }

        board[0][2] = new MinesweeperTile(1);
        board[1][1] = new MinesweeperTile(1);
        board[2][2] = new MinesweeperTile(1);

        Minesweeper test = new Minesweeper(board, 1);

        test.revealTile(0, 0);
        assertFalse(board[1][2].isFlagged());

        board[1][2].setFlagged();
        assertTrue(board[1][2].isFlagged());

        test.revealTile(0, 1);
        assertFalse(board[1][2].isRevealed());

        board[1][2].setFlagged();
        assertFalse(board[1][2].isFlagged());

        test.revealTile(1, 2);
        assertTrue(board[1][2].isRevealed());
    }

    @Test
    public void testFirstClick() {
        JLabel status = new JLabel();
        MinesweeperBoard board = new MinesweeperBoard(status, 1);
        Minesweeper ms = board.getMs();
        ms.reset(1);
        ms.revealTile(0, 0);

        assertEquals(0, ms.getBoard()[0][0].getType());

        for (int row = 0; row < 1; row++) {
            for (int col = 0; col < 1; col++) {
                assertEquals(0, ms.getBoard()[row][col].getType());
            }
        }
    }

    @Test
    public void testBoundary() {
        JLabel status = new JLabel();
        MinesweeperBoard board = new MinesweeperBoard(status, 1);
        Minesweeper ms = board.getMs();
        ms.reset(1);
        ms.revealTile(-1, 0);
        ms.revealTile(10, 0);

        assertTrue(ms.getFirstClick());
    }

    @Test
    public void testSurroundingCount() {
        MinesweeperTile[][] board = new MinesweeperTile[3][3];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                board[row][col] = new MinesweeperTile(0);
            }
        }

        board[0][1] = new MinesweeperTile(1);
        board[1][1] = new MinesweeperTile(1);

        Minesweeper test = new Minesweeper(board, 2);

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                test.changeMinesSurrounding(row, col);
            }
        }
        assertEquals(2, test.getBoard()[0][0].getMinesSurrounding());
        assertEquals(1, test.getBoard()[2][2].getMinesSurrounding());
    }

    @Test
    public void testSaveLoad() throws IOException {
        MinesweeperTile[][] board = new MinesweeperTile[3][3];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                board[row][col] = new MinesweeperTile(0);
            }
        }

        board[1][1] = new MinesweeperTile(1);
        Minesweeper test = new Minesweeper(board, 1);

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                test.changeMinesSurrounding(row, col);
            }
        }

        test.revealTile(0, 0);
        board[2][2].setFlagged();
        test.revealTile(0, 1);

        MinesweeperSaver saver = new MinesweeperSaver("test.txt");
        saver.saveFile(test);

        Minesweeper loaded = saver.readFile();

        assertEquals(test.getDifficulty(), loaded.getDifficulty());
        assertEquals(test.isGameOver(), loaded.isGameOver());
        assertEquals(test.getNumberOfMinesOnBoard(), loaded.getNumberOfMinesOnBoard());

        for (int row = 0; row < test.getBoard().length; row++) {
            for (int col = 0; col < test.getBoard()[0].length; col++) {
                assertEquals(
                        test.getBoard()[row][col].getType(),
                        loaded.getBoard()[row][col].getType()
                );
                assertEquals(
                        test.getBoard()[row][col].isRevealed(),
                        loaded.getBoard()[row][col].isRevealed()
                );
                assertEquals(
                        test.getBoard()[row][col].isFlagged(),
                        loaded.getBoard()[row][col].isFlagged()
                );
                assertEquals(
                        test.getBoard()[row][col].getMinesSurrounding(),
                        loaded.getBoard()[row][col].getMinesSurrounding()
                );
            }
        }
        saver.close();
    }

    @Test
    public void testSaveLoadGameOverClick() throws IOException {
        // edge case where player loses, exits, comes back, and attempts to click
        // which shouldn't work
        MinesweeperTile[][] board = new MinesweeperTile[3][3];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                board[row][col] = new MinesweeperTile(0);
            }
        }

        board[1][1] = new MinesweeperTile(1);
        Minesweeper test = new Minesweeper(board, 1);

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                test.changeMinesSurrounding(row, col);
            }
        }

        test.revealTile(0, 0);
        test.revealTile(1, 1); // loses game :(
        assertTrue(test.isGameOver());

        MinesweeperSaver saver = new MinesweeperSaver("test.txt");
        saver.saveFile(test);

        Minesweeper loaded = saver.readFile();
        assertTrue(loaded.isGameOver());

        loaded.revealTile(0, 1);
        assertFalse(loaded.getBoard()[0][1].isRevealed());

        saver.close();
    }

    @Test
    public void testFlagsAtLimit() {
        MinesweeperTile[][] testBoard = new MinesweeperTile[3][3];
        for (int i = 0; i < testBoard.length; i++) {
            for (int j = 0; j < testBoard[0].length; j++) {
                testBoard[i][j] = new MinesweeperTile(0);
            }
        }

        testBoard[1][1] = new MinesweeperTile(1);
        testBoard[1][2] = new MinesweeperTile(1);
        testBoard[2][1] = new MinesweeperTile(1);

        Minesweeper test = new Minesweeper(testBoard, 3);
        testBoard[1][1].setFlagged();
        testBoard[1][2].setFlagged();
        testBoard[2][1].setFlagged();
        testBoard[2][2].setFlagged();

        assertFalse(test.placeFlagValid(2, 2));
    }

    @Test
    public void testFlagsAtFirstClick() {
        MinesweeperTile[][] testBoard = new MinesweeperTile[3][3];
        for (int i = 0; i < testBoard.length; i++) {
            for (int j = 0; j < testBoard[0].length; j++) {
                testBoard[i][j] = new MinesweeperTile(0);
            }
        }

        testBoard[1][1] = new MinesweeperTile(1);
        testBoard[1][2] = new MinesweeperTile(1);
        testBoard[2][1] = new MinesweeperTile(1);
        Minesweeper test = new Minesweeper(testBoard, 3);

        assertFalse(test.placeFlagValid(1, 1));
    }
}
