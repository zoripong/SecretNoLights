package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import adapter.RankingReader;
import model.Record;

public class RegisterPanel extends JPanel implements ActionListener, KeyListener {
	private FrameManager fm;

	private Image background;
	private Image screenImage;
	private Timer t = new Timer(10, this);
	
	private int startX;
	private int startY;
	private boolean isWrite;
	
	private ArrayList<ImageIcon> alphabets;
	private ArrayList<Character> writeChar;
	
	private Record record;
	public RegisterPanel(FrameManager fm, Record record) {
		this.fm = fm;
		this.record = record;
		
		init();

		setLayout(null);
		setFocusable(true);
		addKeyListener(this);
	}

	private void init() {
		background = new ImageIcon(SNL.class.getResource("../images/ranking_background.png")).getImage();
		
		startX = 100;
		startY = 100;
		isWrite = true;

		alphabets = new ArrayList<ImageIcon>();
		char alpha = 'a';
		for (int i = 0; i < 26; i++) {
			String name = "../images/ranking_" + alpha + ".png";
			alphabets.add(new ImageIcon(SNL.class.getResource(name)));
			alpha++;
		}
		
		writeChar = new ArrayList<Character>();
		
	}
	

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			pushAlpha('a');
			break;
		case KeyEvent.VK_B:
			pushAlpha('b');
			break;
		case KeyEvent.VK_C:
			pushAlpha('c');
			break;
		case KeyEvent.VK_D:
			pushAlpha('d');
			break;
		case KeyEvent.VK_E:
			pushAlpha('e');
			break;
		case KeyEvent.VK_F:
			pushAlpha('f');
			break;
		case KeyEvent.VK_G:
			pushAlpha('g');
			break;
		case KeyEvent.VK_H:
			pushAlpha('h');
			break;
		case KeyEvent.VK_I:
			pushAlpha('i');
			break;
		case KeyEvent.VK_J:
			pushAlpha('j');
			break;
		case KeyEvent.VK_K:
			pushAlpha('k');
			break;
		case KeyEvent.VK_L:
			pushAlpha('l');
			break;
		case KeyEvent.VK_M:
			pushAlpha('m');
			break;
		case KeyEvent.VK_N:
			pushAlpha('n');
			break;
		case KeyEvent.VK_O:
			pushAlpha('o');
			break;
		case KeyEvent.VK_P:
			pushAlpha('p');
			break;
		case KeyEvent.VK_Q:
			pushAlpha('q');
			break;
		case KeyEvent.VK_R:
			pushAlpha('r');
			break;			
		case KeyEvent.VK_S:
			pushAlpha('s');
			break;
		case KeyEvent.VK_T:
			pushAlpha('t');
			break;
		case KeyEvent.VK_U:
			pushAlpha('u');
			break;
		case KeyEvent.VK_V:
			pushAlpha('v');
			break;
		case KeyEvent.VK_W:
			pushAlpha('w');
			break;
		case KeyEvent.VK_X:
			pushAlpha('x');
			break;
		case KeyEvent.VK_Y:
			pushAlpha('y');
			break;
		case KeyEvent.VK_Z:
			pushAlpha('z');
			break;
		case KeyEvent.VK_BACK_SPACE:
			deleteAlpha();
			break;
		}

	}

	private void pushAlpha(char ch) {
		if(writeChar.size()>3)
			return;
		if(isWrite) {			
			writeChar.add(ch);
			isWrite = false;
			System.out.println(ch);
		}
	}
	private void deleteAlpha() {
		if(writeChar.size() != 0)
			writeChar.remove(writeChar.size()-1);
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		isWrite = true;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		screenImage = createImage(SNL.SCREEN_WIDTH, SNL.SCREEN_HEIGHT);
		Graphics screenGraphic = screenImage.getGraphics();
		screenDraw(screenGraphic);
		g.drawImage(screenImage, 0, 0, null);
		
		if(writeChar.size() == 4) {
			fm.changePanel("HallPanel");
			String name = "";
			for(int i = 0; i<3; i++) {
				name += writeChar.get(i);
			}
			System.out.println("name : "+ name);
			fm.getRecord().setName(name);
			return;
		}
		
		startX = 100;
		for(int i = 0; i<writeChar.size(); i++){
			
			g2d.drawImage(alphabets.get(Math.abs((int) (97 - writeChar.get(i)))).getImage(), startX, startY, null);
			startX += (alphabets.get(Math.abs((int) (97 - writeChar.get(i)))).getIconWidth() + 10);
			
		}
				


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
