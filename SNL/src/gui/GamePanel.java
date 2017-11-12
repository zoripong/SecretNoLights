
package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.omg.CosNaming.IstringHelper;

import adapter.MapReader;
import adapter.Music;
import customInterface.AutoMovingListener;
import customInterface.Direction;
import customInterface.JumpListener;
import model.GameObj;
import model.Location;
import model.Monster;
import model.Player;
import model.Record;
import thread.JumpThread;
import thread.MonsterThread;

public class GamePanel extends JPanel implements ActionListener, KeyListener, Direction, JumpListener {
	private final int BLOCK_WIDTH = 43;
	private final int BLOCK_HEIGHT = 40;

	private final int MAX_STAGE = 10;

	private FrameManager fm;
	private int charType;

	private Image screenImage;
	private Image background;

	Timer t = new Timer(10, this); // 1초마다 actionPerformed 실행
	Player p;
	Rectangle2D player;

	private Music gameMusic;
	private MapReader mMapReader;
	private boolean isMapDraw;
	private int isOpenDoor;

	private boolean isJumpingMove;
	private JumpThread jumpThread;
	private boolean isJumpable;

	ArrayList<Monster> monsters;
	ArrayList<MonsterThread> monsterThreads;

	ArrayList<ArrayList<Long>> startCrushes;
	ArrayList<Long> startCrush;
	ArrayList<Integer> crushCount;

	ArrayList<GameObj> items;
	ArrayList<ImageIcon> itemImages;

	private long startStageTime;

	private ImageIcon nextStageImage;
	private ImageIcon darknessImage;

	private boolean setDark;
	private long darkStart;
	
	private boolean setLight;
	private long lightStart;

	private boolean isSpeedUp;
	private long speedUpStart;
	
	public GamePanel(FrameManager fm, int charType) {
		this.fm = fm;
		this.charType = charType;

		init();

		setFocusable(true);
		this.requestFocus();
		addKeyListener(this);
		t.start();

	}

	public void init() {

		background = new ImageIcon(SNL.class.getResource("../images/game_background.png")).getImage();
		darknessImage = new ImageIcon(SNL.class.getResource("../images/darkness_2.png"));

		gameMusic = new Music("gameMusic.mp3", true);
		gameMusic.start();

		// Map read
		mMapReader = new MapReader(0);
		mMapReader.setStage();
		isMapDraw = true;
		isJumpingMove = false;
		isJumpable = true;

		// Monster
		monsterThreads = new ArrayList<MonsterThread>();
		setMonsters();

		// init player
		ImageIcon character = new ImageIcon(
				SNL.class.getResource("../images/front_" + String.valueOf(charType) + ".png"));
		p = new Player(SNL.SCREEN_WIDTH - 200, SNL.SCREEN_HEIGHT - 80, character, charType, mMapReader);
		player = new Rectangle2D.Double(p.getPosX(), p.getPosY(), p.getWidth(), p.getHeight());

		isOpenDoor = 0;

		startCrush = new ArrayList<>();
		startCrushes = new ArrayList<>();
		crushCount = new ArrayList<>();
		items = new ArrayList<>();
		itemImages = new ArrayList<>();

		itemImages.add(new ImageIcon(SNL.class.getResource("../images/item_eyesight.png")));

		itemImages.add(new ImageIcon(SNL.class.getResource("../images/item_hp.png")));

		itemImages.add(new ImageIcon(SNL.class.getResource("../images/item_speed.png")));

		for (int i = 0; i < monsters.size(); i++) {
			startCrush.add((long) 0);
			startCrushes.add(new ArrayList<>());
			crushCount.add(0);
		}

		startStageTime = System.currentTimeMillis();
		nextStageImage = new ImageIcon(SNL.class.getResource("../images/next_stage.png"));

		darkStart = -1;
		setDark = false;
		
		lightStart = -1;
		setLight = false;
		
		speedUpStart = -1;
		isSpeedUp = false;
	}

