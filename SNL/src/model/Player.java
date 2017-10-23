package model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import customInterface.Attackable;
import customInterface.Direction;
import customInterface.JumpListener;
import gui.SNL;
import thread.JumpThread;

public class Player extends GameObj implements Direction, Attackable {

	private boolean isSelecter;
	private int dx, dy;
	private double imageIdx;
	private boolean isRight;
	private boolean isAttacking;
	// private ImageIcon jumpRight[];
	int jumpIdx = 0;
	private int charType;

	private JumpListener jumpListener;

	public Player(int x, int y, ImageIcon image, int charType) {
		super(x, y-image.getIconHeight(), image);
		this.charType = charType;
		isSelecter = false;
		dx = 5;
		dy = 0;
		isRight = true;
		imageIdx = 1; // 谅快 捞悼
		isAttacking=false;

	}

	public Player(int x, int y, ImageIcon image, int charType, int dx, int dy, boolean isSelecter) {
		this(x, y+image.getIconHeight(), image, charType);
		this.dx = dx;
		this.dy = dy;
		this.isSelecter = isSelecter;
	}

	public void move(int direction) {
		String imageFile;
		switch (direction) {
		case LEFT:
			isRight = false;
			imageFile = "../images/left_" + String.valueOf(charType) + "_" + String.valueOf((int) imageIdx % 3 + 1)
					+ ".png";
			System.out.println(imageFile);
			setImage(new ImageIcon(SNL.class.getResource(imageFile)));
			imageIdx += 0.3;
			setPosX(getPosX() - dx);
			if (getPosX() < 0)
				setPosX(0);
			break;
		case RIGHT:
			isRight = true;
			imageFile = "../images/right_" + String.valueOf(charType) + "_" + String.valueOf((int) imageIdx % 3 + 1)
					+ ".png";
			ImageIcon icon = new ImageIcon(SNL.class.getResource(imageFile));
			setImage(icon);
			imageIdx += 0.3;
			setPosX(getPosX() + dx);
			if (getPosX() > (SNL.SCREEN_WIDTH - icon.getIconWidth()))
				setPosX(SNL.SCREEN_WIDTH - icon.getIconWidth());
			break;

		case UP:
			if (isSelecter)
				setPosY(getPosY() - dy);
			break;
		case DOWN:
			if (isSelecter)
				setPosY(getPosY() + dy);
			 else{
//			 TODO : ENTER THE DOOR
			
			 }
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
		isAttacking=true;
		System.out.println("attack");
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
		isAttacking=false;
//		if (!isRight)
//			setPosX(getPosX() + 60);

	}

	public int isCrush(GameObj obj) {
		int startX = getA()[0];
		int endX = getD()[0];

		int startY = getA()[1];
		int endY = getB()[1];

		if(isAttacking) {
			return -1;
		}
		if (startX <= obj.getA()[0] && obj.getA()[0] <= endX && startY <= obj.getA()[1] && obj.getA()[1] <= endY) {
			System.out.println("1");
			return 1;
		}
		
		if (startX <= obj.getB()[0] && obj.getB()[0] <= endX && startY <= obj.getB()[1] && obj.getB()[1] <= endY) {
			System.out.println("2");
			return 1;
		}
		
		if (startX <= obj.getC()[0] && obj.getC()[0] <= endX && startY <= obj.getC()[1] && obj.getC()[1] <= endY) {
			System.out.println("3");
			return 1;
		}
		if (startX <= obj.getD()[0] && obj.getD()[0] <= endX && startY <= obj.getD()[1] && obj.getD()[1] <= endY) {
			System.out.println("4");
			return 1;
		}

		
		
		return 0;
	}

}
