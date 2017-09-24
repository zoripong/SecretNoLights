package model;

import java.awt.Image;

public class GameObj {
	private int posX;
	private int posY;	
	private Image image;

	public GameObj(int x, int y, Image image) {
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
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	
	
	
}