	public void actionPerformed(ActionEvent ae) {
		repaint();
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		player = new Rectangle2D.Double(p.getPosX(), p.getPosY(), p.getWidth(), p.getHeight());

		if(isSpeedUp) {
			if(speedUpStart != -1) {
				if(System.currentTimeMillis() - speedUpStart > 3000) {
					isSpeedUp = false;
					p.setDx(p.getDx()/2);
				}
			}
		}
		
		// System.out.println(p.getPosX() + ", " + p.getPosY());
		if (!p.isJumping()) {

			ImageIcon front = new ImageIcon(
					SNL.class.getResource("../images/front_" + String.valueOf(charType) + ".png"));

			Rectangle2D a = new Rectangle2D.Double(p.getPosX(), p.getPosY() + 3, front.getIconWidth(),
					front.getIconHeight());

			if ((!mMapReader.isCrush(a)) && !p.isAttack()) {
				isJumpable = false;
				p.setPosY(p.getPosY() + 2);
				if (p.isRight()) {
					p.setImage(new ImageIcon(
							SNL.class.getResource("../images/jump_right_" + String.valueOf(charType) + ".png")));
				} else {
					p.setImage(new ImageIcon(
							SNL.class.getResource("../images/jump_left_" + String.valueOf(charType) + ".png")));
				}
			} else {
				isJumpable = true;
				if (!isJumpingMove && !p.isAttack())
					p.setImage(new ImageIcon(
							SNL.class.getResource("../images/front_" + String.valueOf(charType) + ".png")));
			}

		}

		if (isOpenDoor == 2) {
			if (mMapReader.getStage() >= MAX_STAGE) {
				mMapReader = null;
				gameMusic.close();
				fm.setRecord(new Record(String.valueOf(p.getScore())));
				fm.changePanel("RegisterPanel");
				return;
			} else {
				nextStage(g);
			}

		}

		Rectangle2D mosPlayer = new Rectangle2D.Double(p.getPosX(), p.getPosY(), p.getWidth(), p.getHeight());
		if (!p.isAttack()) {
			for (int i = 0; i < items.size(); i++) {
				Rectangle2D item = new Rectangle2D.Double(items.get(i).getPosX(), items.get(i).getPosY(),
						items.get(i).getWidth(), items.get(i).getHeight());
				if (mosPlayer.intersects(item)) {
					try {
						AudioInputStream ais = AudioSystem
								.getAudioInputStream(new File("./src/music/item.wav"));
						Clip clip = AudioSystem.getClip();
						clip.stop();
						clip.open(ais);
						clip.start();
					}catch(Exception e) {
						
					}
					switch (items.get(i).getItemType()) {
					case 0:
						itemEyesight();
						System.out.println("시야 확보");
						break;
					case 1:
						p.addLife();
						System.out.println("체력 충전");
						break;
					case 2:
						itemSpeedUp();
						System.out.println("속도 업");
						break;
					}
					items.remove(i);
					break;
				}

			}
		}

		for (int i = 0; i < monsters.size(); i++) {
			Rectangle2D monster = new Rectangle2D.Double(monsters.get(i).getPosX(), monsters.get(i).getPosY(),
					monsters.get(i).getWidth(), monsters.get(i).getHeight());

			if (mosPlayer.intersects(monster)) {
				if (crushCount.get(i) == 0) {
					startCrush.set(i, System.currentTimeMillis());
				}

				crushCount.set(i, crushCount.get(i) + 1);
				// 공격중
				if ((System.currentTimeMillis() - startCrush.get(i)) >= 230) {
					startCrush.set(i, startCrush.get(i) + 230);
					if (p.isAttack()) {
						if (p.isRight()) {
							if (p.getPosX() < monster.getX()) {
								// 몬스터 사망
								try {
									AudioInputStream ais = AudioSystem
											.getAudioInputStream(new File("./src/music/crush_monster.wav"));
									Clip clip = AudioSystem.getClip();
									clip.stop();
									clip.open(ais);
									clip.start();
								} catch (Exception ex) {
								}
								createItem(monsters.get(i));
								p.increaseScore(300);
								crushMonster(i);

							} else {
								// 플레이어 사망
								if (crushPlayer())
									return;
							}
						} else {
							if (p.getPosX() > monster.getX()) {

								// 몬스터 사망
								try {
									AudioInputStream ais = AudioSystem
											.getAudioInputStream(new File("./src/music/crush_monster.wav"));
									Clip clip = AudioSystem.getClip();
									clip.stop();
									clip.open(ais);
									clip.start();
								} catch (Exception ex) {
								}
								createItem(monsters.get(i));
								p.increaseScore(300);
								crushMonster(i);
							} else {
								// 플레이어 사망
								if (crushPlayer())
									return;
							}
						}
					} else {
						if (crushPlayer())
							return;
					}
				}

			} else {
				if (crushCount.get(i) != 0) {
					// System.out.println("충돌 끝,,");
					// System.out.println("startCrush " + startCrush.get(i));
					// System.out.println("startCrush Size " + startCrushes.get(i).size());
					// System.out.println("crushCount " + crushCount);
					startCrushes.get(i).clear();
					crushCount.set(i, 0);
				}

				// startCrush.set(i, (long) 0);
			}
		}

		// draw the background
		screenImage = createImage(SNL.SCREEN_WIDTH, SNL.SCREEN_HEIGHT);
		Graphics screenGraphic = screenImage.getGraphics();
		screenDraw(screenGraphic);
		g.drawImage(screenImage, 0, 0, null);

		// draw the door
		mMapReader.drawDoor(g, isOpenDoor);

		// draw the item
		drawItems(g);

		// draw the Map
		if (isMapDraw) {
			mMapReader.drawStage(g);
		}

		// draw the player
		p.draw(g);

		// draw the monsters
		for (int i = 0; i < monsters.size(); i++)
			monsters.get(i).draw(g);

		// draw the dark
		if (setDark) {
			darknessImage = new ImageIcon(SNL.class.getResource("../images/darkness_3.png"));
			if (darkStart != -1) {
				if (System.currentTimeMillis() - darkStart > 3000)
					setDark = false;
			}

		} else {
			darknessImage = new ImageIcon(SNL.class.getResource("../images/darkness_2.png"));

		}
		
		if(setLight) {
			if (lightStart != -1) {
				if (System.currentTimeMillis() - lightStart > 3000)
					setLight = false;
			}
		}else {
			g2d.drawImage(darknessImage.getImage(), (p.getPosX() + p.getWidth() / 2) - darknessImage.getIconWidth() / 2,
					(p.getPosY() + p.getHeight() / 2) - darknessImage.getIconHeight() / 2, null);
			
		}

		// draw the next stage
		if (isOpenDoor == 1) {
			g2d.drawImage(nextStageImage.getImage(), SNL.SCREEN_WIDTH / 2 - nextStageImage.getIconWidth() / 2,
					SNL.SCREEN_HEIGHT / 2 - nextStageImage.getIconHeight() / 2, null);
			isOpenDoor++;
		}

		// draw the life
		p.drawLife(g);

		// draw the score
		p.drawScore(g);

	}
	
