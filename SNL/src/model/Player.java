package model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;

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

	// private ImageIcon jumpRight[];
	int jumpIdx = 0;
	private int charType;

	private JumpListener jumpListener;

	public Player(int x, int y, Image image, int charType) {
		super(x, y, image);
		this.charType = charType;
		isSelecter = false;
		dx = 5;
		dy = 0;
		isRight = true;
		imageIdx = 1; // 좌우 이동
		// jumpRight = new ImageIcon[5];
		// for (int i = 0; i < jumpRight.length; ++i) {
		// String path = "../images/dino_jump_right.png";
		// //// this.debuggable.debug(path);
		// this.jumpRight[i] = new ImageIcon(path);
		// } // 배열에 이동하는 이미지 추가

	}

	public Player(int x, int y, Image image, int charType, int dx, int dy, boolean isSelecter) {
		this(x, y, image, charType);
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
			setImage(new ImageIcon(SNL.class.getResource(imageFile)).getImage());
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
			setImage(icon.getImage());
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
			// else{
			// TODO : ENTER THE DOOR
			//
			// }
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
		//TODO : attack 중복처리  and Thread
		return false;
	}

	public void addX(int add) {
		setPosX(getPosX() + add);
	}

	public void addY(int add) {
		setPosY(getPosY() + add);
	}

	@Override
	public void attack() {
		// TODO 충돌처리
		System.out.println("attack");
		if (isRight)
			setImage(new ImageIcon(SNL.class.getResource("../images/attack_right_" + String.valueOf(charType) + ".png"))
					.getImage());
		else {
			// TODO LEFT ATTACK
			setPosX(getPosX() - 60);
			setImage(new ImageIcon(SNL.class.getResource("../images/attack_left_" + String.valueOf(charType) + ".png"))
					.getImage());
		}
	}

	public void attackEnd() {
		if (!isRight)
			setPosX(getPosX() + 60);

	}

}
