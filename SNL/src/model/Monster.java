package model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

import adapter.MapReader;
import gui.SNL;

public class Monster extends GameObj {
	
	private MapReader mMapReader;
	private Rectangle2D mMonsterArea;
	private int dx, dy;

	private int jumpIdx = 0;
	private double imageIdx = 0;

	private boolean isRight;
	private boolean isJumpable;

	public Monster(int x, int y, ImageIcon image, MapReader mapReader) {
		super(x, y, image);
		int num = (int) (Math.random() * 10);
		dy = 0;
		dx = 3;
		if (num % 2 == 0) {
			isRight = true;			
		} else {
			isRight = false;
		}
		this.mMapReader = mapReader;
		mMonsterArea = new Rectangle2D.Double(getPosX(), getPosY(), getWidth(), getHeight());
		isJumpable = true;
	}


	public void update() {
		String imageFile;
		mMonsterArea = new Rectangle2D.Double(getPosX(), getPosY(), getWidth(), getHeight());

		if (!isJumping()) {
			Rectangle2D a = new Rectangle2D.Double(getPosX(), getPosY() + 5, getWidth(), getHeight());

			if ((!mMapReader.isCrush(a))) {
				isJumpable = false;
				setPosY(getPosY() + 8
						);
//				System.out.println(getPosX());
			} else {
				isJumpable = true;
			}

		}
		if (isRight) {
			imageFile = "images/right_monster_" + String.valueOf((int) imageIdx % 3 + 1) + ".png";
		

		} else {
			imageFile = "images/left_monster_" + String.valueOf((int) imageIdx % 3 + 1) + ".png";
		}
		setImage(new ImageIcon(SNL.class.getClassLoader().getResource(imageFile)));

		imageIdx += 0.3;

		if (getPosX() < mMapReader.getBlockWidth()) {			
			isRight = true;
		}


		if (getPosX() > (SNL.SCREEN_WIDTH - getWidth() - mMapReader.getBlockWidth())) {
			
			isRight = false;
		}

		if (!mMapReader.isCrush(mMonsterArea)) {
			if(isRight)
				setPosX(getPosX() + dx);
			else
				setPosX(getPosX() - dx);
		}
		else {

			if(isRight)
				setPosX(getPosX() - dx);
			else
				setPosX(getPosX() + dx);
			changeDirection();
		}
		
	
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(getImage(), getPosX(), getPosY(), null);
		// System.out.println("(" + getPosX() + "," + getPosY() + ")");
	}

	public void changeDirection() {
//		dx *= -1;
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

	public boolean isJumpable() {
		return isJumpable;
	}

}
