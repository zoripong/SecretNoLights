package model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
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
	private int score;

	private JumpListener jumpListener;

	private String imageName;

	private ImageIcon currentImage;
	private ArrayList<Location> attackLocation;

	public Player(int x, int y, ImageIcon image, int charType, MapReader mapReader) {
		super(x, y - image.getIconHeight(), image);
		this.charType = charType;
		isSelecter = false;
		dx = 9;
		dy = 0;
		isRight = true;
		imageIdx = 1; // 좌우 이동
		isAttacking = false;
		mMapReader = mapReader;
		life = 5;
		currentImage = image;
		
		// TODO attack
		attackLocation = new ArrayList<>();
		attackLocation.add(new Location(86, 21));
		attackLocation.add(new Location(101, 20));
		attackLocation.add(new Location(76, 0));
		attackLocation.add(new Location(80, 0));

	}

	public Player(int x, int y, ImageIcon image, int charType, int dx, int dy) {
		this(x, y + image.getIconHeight(), image, charType, null);
		this.dx = dx;
		this.dy = dy;
		this.isSelecter = true;
	}

	public void move(int direction) {
		Rectangle2D player = new Rectangle2D.Double(getPosX(), getPosY(), getWidth(), getHeight());

		switch (direction) {
		case LEFT:
			imageName = "../images/left_" + String.valueOf(charType) + "_" + String.valueOf((int) imageIdx % 3 + 1)
					+ ".png";
			// System.out.println(imageFile);

			currentImage = new ImageIcon(SNL.class.getResource(imageName));
			setImage(currentImage);

			imageIdx += 0.3;

			if (getPosX() < mMapReader.getBlockWidth())
				setPosX(mMapReader.getBlockWidth());

			if (!mMapReader.isCrush(player))
				setPosX(getPosX() - dx);

			break;
		case RIGHT:

			imageName = "../images/right_" + String.valueOf(charType) + "_" + String.valueOf((int) imageIdx % 3 + 1)
					+ ".png";
			currentImage = new ImageIcon(SNL.class.getResource(imageName));
			setImage(currentImage);

			imageIdx += 0.3;

			if (getPosX() > (SNL.SCREEN_WIDTH - getWidth() - mMapReader.getBlockWidth()))
				setPosX(SNL.SCREEN_WIDTH - getWidth() - mMapReader.getBlockWidth());

			if (!mMapReader.isCrush(player))
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
		// TODO : attack 중복처리 and Thread
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
		// TODO 충돌처리
		isAttacking = true;
		// System.out.println("attack");

		if (isRight) {
			imageName = "../images/attack_right_" + String.valueOf(charType) + ".png";
			currentImage = new ImageIcon(SNL.class.getResource(imageName));
			setImage(currentImage);
		} else {
			// TODO LEFT ATTACK
			imageName = "../images/attack_left_" + String.valueOf(charType) + ".png";
			currentImage = new ImageIcon(SNL.class.getResource(imageName));
			setImage(currentImage);

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
		if (startX <= obj.getLocation('A').getX() && obj.getLocation('A').getX() <= endX
				&& startY <= obj.getLocation('A').getY() && obj.getLocation('A').getY() <= endY) {
			// System.out.println("1");
			return 1;
		}

		if (startX <= obj.getLocation('B').getX() && obj.getLocation('B').getX() <= endX
				&& startY <= obj.getLocation('B').getY() && obj.getLocation('B').getY() <= endY) {
			// System.out.println("2");
			return 1;
		}

		if (startX <= obj.getLocation('C').getX() && obj.getLocation('C').getX() <= endX
				&& startY <= obj.getLocation('C').getY() && obj.getLocation('C').getY() <= endY) {
			// System.out.println("3");
			return 1;
		}
		if (startX <= obj.getLocation('D').getX() && obj.getLocation('D').getX() <= endX
				&& startY <= obj.getLocation('D').getY() && obj.getLocation('D').getY() <= endY) {
			// System.out.println("4");
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

	public Location currentLocation() {
		ImageIcon blockImageIcon = new ImageIcon(SNL.class.getResource("../images/block.png"));

		int x = (getPosX() + (currentImage.getIconWidth() / 2)) / blockImageIcon.getIconWidth();
		int y = (getPosY() + (currentImage.getIconHeight() * 3 / 2)) / blockImageIcon.getIconHeight();
		return new Location(x, y);
	}

	public String toString() {
		ImageIcon blockImageIcon = new ImageIcon(SNL.class.getResource("../images/block.png"));

		int x = (getPosX() + (currentImage.getIconWidth() / 2)) / blockImageIcon.getIconWidth();
		int y = (getPosY() + (currentImage.getIconHeight() * 3 / 2)) / blockImageIcon.getIconHeight();

		return "현재 좌표 (" + x + "," + y + ")";
	}
	
	public ImageIcon currentImage() {
		return currentImage;
	}
	
	public int getAttackPosX(int charType) {
		return attackLocation.get(charType).getX();
	}
	
	public int getAttackPosY(int charType) {
		return attackLocation.get(charType).getY();
	}
}
