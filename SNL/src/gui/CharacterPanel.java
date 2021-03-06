package gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import adapter.Music;

public class CharacterPanel extends JPanel implements ActionListener, KeyListener {

	FrameManager fm;

	Image background;
	Image screenImage;
	Timer t = new Timer(10, this);

	Music gameMusic;

	int charType; // 0~3
	Image charImages[];
	
	public CharacterPanel(FrameManager fm) {
		this.fm = fm;
		init();

		setLayout(null);
		setFocusable(true);
		addKeyListener(this);
	}

	private void init() {
//		System.out.println("character panel");
		background = new ImageIcon(SNL.class.getClassLoader().getResource("images/characterBackground.png")).getImage();

		charImages = new Image[4];
		initSelectBoard();
		
		gameMusic = new Music("mainMusic.mp3", true);
		gameMusic.start();
	}

	public void paint(Graphics g) {
		super.paint(g);
		screenImage = createImage(SNL.SCREEN_WIDTH, SNL.SCREEN_HEIGHT);
		Graphics screenGraphic = screenImage.getGraphics();
		screenDraw(screenGraphic);
		g.drawImage(screenImage, 0, 0, null);
		
		for(int i = 0; i<charImages.length; i++) {
			g.drawImage(charImages[i], 220*i-130, 30, null);
		}

	}

	public void screenDraw(Graphics g) {
		g.drawImage(background, 0, 0, null);
		paintComponents(g);
		this.repaint();
	}	

	@Override
	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			gameMusic.close();
			fm.changePanel("MainPanel");
			break;
		case KeyEvent.VK_LEFT:
			if (charType > 0) {
				charType--;
				initSelectBoard();
				repaint();
			}
				
			break;
		case KeyEvent.VK_RIGHT:
			if (charType < 3) {
				charType++;
				initSelectBoard();				
				repaint();
			}
			break;
		case KeyEvent.VK_SPACE:
		case KeyEvent.VK_ENTER:
			gameMusic.close();
			fm.gameStart(charType);
		}
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
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
	
	private void initSelectBoard() {
		for(int i = 0; i<charImages.length; i++) {
		String source = null;
		if(i == charType)
			source = "images/character_type_selected_"+String.valueOf(i)+".png";
		else
			source = "images/character_type_"+String.valueOf(i)+".png";
		
//		System.out.println(source);
		charImages[i] = new ImageIcon(SNL.class.getClassLoader().getResource(source)).getImage();
	}

	}

}
