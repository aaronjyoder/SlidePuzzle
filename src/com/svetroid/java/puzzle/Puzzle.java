package com.svetroid.java.puzzle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Puzzle extends JFrame {

  private BufferedImage image;
  private int size;
  private Piece[][] pieces;
  public static final int TILE_SIZE = 100;
  public static final Color BLANK_COLOR = new Color(50, 40, 30, 255);

  private JLabel[][] tileLabelHolder;
  private JPanel panel;

  public Puzzle(String imagePath, int size) {
    this.size = size;
    pieces = new Piece[size][size];
    try {
      image = ImageIO.read(new File(imagePath));
    } catch (IOException e) {
      e.printStackTrace();
    }
    generatePieces();
    generatePuzzle();

    // Window generation.
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Slide Puzzle");
    tileLabelHolder = new JLabel[size][size];
    panel = new JPanel(new GridBagLayout());

    // Creating the menu.
    JMenuBar menuBar = new JMenuBar();
    JMenu menuFile = new JMenu("File");
    JMenuItem menuItemNew = new JMenuItem(new AbstractAction("New") {
      @Override
      public void actionPerformed(ActionEvent e) {
        generatePuzzle();
        rebuildPanel();
      }
    });
    JMenuItem menuItemOpen = new JMenuItem(new AbstractAction("Open...") {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "png", "jpg", "jpeg");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          try {
            BufferedImage temp = image;
            image = ImageIO.read(chooser.getSelectedFile());
            if (image.getHeight() == image.getWidth()) {
              generatePieces();
              generatePuzzle();
              rebuildPanel();
            } else {
              JOptionPane.showMessageDialog(null, "Image must be a square.");
              image = temp;
            }
          } catch (IOException ex) {
            ex.printStackTrace();
          }
        }
      }
    });

    JMenu menuSize = new JMenu("Size");
    JMenuItem menuItemSize2 = new JMenuItem(new AbstractAction("2x2") {
      @Override
      public void actionPerformed(ActionEvent e) {
        setPuzzleSize(2);
        generatePuzzle();
        rebuildPanel();
        pack();
        setLocationRelativeTo(null);
      }
    });
    JMenuItem menuItemSize3 = new JMenuItem(new AbstractAction("3x3") {
      @Override
      public void actionPerformed(ActionEvent e) {
        setPuzzleSize(3);
        generatePuzzle();
        rebuildPanel();
        pack();
        setLocationRelativeTo(null);
      }
    });
    JMenuItem menuItemSize4 = new JMenuItem(new AbstractAction("4x4") {
      @Override
      public void actionPerformed(ActionEvent e) {
        setPuzzleSize(4);
        generatePuzzle();
        rebuildPanel();
        pack();
        setLocationRelativeTo(null);
      }
    });
    JMenuItem menuItemSize5 = new JMenuItem(new AbstractAction("5x5") {
      @Override
      public void actionPerformed(ActionEvent e) {
        setPuzzleSize(5);
        generatePuzzle();
        rebuildPanel();
        pack();
        setLocationRelativeTo(null);
      }
    });
    JMenuItem menuItemSize6 = new JMenuItem(new AbstractAction("6x6") {
      @Override
      public void actionPerformed(ActionEvent e) {
        setPuzzleSize(6);
        generatePuzzle();
        rebuildPanel();
        pack();
        setLocationRelativeTo(null);
      }
    });
    JMenuItem menuItemSize7 = new JMenuItem(new AbstractAction("7x7") {
      @Override
      public void actionPerformed(ActionEvent e) {
        setPuzzleSize(7);
        generatePuzzle();
        rebuildPanel();
        pack();
        setLocationRelativeTo(null);
      }
    });
    JMenuItem menuItemSize8 = new JMenuItem(new AbstractAction("8x8") {
      @Override
      public void actionPerformed(ActionEvent e) {
        setPuzzleSize(8);
        generatePuzzle();
        rebuildPanel();
        pack();
        setLocationRelativeTo(null);
      }
    });

    JMenu menuHint = new JMenu("Hint");
    menuHint.addMouseListener(hintListener());

    menuFile.add(menuItemNew);
    menuFile.add(menuItemOpen);
    menuSize.add(menuItemSize2);
    menuSize.add(menuItemSize3);
    menuSize.add(menuItemSize4);
    menuSize.add(menuItemSize5);
    menuSize.add(menuItemSize6);
    menuSize.add(menuItemSize7);
    menuSize.add(menuItemSize8);
    menuBar.add(menuFile);
    menuBar.add(menuSize);
    menuBar.add(Box.createHorizontalGlue());
    menuBar.add(menuHint);
    setJMenuBar(menuBar);

    rebuildPanel();
    add(panel, BorderLayout.CENTER);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);
  }

  private MouseListener tileListener() {
    MouseListener ml = new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent e) {

        Piece currentPiece = null;
        boolean hasFoundPiece = false;
        for (int y = 0; y < tileLabelHolder.length; y++) {
          for (int x = 0; x < tileLabelHolder.length; x++) {
            if (tileLabelHolder[x][y] == e.getSource()) {
              currentPiece = pieces[x][y];
              hasFoundPiece = true;
              break;
            }
          }
          if (hasFoundPiece) {
            break;
          }
        }
        if (currentPiece != null) {
          Piece blankPiece = findBlank(currentPiece);
          if (blankPiece != null) {
            movePiece(currentPiece, blankPiece.getPosition().getX(), blankPiece.getPosition().getY());
            rebuildPanel();
          }
        }

      }
    };
    return ml;
  }

  private MouseListener hintListener() {
    MouseListener ml = new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        panel.removeAll();
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 1;
        c.gridy = 1;
        JLabel imageLabel = new JLabel();
        imageLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, BLANK_COLOR));
        imageLabel.setIcon(new ImageIcon(resizeImage(image, size)));
        panel.add(imageLabel, c);
        panel.repaint();
        panel.revalidate();
      }

      @Override
      public void mouseExited(MouseEvent e) {
        rebuildPanel();
      }
    };
    return ml;
  }

  private void rebuildPanel() {
    panel.removeAll();
    for (int y = 0; y < pieces.length; y++) {
      for (int x = 0; x < pieces.length; x++) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = x;
        c.gridy = y;
        tileLabelHolder[x][y] = new JLabel();
        tileLabelHolder[x][y].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, BLANK_COLOR));
        tileLabelHolder[x][y].setIcon(new ImageIcon(pieces[x][y].getImage()));
        tileLabelHolder[x][y].addMouseListener(tileListener());
        panel.add(tileLabelHolder[x][y], c);
      }
    }
    panel.repaint();
    panel.revalidate();
  }

  private void movePiece(Piece p, int x, int y) {
    int oldX = p.getPosition().getX();
    int oldY = p.getPosition().getY();
    Piece temp = pieces[x][y];
    pieces[x][y] = p;
    pieces[oldX][oldY] = temp;
    p.setPosition(x, y);
    pieces[oldX][oldY].setPosition(oldX, oldY);
  }

  private Piece findBlank(Piece piece) {
    int x = piece.getPosition().getX();
    int y = piece.getPosition().getY();
    if (x - 1 > -1 && pieces[x - 1][y].isBlank()) {
      return pieces[x - 1][y];
    } else if (x + 1 < size && pieces[x + 1][y].isBlank()) {
      return pieces[x + 1][y];
    } else if (y - 1 > -1 && pieces[x][y - 1].isBlank()) {
      return pieces[x][y - 1];
    } else if (y + 1 < size && pieces[x][y + 1].isBlank()) {
      return pieces[x][y + 1];
    }
    return null;
  }

  private void generatePieces() {
    int pieceSize = image.getWidth() / size;
    for (int x = 0; x < size; x++) {
      for (int y = 0; y < size; y++) {
        pieces[x][y] = new Piece(resizeImage(image.getSubimage(x * pieceSize, y * pieceSize, pieceSize, pieceSize), 1), new Position(x, y));
      }
    }
    for (int x = 0; x < pieces[size - 1][size - 1].getImage().getWidth(); x++) {
      for (int y = 0; y < pieces[size - 1][size - 1].getImage().getHeight(); y++) {
        pieces[size - 1][size - 1].getImage().setRGB(x, y, (BLANK_COLOR).getRGB());
        pieces[size - 1][size - 1].setBlank(true);
      }
    }
  }

  private BufferedImage resizeImage(BufferedImage original, int multiplier) {
    BufferedImage resized = new BufferedImage(TILE_SIZE * multiplier, TILE_SIZE * multiplier, original.getType() == 0 ? 5 : original.getType());
    Graphics2D g = resized.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g.drawImage(original, 0, 0, TILE_SIZE * multiplier, TILE_SIZE * multiplier, 0, 0, original.getWidth(), original.getHeight(), null);
    g.dispose();
    return resized;
  }

  public BufferedImage generateImage() {
    BufferedImage result = new BufferedImage(size * TILE_SIZE, size * TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
    Graphics g = result.getGraphics();
    for (int x = 0; x < size; x++) {
      for (int y = 0; y < size; y++) {
        BufferedImage bi = pieces[x][y].getImage();
        g.drawImage(bi, x * TILE_SIZE, y * TILE_SIZE, null);
      }
    }
    return result;
  }

  public BufferedImage generatePuzzle() {
    int[] rand = new int[4];
    for (int i = 0; i < size * size; i++) {
      for (int r = 0; r < rand.length; r++) {
        rand[r] = ThreadLocalRandom.current().nextInt(0, size);
      }
      movePiece(pieces[rand[0]][rand[1]], rand[2], rand[3]);
    }
    return generateImage();
  }

  public int getPuzzleSize() {
    return size;
  }

  public void setPuzzleSize(int size) {
    this.size = size;
    pieces = new Piece[size][size];
    tileLabelHolder = new JLabel[size][size];
    generatePieces();
  }

  public BufferedImage getImage() {
    return image;
  }

  public Piece getPiece(int x, int y) {
    if (x < 0 || y < 0 || x >= size || y >= size) {
      return null;
    }
    return pieces[x][y];
  }

  public Piece[][] getPieces() {
    return pieces;
  }


}
