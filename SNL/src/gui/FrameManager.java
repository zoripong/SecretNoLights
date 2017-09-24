package gui;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class FrameManager extends JFrame {

	Container contain;

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
		// �ٽ� �ڵ�
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
		case "HallPanel":
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
}
