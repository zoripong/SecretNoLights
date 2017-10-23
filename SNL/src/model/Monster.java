package model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import gui.SNL;

public class Monster extends GameObj {
	private int dx, dy;

	private int jumpIdx = 0;
	private double imageIdx = 0;

	private boolean isRight;

	//TODO
	public Monster(int x, int y, ImageIcon image) {
		super(x, y-image.getIconHeight()-10, image);
		System.out.println(image.getIconHeight());
		int num = (int) (Math.random() * 10);
		dy = 0;
		if (num % 2 == 0) {
			dx = 5;
			isRight = true;			
		} else {
			dx = -5;
			isRight = false;
		}
//		width = new ImageIcon(SNL.class.getResource("../images/front_monster.png")).getIconWidth();
	}

	public void update() {
		String imageFile;
		if (isRight) {
			imageFile = "../images/right_monster_" + String.valueOf((int) imageIdx % 3 + 1) + ".png";
		} else {
			imageFile = "../images/left_monster_" + String.valueOf((int) imageIdx % 3 + 1) + ".png";

		}
		setImage(new ImageIcon(SNL.class.getResource(imageFile)));

		imageIdx += 0.3;
		if (getPosX() > 900 - getWidth()) {
			dx = -5;
			isRight = false;
		}
		else if (getPosX() < 0) {
			dx = 5;
			isRight = true;
		}
		setPosX(getPosX() + dx);
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(getImage(), getPosX(), getPosY(), null);
		// System.out.println("(" + getPosX() + "," + getPosY() + ")");
	}

	public void changeDirection() {
		dx *= -1;
		isRight = !isRight;
	}

	public int getDx() {
		return dx;
	}
	
	public int getDy() {
		return dy;
	}
	
	public void setDx(int dx) {
		this.dx = dx;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}

	public int getJumpIdx() {
		return this.jumpIdx;
	}

	public void setJumpIdx(int jumpIdx) {
		this.jumpIdx = jumpIdx;
	}

	public boolean isRight() {
		return isRight;
	}
	

	public boolean isJumping() {
		if (this.jumpIdx == 0)
			return false;
		return true;
	}

	public void addY(int add) {
		setPosY(getPosY() + add);
	}
	

}
