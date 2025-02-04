package org;

import javax.swing.*;

public class Game {
    public static void main(String[] args) {
        Runnable game = new org.minesweeper.RunMinesweeper();
        SwingUtilities.invokeLater(game);
    }
}
