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

import adapter.Music;

public class StoryPanel extends JPanel implements ActionListener, KeyListener {
	private final int MAX_PAGE = 3;
	FrameManager fm;

	private int page;
	Image background;
	Image screenImage;
	Timer t = new Timer(10, this);

	Music gameMusic;

	public StoryPanel(FrameManager fm) {
		this.fm = fm;
		init();

		setLayout(null);
		setFocusable(true);
		addKeyListener(this);
	}

	private void init() {
		page = 1;
		background = new ImageIcon(SNL.class.getClassLoader().getResource("images/story_" + page + ".png")).getImage();
	
		gameMusic = new Music("Are_You_Sleeping_Instrumental.mp3", true);
		gameMusic.start();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			if (page >= MAX_PAGE) {
				fm.changePanel("CharacterPanel");
				gameMusic.close();
			} else {
				page++;
			}
			break;
		case KeyEvent.VK_LEFT:
			if(page > 1)
				page--;
			break;
		case KeyEvent.VK_ESCAPE:
			fm.changePanel("MainPanel");
			gameMusic.close();
			break;
		case KeyEvent.VK_SPACE:
			fm.changePanel("CharacterPanel");
			gameMusic.close();
			break;
			
		}

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void paint(Graphics g) {
		super.paint(g);
		background = new ImageIcon(SNL.class.getClassLoader().getResource("images/story_" + page + ".png")).getImage();

		screenImage = createImage(SNL.SCREEN_WIDTH, SNL.SCREEN_HEIGHT);
		Graphics screenGraphic = screenImage.getGraphics();
		screenDraw(screenGraphic);
		g.drawImage(screenImage, 0, 0, null);
	}

	private void screenDraw(Graphics g) {
		g.drawImage(background, 0, 0, null);
		paintComponents(g);
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
