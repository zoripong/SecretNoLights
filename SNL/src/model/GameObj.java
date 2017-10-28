package model;

import java.awt.Image;

import javax.swing.ImageIcon;

public class GameObj {
	private final int BLOCK_WIDTH = 43;

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

	public Location getLocation(char type) {
		switch(type) {
		case 'a':
		case 'A':
			return new Location( posX, posY );
		case 'b':
		case 'B':
			return new Location( posX, posY + image.getIconHeight() );
		case 'c':
		case 'C':
			return new Location( posX + image.getIconWidth(), posY + image.getIconHeight() );
		case 'd':
		case 'D':
			return new Location( posX + image.getIconWidth(), posY );
			default:
				return null;
		}
	}

}
