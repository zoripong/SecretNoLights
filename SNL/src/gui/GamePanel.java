package gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

	private Music gameMusic;
	private MapReader mMapReader;
	private boolean isMapDraw;
	private int isOpenDoor;

	private boolean isJumpingMove;
	private JumpThread jumpThread;

	ArrayList<Monster> monsters;
	ArrayList<MonsterThread> monsterThreads;

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

		// Monster
		monsterThreads = new ArrayList<MonsterThread>();
		setMonsters();

		// init player
		ImageIcon character = new ImageIcon(
				SNL.class.getResource("../images/front_" + String.valueOf(charType) + ".png"));
		p = new Player(40, SNL.SCREEN_HEIGHT - 80, character, charType, mMapReader);

		isOpenDoor = 0;
	}

	public void actionPerformed(ActionEvent ae) {
		repaint();
	}

	public void paint(Graphics g) {
		// System.out.println(p.isJumping());
		super.paint(g);
		if (isOpenDoor == 2) {
			removeMonsters();

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			isOpenDoor = 0;

			mMapReader.nextStage(); // 좌표 갱신을 포함하고 있음 [setStage()]
			setMonsters();

		}

		for (int i = 0; i < monsters.size(); i++) {
			switch (p.isCrush(monsters.get(i))) {
			case -1:
				// 몬스터 사망
				// TODO : 시간 지속 시 사망으로,,
				// System.out.println("몬스터 사망");
				monsterThreads.get(i).onStop();
				monsterThreads.remove(i);
				monsters.remove(i);
				break;
			case 0:
				// 아무것도 아닌 상황
				break;
			case 1:
				// todo : 몇 초 이상 지속시 감소
				// 몬스터에 맞아서 죽음
				if (p.getLife() == 0) {
					gameMusic.close();
					fm.changePanel("HallPanel");
				} else {
					p.minusLife();
				}
				// System.out.println("남은 목숨 : " + p.getLife());

				return;
			}
		}
		// set the background
		screenImage = createImage(SNL.SCREEN_WIDTH, SNL.SCREEN_HEIGHT);
		Graphics screenGraphic = screenImage.getGraphics();
		screenDraw(screenGraphic);

		g.drawImage(screenImage, 0, 0, null);

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
		jumpThread = new JumpThread(this);
		jumpThread.start();
	}

	@Override
	public void jumpTimeArrived(int jumpIdx, int jumpy, boolean isDown) {
		// TODO
		// 벽돌과 부딪혔을 때 처리하는 부분
		if (isDown) { // 아래에 벽과 겹칠 때 . . .
			System.out.println(mMapReader.isUnderBlock(p.getLocation('B').getX(), p.getLocation('C').getY()));
			if (mMapReader.isUnderBlock(p.getLocation('C').getX(), p.getLocation('C').getY())
					|| mMapReader.isUnderBlock(p.getLocation('B').getX(), p.getLocation('B').getY())) {
				// try {
				// Thread.sleep(1000);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				System.out.println("점프 끝..!");
				jumpThread.stopJump();

			}
		}

		// 블록 좌우 충돌

		if (isJumpingMove) {
			if (p.isRight()) {
				int cx = p.getLocation('C').getX();
				int cy = p.getLocation('C').getY();
				int dx = p.getLocation('D').getX();
				int dy = p.getLocation('D').getY();

				if (!(mMapReader.isBlock(cx, cy) || mMapReader.isBlock(dx, dy))) {
					System.out.println((cx / 45) + "/" + (cy / 40) + "/" + (dx / 45) + "/" + (dy / 40));
					System.out.println("충돌중");
					// setPosX(getPosX() + dx);
					// 벽돌이 양옆에 벽돌이 있는지 check
					p.addX(p.getDx());
				}
			} else {
				if (!(mMapReader.isBlock(p.getLocation('A').getX(), p.getLocation('A').getY())
						|| mMapReader.isBlock(p.getLocation('B').getX(), p.getLocation('B').getY()))) {
					// System.out.println("충돌중");
					// setPosX(getPosX() - dx);
					p.addX(p.getDx() * -1);
				}
			}
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
			System.out.println(p.getPosY());
			p.setPosY(p.getPosY() + p.getPosY() % 40 );
			System.out.println(p.getPosY());
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			// System.out.println("AttackBefore" + p.getPosX());
			p.attackEnd();
			// System.out.println("AttackAfter" + p.getPosX());
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
			jump();
			break;
		case KeyEvent.VK_DOWN:
			p.move(DOWN);
			// System.out.println(p.getA()[0] + "/" + mMapReader.getDoorMid() + "/" +
			// p.getD()[0]);
			if (!p.isJumping()) {
				if (p.getLocation('A').getX() <= mMapReader.getDoorMid()
						&& mMapReader.getDoorMid() <= p.getLocation('D').getX()) {
					// System.out.println("열려라 참깨");
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
			monsterThreads.add(new MonsterThread(monster, new AutoMovingListener() {
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
			monsterThreads.get(i).onStop();
			monsterThreads.remove(i);
			monsters.remove(i);
		}
	}

}
