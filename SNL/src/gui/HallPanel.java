package gui;

import java.awt.Graphics;
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

public class HallPanel extends JPanel implements ActionListener, KeyListener {
	private FrameManager fm;

	private Image background;
	private Image screenImage;
	private Timer t = new Timer(10, this);

	private RankingReader rankingReader;
	private Record record;

	private int myRank;
	private int outputIdx;

	public HallPanel(FrameManager fm, Record record) {
		this.fm = fm;
		this.record = record;

		init();

		setLayout(null);
		setFocusable(true);
		addKeyListener(this);
	}

	private void init() {
		background = new ImageIcon(SNL.class.getResource("../images/ranking_background.png")).getImage();
		rankingReader = new RankingReader(record);
		myRank = rankingReader.insertRecord();

		
		if (myRank+1 == 1 || myRank+1 == 2) {
			outputIdx = 0;
		} else if (myRank+1 == rankingReader.getRankingSize()) {
			outputIdx = rankingReader.getRankingSize() - 5;
		} else if (myRank+1 == rankingReader.getRankingSize()-1) {
			outputIdx = rankingReader.getRankingSize() - 5;
		} else {
			outputIdx = myRank - 2;
		}
		System.out.println("size : "+rankingReader.getRankingSize()+" / ∑©≈∑ :" + myRank +" / ¿Œµ¶Ω∫ : "+ outputIdx);
		
		try {
			Thread.sleep(3000);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_SPACE:
			fm.changePanel("MainPanel");
			rankingReader.writeFile();
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

		rankingReader.showRecord(g, outputIdx);

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
