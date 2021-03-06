package gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import adapter.Music;
import customInterface.Direction;
import model.Player;
import thread.JumpThread;

public class MainPanel extends JPanel implements ActionListener, KeyListener, Direction {
	private final int GAME_START = 450;
	private final int EXPLAIN_GAME = 560;
	FrameManager fm;

	Image background;
	Image screenImage;
	Timer t = new Timer(10, this);
	Music gameMusic;

	ImageIcon startImage;
	ImageIcon explainImage;
	
	Player selecter;

	public MainPanel(FrameManager f) {
		fm = f;

		init();
		setLayout(null);
		setFocusable(true);
		addKeyListener(this);

		t.start();
	}

	private void init() {

		System.out.println(SNL.class.getClassLoader().getResource("images/selecter.png"));
		ImageIcon selectImage = new ImageIcon(SNL.class.getClassLoader().getResource("images/selecter.png"));
//		selectImage = new ImageIcon(SNL.class.getResource("../images/selecter.png"));
		// selecter = new Player(SNL.SCREEN_WIDTH/2 + 200, 490, selectImage, 0, 5,
		// true);
		selecter = new Player(SNL.SCREEN_WIDTH / 2 + 220, GAME_START+10, selectImage, -1, 0, EXPLAIN_GAME-GAME_START);
		
		background = new ImageIcon(SNL.class.getClassLoader().getResource("images/background.png")).getImage();

		startImage = new ImageIcon(SNL.class.getClassLoader().getResource("images/start.png"));
		explainImage = new ImageIcon(SNL.class.getClassLoader().getResource("images/explain.png"));

		
		gameMusic = new Music("Above_and_Beyond.mp3", true);
		gameMusic.start();
	}

	public void paint(Graphics g) {
		super.paint(g);
		screenImage = createImage(SNL.SCREEN_WIDTH, SNL.SCREEN_HEIGHT);
		Graphics screenGraphic = screenImage.getGraphics();
		screenDraw(screenGraphic);
		g.drawImage(screenImage, 0, 0, null);
		selecter.draw(g);
		
		g.drawImage(startImage.getImage(), SNL.SCREEN_WIDTH/2 - startImage.getIconWidth()/2 -20, GAME_START, null);
		g.drawImage(explainImage.getImage(), SNL.SCREEN_WIDTH/2 - explainImage.getIconWidth()/2 -20, EXPLAIN_GAME, null);
	}

	public void screenDraw(Graphics g) {
		g.drawImage(background, 0, 0, null);
		paintComponents(g);
		this.repaint();
	}
	
	

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			System.out.println(selecter.getPosY() + ">" + GAME_START);
			if (selecter.getPosY() -10> GAME_START)
				selecter.move(UP);
			break;
		case KeyEvent.VK_DOWN:
			System.out.println(selecter.getPosY() + "<" + EXPLAIN_GAME);
			if (selecter.getPosY() -10< EXPLAIN_GAME)
				selecter.move(DOWN);
			break;
		case KeyEvent.VK_SPACE:
		case KeyEvent.VK_ENTER:
			// TODO �г� ��ȯ
			gameMusic.close();
			if (selecter.getPosY()-10 == GAME_START) {
				fm.changePanel("StoryPanel");
			} else {
				fm.changePanel("ExplainPanel");
			}
			break;
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		repaint();
	}

}
