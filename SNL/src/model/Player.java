package model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import adapter.MapReader;
import customInterface.Attackable;
import customInterface.Direction;
import customInterface.JumpListener;
import gui.SNL;
import thread.JumpThread;

public class Player extends GameObj implements Direction, Attackable {
	private final int BLOCK_WIDTH = 43;

	private boolean isSelecter;
	private int dx, dy;
	private double imageIdx;
	private boolean isRight;
	private boolean isAttacking;
	private MapReader mMapReader;
	// private ImageIcon jumpRight[];
	int jumpIdx = 0;
	private int charType;
	private int life;
	
	private JumpListener jumpListener;

	public Player(int x, int y, ImageIcon image, int charType, MapReader mapReader) {
		super(x, y - image.getIconHeight(), image);
		this.charType = charType;
		isSelecter = false;
		dx = 9;
		dy = 0;
		isRight = true;
		imageIdx = 1; // 谅快 捞悼
		isAttacking = false;
		mMapReader = mapReader;
		life = 5;
	}

	public Player(int x, int y, ImageIcon image, int charType, int dx, int dy, boolean isSelecter) {
		this(x, y + image.getIconHeight(), image, charType, null);
		this.dx = dx;
		this.dy = dy;
		this.isSelecter = isSelecter;
	}

	public void move(int direction) {
		String imageFile;
		switch (direction) {
		case LEFT:
			imageFile = "../images/left_" + String.valueOf(charType) + "_" + String.valueOf((int) imageIdx % 3 + 1)
					+ ".png";
//			System.out.println(imageFile);
			setImage(new ImageIcon(SNL.class.getResource(imageFile)));
			imageIdx += 0.3;

			if (getPosX() < BLOCK_WIDTH)
				setPosX(BLOCK_WIDTH);
			if(!(mMapReader.isBlock(getLocation('A').getX(), getLocation('B').getY()))){
				setPosX(getPosX() - dx);
			}
			break;
		case RIGHT:
			imageFile = "../images/right_" + String.valueOf(charType) + "_" + String.valueOf((int) imageIdx % 3 + 1)
					+ ".png";
			setImage(new ImageIcon(SNL.class.getResource(imageFile)));
			imageIdx += 0.3;
			if (getPosX() > (SNL.SCREEN_WIDTH - getWidth() - BLOCK_WIDTH))
				setPosX(SNL.SCREEN_WIDTH - getWidth() - BLOCK_WIDTH);
			
			if(!(mMapReader.isBlock(getLocation('D').getX(), getLocation('C').getY())))
				setPosX(getPosX() + dx);
			break;

		case UP:
			if (isSelecter)
				setPosY(getPosY() - dy);
			break;
		case DOWN:
			if (isSelecter)
				setPosY(getPosY() + dy);
			
			break;
		}

	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(getImage(), getPosX(), getPosY(), null);
		// System.out.println("(" + getPosX() + "," + getPosY() + ")");
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}
	
	public int getDx() {
		return dx;
	}
	
	public int getDy() {
		return dy;
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
	
	public void setDirection(boolean isRight) {
		this.isRight = isRight;
	}

	public boolean isJumping() {
		if (this.jumpIdx == 0)
			return false;
		return true;
	}

	public boolean isAttack() {
		// TODO : attack 吝汗贸府 and Thread
		return isAttacking;
	}

	public void addX(int add) {
		setPosX(getPosX() + add);
	}

	public void addY(int add) {
		setPosY(getPosY() + add);
	}

	@Override
	public void attack() {
		// TODO 面倒贸府
		isAttacking = true;
//		System.out.println("attack");
		if (isRight)
			setImage(new ImageIcon(
					SNL.class.getResource("../images/attack_right_" + String.valueOf(charType) + ".png")));
		else {
			// TODO LEFT ATTACK
			setPosX(getPosX() - 60);
			setImage(
					new ImageIcon(SNL.class.getResource("../images/attack_left_" + String.valueOf(charType) + ".png")));
		}
	}

	public void attackEnd() {
		isAttacking = false;
		// if (!isRight)
		// setPosX(getPosX() + 60);

	}

	public int isCrush(GameObj obj) {
		int startX = getLocation('A').getX();
		int endX = getLocation('D').getX();

		int startY = getLocation('A').getY();
		int endY = getLocation('B').getY();

		if (isAttacking) {
			return -1;
		}
		if (startX <= obj.getLocation('A').getX() && obj.getLocation('A').getX() <= endX && startY <= obj.getLocation('A').getY() && obj.getLocation('A').getY() <= endY) {
//			System.out.println("1");
			return 1;
		}

		if (startX <= obj.getLocation('B').getX() && obj.getLocation('B').getX() <= endX && startY <= obj.getLocation('B').getY() && obj.getLocation('B').getY() <= endY) {
//			System.out.println("2");
			return 1;
		}

		if (startX <= obj.getLocation('C').getX() && obj.getLocation('C').getX() <= endX && startY <= obj.getLocation('C').getY() && obj.getLocation('C').getY() <= endY) {
//			System.out.println("3");
			return 1;
		}
		if (startX <= obj.getLocation('D').getX() && obj.getLocation('D').getX() <= endX && startY <= obj.getLocation('D').getY() && obj.getLocation('D').getY() <= endY) {
//			System.out.println("4");
			return 1;
		}

		return 0;
	}
	
	public void addLife() {
		life++;
	}
	
	public void minusLife() {
		life--;
	}
	
	public void setLife(int life) {
		this.life = life;
	}
	
	public int getLife() {
		return life;
	}
	
}
