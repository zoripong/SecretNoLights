package gui;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Record;

public class FrameManager extends JFrame {

	private Container contain;
	private Record record;
	
	public FrameManager() {
		init();
	}

	public FrameManager(boolean isInnerInstace) {
		contain = getContentPane();

	}

	public void init() {
		contain = getContentPane();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("SNL");
		setSize(SNL.SCREEN_WIDTH, SNL.SCREEN_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);

		// GamePanel p = new GamePanel();
		MainPanel p = new MainPanel(this);
		p.setBounds(100, 100, 100, 100);
		contain.add(p);

		setVisible(true);
	}

	public void changePanel(String panelName) {
		// ÇÙ½É ÄÚµå
		contain.removeAll();

		JPanel p = null;
		switch (panelName) {
		case "MainPanel":
			p = new MainPanel(this);
			break;

		case "CharacterPanel":
			p = new CharacterPanel(this);
			break;
		case "ExplainPanel":
			p = new ExplainPanel(this);
			break;
		case "RegisterPanel":
			if(record != null)
				p = new RegisterPanel(this, record);
			break;
		case "HallPanel":
			p = new HallPanel(this, record);
			break;
		}
		contain.add(p);
		p.requestFocus();
		p.setFocusable(true);

		revalidate();
		repaint();

	}

	public void gameStart(int charType) {
		contain.removeAll();

		JPanel p = new GamePanel(this, charType);

		contain.add(p);
		p.requestFocus();
		p.setFocusable(true);

		revalidate();
		repaint();

	}
	
	public void setRecord(Record record) {
		this.record = record;
	}
	public Record getRecord() {
		return this.record;
	}
	
}
