package model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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
	private int attackCount;
	private JumpListener jumpListener;

	private String imageName;

	private ImageIcon currentImage;
	private ArrayList<Location> attackLocation;

	private ArrayList<ImageIcon> scoreNumbers;
	private ImageIcon scoreImage;

	private ImageIcon lifeImage;
	
	private boolean isJumpingAttack;

	public Player(int x, int y, ImageIcon image, int charType, MapReader mapReader) {
		super(x, y - image.getIconHeight(), image);
		this.charType = charType;
		isSelecter = false;
		dx = 9;
		dy = 0;
		isRight = true;
		imageIdx = 1; // 좌우 이동
		isAttacking = false;
		isJumpingAttack = false;
		mMapReader = mapReader;
		life = 5;
		currentImage = image;

		// 캐릭터별 이미지차
		attackLocation = new ArrayList<>();
		attackLocation.add(new Location(86, 21));
		attackLocation.add(new Location(101, 20));
		attackLocation.add(new Location(76, 0));
		attackLocation.add(new Location(80, 0));

		attackCount = 0;
		score = 0;

		// life
		lifeImage = new ImageIcon(SNL.class.getClassLoader().getResource("images/life.png"));

		// score
		scoreImage = new ImageIcon(SNL.class.getClassLoader().getResource("images/score.png"));

		scoreNumbers = new ArrayList<>();
		for (int i = 0; i < 10; i++)
			scoreNumbers.add(new ImageIcon(SNL.class.getClassLoader().getResource("images/score_" + String.valueOf(i) + ".png")));

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
			imageName = "images/left_" + String.valueOf(charType) + "_" + String.valueOf((int) imageIdx % 3 + 1)
					+ ".png";
			// System.out.println(imageFile);
			
			currentImage = new ImageIcon(SNL.class.getClassLoader().getResource(imageName));
			setImage(currentImage);

			imageIdx += 0.3;

			if (getPosX() < mMapReader.getBlockWidth())
				setPosX(mMapReader.getBlockWidth());


			
			if(isJumping()) {
				ImageIcon jump = new ImageIcon(SNL.class.getClassLoader().getResource("images/jump_left_"+String.valueOf(charType)+".png"));
				Rectangle2D a = new Rectangle2D.Double(getPosX()-getDx(), getPosY(), jump.getIconWidth(), jump.getIconHeight());
				System.out.println("왜지?");
				if (!mMapReader.isCrush(a))
					setPosX(getPosX());
				
			}else {
				if (!mMapReader.isCrush(player) && getPosX()>45) {
					if(isAttacking)
						return;
					else
						setPosX(getPosX() - dx);
				}
			}

			break;
		case RIGHT:

			imageName = "images/right_" + String.valueOf(charType) + "_" + String.valueOf((int) imageIdx % 3 + 1)
					+ ".png";
			currentImage = new ImageIcon(SNL.class.getClassLoader().getResource(imageName));
			setImage(currentImage);

			imageIdx += 0.3;

			if (getPosX() > (SNL.SCREEN_WIDTH - getWidth() - mMapReader.getBlockWidth()))
				setPosX(SNL.SCREEN_WIDTH - getWidth() - mMapReader.getBlockWidth());

			if (!mMapReader.isCrush(player))
				setPosX(getPosX() + dx);
//
//			if(isJumping()) {
//				Rectangle2D a = new Rectangle2D.Double(getPosX()+getDx(), getPosY(), getWidth(), getHeight());
//				
//				if (!mMapReader.isCrush(a))
//					setPosX(getPosX() + dx);
//				
//			}else {
//				if (!mMapReader.isCrush(player))
//					setPosX(getPosX() + dx);
//			}
			
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

	public void drawLife(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		int x = SNL.SCREEN_WIDTH - ((lifeImage.getIconWidth() + 10) * 5) - 20;
		int y = 10;

		for (int i = 0; i < life; i++) {
			g2d.drawImage(lifeImage.getImage(), x, y, null);
			x += lifeImage.getIconWidth() + 10;
		}
	}

	public void drawScore(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		int x = 10;
		int y = 10;

		g2d.drawImage(scoreImage.getImage(), x, y, null);
		x += scoreImage.getIconWidth() + 10;

		char charOfScore[] = String.valueOf(score).toCharArray();

		for (int i = 0; i < charOfScore.length; i++) {
			int number = Integer.parseInt(String.valueOf(charOfScore[i]));
			g2d.drawImage(scoreNumbers.get(number).getImage(), x, y, null);
			x += scoreNumbers.get(number).getIconWidth() + 10;
		}

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
		if(isJumping()) {
			isJumpingAttack = true;
			return;
		}
		
		isAttacking = true;
		
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(SNL.class.getClassLoader().getResource("music/attack_effect.wav").toURI()));

			Clip clip = AudioSystem.getClip();
			clip.stop();
			clip.open(ais);
			clip.start();
		} catch (Exception ex) {
		}
		
		if (isRight) {
			imageName = "images/attack_right_" + String.valueOf(charType) + ".png";
			currentImage = new ImageIcon(SNL.class.getClassLoader().getResource(imageName));
			setImage(currentImage);
		} else {
			// TODO LEFT ATTACK
			imageName = "images/attack_left_" + String.valueOf(charType) + ".png";
			currentImage = new ImageIcon(SNL.class.getClassLoader().getResource(imageName));

			setImage(currentImage);

		}
		if (attackCount == 0) {
			if (!isRight())
				setPosX(getPosX() - getAttackPosX(charType));
			setPosY(getPosY() - getAttackPosY(charType));
		}
		attackCount++;
	}

	public void attackEnd() {
		System.out.println(isJumpingAttack);
		if(isJumpingAttack) {
			isJumpingAttack = false;
			return;
		}
		
		isAttacking = false;
		attackCount = 0;
		if (isRight) {
			setPosY(getPosY() + getAttackPosY(charType));
		} else {
			setPosX(getPosX() + getAttackPosX(charType));
			setPosY(getPosY() + getAttackPosY(charType));
		}
	}

	public void addLife() {
		if(life >= 5)
			return;
		life++;
	}

	public void minusLife() {
		life--;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getLife() {
		System.out.println("남은 목숨" + life);
		return life;
	}

	public Location currentLocation() {
		ImageIcon blockImageIcon = new ImageIcon(SNL.class.getClassLoader().getResource("images/block.png"));

		int x = (getPosX() + (currentImage.getIconWidth() / 2)) / blockImageIcon.getIconWidth();
		int y = (getPosY() + (currentImage.getIconHeight() * 3 / 2)) / blockImageIcon.getIconHeight();
		return new Location(x, y);
	}

	public String toString() {
		ImageIcon blockImageIcon = new ImageIcon(SNL.class.getClassLoader().getResource("images/block.png"));

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

	public void increaseScore(int score) {
		this.score += score;
		System.out.println("현재 스코어 " + this.score);
	}

	public int getScore() {
		return score;
	}
}
