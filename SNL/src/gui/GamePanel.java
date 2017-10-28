package gui;

//http://lacti.me/2011/11/06/flight-game-with-java-3/
/*
// * 딜레이	
 *  private void playerShot() {
        if (shotTick < System.currentTimeMillis()) {
            shotTick = System.currentTimeMillis() + 200;
            bullets.add(new Bullet(shipPos.x, shipPos.y - (ship.getHeight(this) / 2 + 8), -1));
        }
    }
 * 
 */
import java.awt.Graphics;
import java.awt.Image;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.omg.CosNaming.IstringHelper;

import adapter.MapReader;
import adapter.Music;
import customInterface.AutoMovingListener;
import customInterface.Direction;
import customInterface.JumpListener;
import model.Location;
import model.Monster;
import model.Player;
import thread.JumpThread;
import thread.MonsterThread;

public class GamePanel extends JPanel implements ActionListener, KeyListener, Direction, JumpListener {
	private final int BLOCK_WIDTH = 43;
	private final int BLOCK_HEIGHT = 40;

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

	private boolean isCrush;

	ArrayList<ArrayList<Long>> startCrushes;
	ArrayList<Long> startCrush;
	ArrayList<Integer> crushCount;

	private int keyCount;

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

		gameMusic = new Music("gameMusic.mp3", true);
		gameMusic.start();

		// Map read
		mMapReader = new MapReader(1);
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
		p = new Player(46, SNL.SCREEN_HEIGHT - 80, character, charType, mMapReader);
		player = new Rectangle2D.Double(p.getPosX(), p.getPosY(), p.getWidth(), p.getHeight());

		isOpenDoor = 0;
		isCrush = false;

		keyCount = 0;


		startCrush = new ArrayList<>();
		startCrushes = new ArrayList<>();
		crushCount = new ArrayList<>();
		for (int i = 0; i < monsters.size(); i++) {
			startCrush.add((long) 0);
			startCrushes.add(new ArrayList<>());
			crushCount.add(0);
		}
	}

	public void actionPerformed(ActionEvent ae) {
		repaint();
	}

	public void paint(Graphics g) {
		super.paint(g);
		player = new Rectangle2D.Double(p.getPosX(), p.getPosY(), p.getWidth(), p.getHeight());
		// System.out.println(p.getPosY());
		if (!p.isJumping()) {

			ImageIcon front = new ImageIcon(
					SNL.class.getResource("../images/front_" + String.valueOf(charType) + ".png"));
			Rectangle2D a = new Rectangle2D.Double(p.getPosX(), p.getPosY() + 3, front.getIconWidth(),
					front.getIconHeight());

			if ((!mMapReader.isCrush(a))) {
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
			nextStage();
		}

		Rectangle2D mosPlayer = new Rectangle2D.Double(p.getPosX(), p.getPosY(), p.getWidth(), p.getHeight());

		for (int i = 0; i < monsters.size(); i++) {
			Rectangle2D monster = new Rectangle2D.Double(monsters.get(i).getPosX(), monsters.get(i).getPosY(),
					monsters.get(i).getWidth(), monsters.get(i).getHeight());
			// TODO : 공격공격!!
			if (mosPlayer.intersects(monster)) {
				if (crushCount.get(i) == 0) {
					startCrush.set(i, System.currentTimeMillis());
				}

				crushCount.set(i, crushCount.get(i) + 1);
				// 공격중
				if (isCrush == false) {
					isCrush = true;
					if ((System.currentTimeMillis() - startCrush.get(i)) >= 250) {
						startCrush.set(i, startCrush.get(i) + 250);
						if (p.isAttack()) {
							if (p.isRight()) {
								if (p.getPosX() < monster.getX()) {

									crushMonster(i);
								} else {
									// 플레이어 사망
									if (crushPlayer())
										return;
								}
							} else {
								if (p.getPosX() > monster.getX()) {
									// 몬스터 사망
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

				}

			} else {
				isCrush = false;
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
		if (isOpenDoor == 1)
			isOpenDoor++;

		// draw the Map
		if (isMapDraw) {
			mMapReader.drawStage(g);
			// isMapDraw = false;
		}

		// draw the player
		p.draw(g);

		// draw the monsters
		for (int i = 0; i < monsters.size(); i++)
			monsters.get(i).draw(g);

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

		if (isJumpingMove) {
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
		keyCount = 0;
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

	private void nextStage() {
		removeMonsters();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		isOpenDoor = 0;

		mMapReader.nextStage();
		setMonsters();

		for (int i = 0; i < monsters.size(); i++) {
			startCrush.add((long) 0);
			startCrushes.add(new ArrayList<>());
			crushCount.add(0);
		}
	}

	private boolean crushPlayer() {
		if (p.getLife() == 0) {
			gameMusic.close();
			fm.changePanel("HallPanel");
			return true;
		} else {
			// System.out.println("목숨 :" + p.getLife());
			p.minusLife();
			return false;
		}

	}

	private void crushMonster(int i) {
		monsterThreads.get(i).onStop();
		monsterThreads.remove(i);
		monsters.remove(i);
	}

}