	public void itemEyesight() {
		setLight = true;
		lightStart = System.currentTimeMillis();
	}
	
	public void itemSpeedUp() {
		isSpeedUp = true;
		speedUpStart = System.currentTimeMillis();
		p.setDx(p.getDx()*2);
	}

	public void createItem(Monster monster) {
		int type = (int) (Math.random() * 3);

		items.add(
				new GameObj(monster.getPosX() + (monster.getWidth() / 2),
						monster.getPosY() + monster.getHeight()
								- itemImages.get(type).getIconHeight(),
						itemImages.get(type), type));


	}
	public void drawItems(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		for (int i = 0; i < items.size(); i++)
			g2d.drawImage(items.get(i).getImage(), items.get(i).getPosX(), items.get(i).getPosY(), null);
	}

	public void screenDraw(Graphics g) {
		g.drawImage(background, 0, 0, null);
		paintComponents(g);
		this.repaint();

	}

	public void jump() {
		jumpThread = new JumpThread(this, mMapReader, p);
		jumpThread.start();
	}

	@Override
	public void jumpTimeArrived(int jumpIdx, int jumpy) {
		jumpThread.setMap(mMapReader, p);

		Rectangle2D a = new Rectangle2D.Double(p.getPosX() + p.getDx(), p.getPosY(), p.getWidth(), p.getHeight());
		Rectangle2D b = new Rectangle2D.Double(p.getPosX() - p.getDy(), p.getPosY(), p.getWidth(), p.getHeight());

		if (isJumpingMove && !mMapReader.isCrush(a) && !mMapReader.isCrush(b)) {
			if (p.isRight())
				p.setPosX(p.getPosX() + p.getDx());
			else
				p.setPosX(p.getPosX() - p.getDx());
		}

		if (p.getPosX() > (SNL.SCREEN_WIDTH - p.getWidth() - BLOCK_WIDTH))
			p.setPosX(SNL.SCREEN_WIDTH - p.getWidth() - BLOCK_WIDTH);

		if (p.getPosX() < BLOCK_WIDTH)
			p.setPosX(BLOCK_WIDTH);

		p.addY(jumpy);
		ImageIcon icon;
		if (p.isRight())
			icon = new ImageIcon(SNL.class.getResource("../images/jump_right_" + String.valueOf(charType) + ".png"));
		else
			icon = new ImageIcon(SNL.class.getResource("../images/jump_left_" + String.valueOf(charType) + ".png"));

		p.setImage(icon);

		p.setJumpIdx(jumpIdx);
	}

