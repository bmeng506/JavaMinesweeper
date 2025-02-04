package org.minesweeper;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.*;

public class RunMinesweeper implements Runnable {
    public void instructionsWindow(JFrame frame) {
        JFrame instructions = new JFrame("Minesweeper Instructions");
        instructions.setSize(600, 200);
        instructions.setLocationRelativeTo(frame);

        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new BorderLayout());

        JTextArea instructionsText = new JTextArea();
        instructionsText.setEditable(false);
        instructionsText.setLineWrap(true);
        instructionsText.setWrapStyleWord(true);
        instructionsText.setBackground(Color.WHITE);
        instructionsText.setText(
                "  \n  Welcome to Minesweeper! \n\n" +
                        "  The objective of the game is to safely uncover all the non-mine " +
                        "tiles.\n\n"
                        +
                        "  The number on the tile represents the number of mines in the 3x3 " +
                        "area around the tile. \n"
                        +
                        "  e.g. If a tile shows '3', then there are 3 mines in the 8 tiles " +
                        "around it.\n\n"
                        +
                        "  To play: \n" +
                        "     Left-click on a tile to reveal.\n" +
                        "     Right-click on a tile to flag it as a potential mine\n" +
                        "     If you reveal all non-mine tiles, you win!\n" +
                        "     There is a flag counter to show the number of flags you have " +
                        "left.\n\n"
                        +
                        "  There are three difficulties: Easy, Medium, and Hard, each with " +
                        "larger boards and " +
                        "more mines!"
                        +
                        "  There is also a feature that your progress will save when you leave " +
                        "and return!\n"
        );
        instructionsPanel.add(instructionsText, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> instructions.dispose());
        instructionsPanel.add(closeButton, BorderLayout.SOUTH);
        instructions.add(instructionsPanel);
        instructions.setVisible(true);
        instructions.pack();
    }

    public void run() {

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Minesweeper");
        frame.setLocation(50, 50);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Minesweeper!");
        status_panel.add(status);

        // Game board
        final MinesweeperBoard board = new MinesweeperBoard(status, 1);
        frame.add(board, BorderLayout.CENTER);

        // mines left
        status.setText("Minesweeper! Flags left: " + board.getMs().getFlagsLeft());

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        String[] difficulties = { "Easy", "Medium", "Hard" };
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        control_panel.add(new JLabel("Difficulty: "));

        for (int i = 0; i < difficulties.length; i++) {
            String difficulty = difficulties[i];
            JButton button = new JButton(difficulty);

            int difficultyIndex = i + 1;
            button.addActionListener(e -> {
                board.reset(difficultyIndex);
            });

            buttonPanel.add(button);
        }

        control_panel.add(buttonPanel);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> {
            int difficulty = board.getMs().getDifficulty();
            board.reset(difficulty);
            status.setText("Minesweeper!");
        });
        control_panel.add(reset);

        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(e -> instructionsWindow(frame));
        control_panel.add(instructions);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                MinesweeperSaver saver = null;
                try {
                    saver = new MinesweeperSaver("Minesweeper_Save_Data");
                    saver.saveFile(board.getMs());
                    saver.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        MinesweeperSaver saver;
        try {
            saver = new MinesweeperSaver("Minesweeper_Save_Data");
            saver.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Minesweeper savedVersion = saver.readFile();

        if (savedVersion != null) {
            board.updateGameFromSaved(savedVersion);
            board.checkGameStatus();
        } else {
            board.reset(1);
        }
        instructionsWindow(frame);
    }
}