package thread;

import javax.swing.ImageIcon;

import adapter.MapReader;
import customInterface.AutoMovingListener;
import customInterface.JumpListener;
import gui.SNL;
import model.Monster;

public class MonsterThread extends Thread implements JumpListener{
	private final int BLOCK_WIDTH = 43;

	Monster monster;
	AutoMovingListener autoMovingListener;
	private boolean isLive = true;
	private MapReader mapReader;
	
	public MonsterThread(Monster monster, MapReader mapReader, AutoMovingListener autoMovingListener) {
		this.monster = monster;
		this.autoMovingListener = autoMovingListener;
		this.mapReader = mapReader;
	}

	public void run() {
		int flag = 0;
		
		while (isLive) {
			flag++;
			
			if(flag % 20 == 0 && ((int)(Math.random()*117))%2 == 0)
				monster.changeDirection();
			
			monster.update();
			autoMovingListener.repaintable();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {

			}

			if(monster.isJumpable() && flag % 30 == 0 && ((int)(Math.random()*5)%3) == 0)
				new JumpThread(this, mapReader, monster).start();
		}
	}
	
	@Override
	public void jumpTimeArrived(int jumpIdx, int jumpy) {

		monster.addY(jumpy);
		ImageIcon icon;
		if(monster.isRight())
			icon = new ImageIcon(SNL.class.getResource("../images/jump_right_monster.png"));

			//			icon = new ImageIcon(SNL.class.getResource("../images/jump_right_monster.png"));
		else
			icon = new ImageIcon(SNL.class.getResource("../images/jump_left_monster.png"));			

			//			icon = new ImageIcon(SNL.class.getResource("../images/jump_left_monster.png"));			
		
		monster.setImage(icon);
		
		if (monster.getPosX() < BLOCK_WIDTH) {			
			monster.setPosX(BLOCK_WIDTH);
			monster.changeDirection();
		}
		
		if (monster.getPosX() > (SNL.SCREEN_WIDTH - monster.getWidth() - BLOCK_WIDTH)) {			
			monster.setPosX(SNL.SCREEN_WIDTH - monster.getWidth() - BLOCK_WIDTH);
			monster.changeDirection();
		}
		
		monster.setJumpIdx(jumpIdx);

	}

	@Override
	public void jumpTimeEnded(boolean isStop) {
		monster.setImage(new ImageIcon(SNL.class.getResource("../images/front_monster.png")));
		monster.setJumpIdx(0);

	}

	public void onStop() {
		isLive = false;
	}
}
