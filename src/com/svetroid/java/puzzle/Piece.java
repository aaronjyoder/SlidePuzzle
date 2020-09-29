package com.svetroid.java.puzzle;

import java.awt.image.BufferedImage;

public class Piece {

  private BufferedImage image;
  private Position pos;
  private boolean isBlank;

  public Piece(BufferedImage image, Position pos) {
    this.image = image;
    this.pos = pos;
    isBlank = false;
  }

  public void setPosition(int x, int y) {
    this.pos.setX(x);
    this.pos.setY(y);
  }

  public void setBlank(boolean blank) {
    this.isBlank = blank;
  }

  public BufferedImage getImage() {
    return image;
  }

  public Position getPosition() {
    return pos;
  }

  public boolean isBlank() {
    return isBlank;
  }

}
