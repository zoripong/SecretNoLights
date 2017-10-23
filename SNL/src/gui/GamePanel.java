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
	private FrameManager fm;
	private int charType;

	private Image screenImage;
	private Image background;

	Timer t = new Timer(10, this); // 1�ʸ��� actionPerformed ����
	Player p;
	ArrayList<Monster> monsters;
	ArrayList<MonsterThread> monsterThreads;
	
	private Music gameMusic;
	private MapReader mMapReader;
	private boolean isMapDraw;
	
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
		ImageIcon character = new ImageIcon(
				SNL.class.getResource("../images/front_" + String.valueOf(charType) + ".png"));
		p = new Player(50, 680, character, charType);

		background = new ImageIcon(SNL.class.getResource("../images/game_background.png")).getImage();

		gameMusic = new Music("gameMusic.mp3", true);
		gameMusic.start();

		// Monster
		ImageIcon monster = new ImageIcon(SNL.class.getResource("../images/front_3.png"));
		monsters = new ArrayList<Monster>();
		monsterThreads = new ArrayList<MonsterThread>();

		monsters.add(new Monster(200, 680, monster));

		for (int i = 0; i < monsters.size(); i++) {
			monsterThreads.add(new MonsterThread(monsters.get(i), new AutoMovingListener() {
				@Override
				public void repaintable() {
					repaint();
				}
			}));
			monsterThreads.get(i).start();
		}
		
		// Map read
		mMapReader = new MapReader(1);
 		isMapDraw = true;
	}

	public void actionPerformed(ActionEvent ae) {
		// p.move(0);
		// System.out.println("actionPerformed");

		repaint();
	}


	public void paint(Graphics g) {
		super.paint(g);

		for (int i = 0; i < monsters.size(); i++) {
			switch (p.isCrush(monsters.get(i))) {
			case -1:
				// ���� ���
				//TODO : �ð� ���� �� �������,,
//				System.out.println("���� ���");
				monsterThreads.get(i).onStop();
				monsterThreads.remove(i);
				monsters.remove(i);
				break;
			case 0:
				//�ƹ��͵� �ƴ� ��Ȳ
				break;
			case 1:
				// ���Ϳ� �¾Ƽ� ����
				gameMusic.close();
				fm.changePanel("HallPanel");
				return;
			}
		}
		// set the background
		screenImage = createImage(SNL.SCREEN_WIDTH, SNL.SCREEN_HEIGHT);
		Graphics screenGraphic = screenImage.getGraphics();
		screenDraw(screenGraphic);
		
		g.drawImage(screenImage, 0, 0, null);

		for (int i = 0; i < monsters.size(); i++)
			monsters.get(i).draw(g);

		p.draw(g);
		
		// set the Map
		if(isMapDraw) {
			mMapReader.setStage(g);
//			isMapDraw = false;
		}

	}

	public void screenDraw(Graphics g) {
		g.drawImage(background, 0, 0, null);
		paintComponents(g);
		this.repaint();
	}

	public void jump() {
		new JumpThread(this).start();
	}

	@Override
	public void jumpTimeArrived(int jumpIdx, int jumpy) {
		// TODO Auto-generated method stub
		if (p.isRight())
			p.addX(5);
		else
			p.addX(-5);
		p.addY(jumpy);
		ImageIcon icon;
		if (p.isRight())
			icon = new ImageIcon(SNL.class.getResource("../images/jump_right_" + String.valueOf(charType) + ".png"));
		else
			icon = new ImageIcon(SNL.class.getResource("../images/jump_left_" + String.valueOf(charType) + ".png"));

		p.setImage(icon);
		if (p.getPosX() < 0)
			p.setPosX(0);
		if (p.getPosX() > (SNL.SCREEN_WIDTH - icon.getIconWidth()))
			p.setPosX(SNL.SCREEN_WIDTH - icon.getIconWidth());

		p.setJumpIdx(jumpIdx);

	}

	@Override
	public void jumpTimeEnded() {
		p.setImage(new ImageIcon(SNL.class.getResource("../images/front_" + String.valueOf(charType) + ".png")));
		p.setJumpIdx(0);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			System.out.println("AttackBefore" + p.getPosX());
			p.attackEnd();
			System.out.println("AttackAfter" + p.getPosX());
		}
		p.setImage(new ImageIcon(SNL.class.getResource("../images/front_" + String.valueOf(charType) + ".png")));
	}

	@Override
	public void keyPressed(KeyEvent e) {
//		System.out.println(e.getKeyCode());
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			p.move(LEFT);
//			System.out.println("left");
			break;
		case KeyEvent.VK_RIGHT:
			p.move(RIGHT);
//			System.out.println("right");
			break;
		case KeyEvent.VK_UP:
			if (p.isJumping())
				return;
			jump();
			break;
		case KeyEvent.VK_DOWN:
			p.move(DOWN);
			break;
		case KeyEvent.VK_SPACE:
			if (p.isAttack())
				return;
			p.attack();
			break;
		}
		repaint();

	}

}