	@Override
	public void jumpTimeEnded(boolean isStop) {
		p.setImage(new ImageIcon(SNL.class.getResource("../images/front_" + String.valueOf(charType) + ".png")));

		Rectangle2D a = new Rectangle2D.Double(p.getPosX() - p.getDx(), p.getPosY(), p.getWidth(), p.getHeight());

		if (mMapReader.isCrush(a)) {
			System.out.println("아무튼,,");
			p.setPosX(p.getPosX() + p.getDx() + 3);
		}
		p.setJumpIdx(0);
		if (isStop) {
			p.setPosY(p.getPosY() + p.getPosY() % 40);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			p.attackEnd();
		}
		p.setImage(new ImageIcon(SNL.class.getResource("../images/front_" + String.valueOf(charType) + ".png")));
		isJumpingMove = false;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// System.out.println(e.getKeyCode());
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			p.setDirection(false);
			p.move(LEFT);
			isJumpingMove = true;
			// System.out.println("left");
			break;
		case KeyEvent.VK_RIGHT:
			p.setDirection(true);
			p.move(RIGHT);
			isJumpingMove = true;
			// System.out.println("right");
			break;
		case KeyEvent.VK_UP:
			if (p.isJumping())
				return;
			if (isJumpable)
				jump();
			break;
		case KeyEvent.VK_DOWN:
			p.move(DOWN);
			if (!p.isJumping()) {
				if (p.getLocation('A').getX() <= mMapReader.getDoorMid()
						&& mMapReader.getDoorMid() <= p.getLocation('D').getX()) {
					isOpenDoor = 1;
				}
			}
			break;
		case KeyEvent.VK_SPACE:
			if (p.isAttack())
				return;

			p.attack();
			break;
		}
		repaint();

	}

	private void setMonsters() {
		monsters = mMapReader.initMonsters();
		monsterThreads.clear();
		for (int i = 0; i < monsters.size(); i++) {
			Monster monster = monsters.get(i);
			monsterThreads.add(new MonsterThread(monster, mMapReader, new AutoMovingListener() {
				@Override
				public void repaintable() {
					if (monster.getPosX() < BLOCK_WIDTH) {
						monster.setPosX(BLOCK_WIDTH);
						monster.changeDirection();
					}
					if (monster.getPosX() > (SNL.SCREEN_WIDTH - monster.getWidth() - BLOCK_WIDTH)) {
						monster.setPosX(SNL.SCREEN_WIDTH - monster.getWidth() - BLOCK_WIDTH);
						monster.changeDirection();
					}
					repaint();
				}
			}));
			monsterThreads.get(i).start();
		}

	}

	private void removeMonsters() {
		for (int i = 0; i < monsters.size(); i++) {
			crushMonster(i);
		}
	}

	private void nextStage(Graphics g) {
		darknessImage = new ImageIcon(SNL.class.getResource("../images/darkness_2.png"));
		items.clear();
		removeMonsters();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		isOpenDoor = 0;

		int score = 5000 - (int) (System.currentTimeMillis() - startStageTime) / 10;
		if (score > 0)
			p.increaseScore(score);

		startStageTime = System.currentTimeMillis();

		mMapReader.nextStage();
		setMonsters();

		for (int i = 0; i < monsters.size(); i++) {
			startCrush.add((long) 0);
			startCrushes.add(new ArrayList<>());
			crushCount.add(0);
		}

	}

	private boolean crushPlayer() {
		if (p.getLife() == 1) {
			mMapReader = null;
			gameMusic.close();
			fm.setRecord(new Record(String.valueOf(p.getScore())));
			fm.changePanel("RegisterPanel");
			return true;
		} else {
			// System.out.println("목숨 :" + p.getLife());
			setDark = true;
			darkStart = System.currentTimeMillis();
			p.minusLife();
			return false;
		}

	}

	private void crushMonster(int i) {
		monsterThreads.get(i).onStop();
		monsterThreads.remove(i);
		monsters.remove(i);
		startCrush.remove(i);
		startCrushes.remove(i);
		crushCount.remove(i);
	}

}
