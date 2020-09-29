package com.svetroid.java;

import com.svetroid.java.puzzle.Puzzle;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

  public static final String PUZZLE_PATH = "res/puzzle/puzzle.png";

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        ex.printStackTrace();
      }
      Puzzle puzzle = new Puzzle(PUZZLE_PATH, 4);
      puzzle.setVisible(true);
    });
  }

}
