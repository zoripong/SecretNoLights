package model;

import java.awt.Image;

import javax.swing.ImageIcon;

public class GameObj {
	private int posX;
	private int posY;
	private ImageIcon image;

	public GameObj(int x, int y, ImageIcon image) {
		posX = x;
		posY = y;
		this.image = image;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public Image getImage() {
		return image.getImage();
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}

	public int getWidth() {
		return image.getIconWidth();
	}

	public int getHeight() {
		return image.getIconHeight();
	}

	public int[] getA() {
		return new int[] { posX, posY };
	}

	public int[] getB() {
		return new int[] { posX, posY + image.getIconHeight() };
	}

	public int[] getC() {
		return new int[] { posX + image.getIconWidth(), posY + image.getIconHeight() };
	}

	public int[] getD() {
		return new int[] { posX + image.getIconWidth(), posY };
	}

}
