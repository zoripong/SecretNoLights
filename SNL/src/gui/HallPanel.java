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

public class HallPanel extends JPanel implements ActionListener, KeyListener{
	FrameManager fm;
	
	Image background;
	Image screenImage;
	Timer t = new Timer(10, this);

	public HallPanel(FrameManager fm) {
		this.fm = fm;
		init();
		
		setLayout(null);
		setFocusable(true);
		addKeyListener(this);
	}
	private void init() {
		background = new ImageIcon(SNL.class.getResource("../images/explainGameBackground.png")).getImage();

	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode()) {
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_SPACE:
			fm.changePanel("MainPanel");
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
