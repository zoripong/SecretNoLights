package gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.omg.CosNaming.IstringHelper;

import adapter.Music;
import customInterface.Direction;
import customInterface.JumpListener;
import model.Player;
import thread.JumpThread;

public class GamePanel extends JPanel implements ActionListener, KeyListener, Direction, JumpListener {
	private FrameManager fm;
	private int charType;
	
	private Image screenImage;
	private Image background;

	Timer t = new Timer(10, this); // 1초마다 actionPerformed 실행
	Player p;

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
		Image selectImage = new ImageIcon(SNL.class.getResource("../images/front_"+String.valueOf(charType)+".png")).getImage();
		p = new Player(50, 550, selectImage, charType);
		background = new ImageIcon(SNL.class.getResource("../images/game_background.png")).getImage();

		Music gameMusic = new Music("gameMusic.mp3", true);
		gameMusic.start();
	}

	public void actionPerformed(ActionEvent ae) {
		// p.move(0);
		// System.out.println("actionPerformed");
		
		repaint();
	}

	// @Override
	// public void updateUI() {
	// // TODO Auto-generated method stub
	// super.updateUI();
	// }

	public void paint(Graphics g) {
		super.paint(g);
		screenImage = createImage(SNL.SCREEN_WIDTH, SNL.SCREEN_HEIGHT);
		Graphics screenGraphic = screenImage.getGraphics();
		screenDraw(screenGraphic);
		g.drawImage(screenImage, 0, 0, null);
		p.draw(g);

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
		if(p.isRight())
			icon = new ImageIcon(SNL.class.getResource("../images/jump_right_"+String.valueOf(charType)+".png"));
		else
			icon = new ImageIcon(SNL.class.getResource("../images/jump_left_"+String.valueOf(charType)+".png"));
			
		p.setImage(icon.getImage());
		if (p.getPosX() < 0)
			p.setPosX(0);
		if (p.getPosX() > (SNL.SCREEN_WIDTH - icon.getIconWidth()))
			p.setPosX(SNL.SCREEN_WIDTH - icon.getIconWidth());

		p.setJumpIdx(jumpIdx);

	}

	@Override
	public void jumpTimeEnded() {
		p.setImage(new ImageIcon(SNL.class.getResource("../images/front_"+String.valueOf(charType)+".png")).getImage());
		p.setJumpIdx(0);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			System.out.println("AttackBefore"+p.getPosX());
			p.attackEnd();
			System.out.println("AttackAfter"+p.getPosX());
		}
		p.setImage(new ImageIcon(SNL.class.getResource("../images/front_"+String.valueOf(charType)+".png")).getImage());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println(e.getKeyCode());
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			p.move(LEFT);
			System.out.println("left");
			break;
		case KeyEvent.VK_RIGHT:
			p.move(RIGHT);
			System.out.println("right");
			break;
		case KeyEvent.VK_UP:
			if (p.isJumping())
				return;
			
			// p.move(UP);
			jump();
			// repaint();
			System.out.println("jump");
			break;
		case KeyEvent.VK_DOWN:
			p.move(DOWN);
			System.out.println("down");
			break;
		case KeyEvent.VK_SPACE:
			if(p.isAttack())
				return;
			System.out.println("space"+p.getPosX());
			
			p.attack();
			break;
		}
		repaint();

	}
}
