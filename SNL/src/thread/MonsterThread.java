package thread;

import javax.swing.ImageIcon;

import customInterface.AutoMovingListener;
import customInterface.JumpListener;
import gui.SNL;
import model.Monster;

public class MonsterThread extends Thread implements JumpListener{

	Monster monster;
	AutoMovingListener autoMovingListener;
	private boolean isLive = true;
	
	public MonsterThread(Monster monster, AutoMovingListener autoMovingListener) {
		this.monster = monster;
		this.autoMovingListener = autoMovingListener;
	}

	public void run() {
		int flag = 0;
		
		while (isLive) {
			flag++;
			
			if(flag % 20 == 0 && ((int)(Math.random()*50))%2 == 0)
				monster.changeDirection();
			
			monster.update();
			autoMovingListener.repaintable();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {

			}

			if(flag % 30 == 0 && ((int)(Math.random()*100)%4) == 0)
				new JumpThread(this).start();
		}
	}
	
	@Override
	public void jumpTimeArrived(int jumpIdx, int jumpy) {
		// TODO Auto-generated method stub
//		if (monster.isRight())
//			p.addX(5);
//		else
//			monster.addX(-5);
		monster.addY(jumpy);
		ImageIcon icon;
		if(monster.isRight())
			icon = new ImageIcon(SNL.class.getResource("../images/jump_right_monster.png"));

			//			icon = new ImageIcon(SNL.class.getResource("../images/jump_right_monster.png"));
		else
			icon = new ImageIcon(SNL.class.getResource("../images/jump_left_monster.png"));			

			//			icon = new ImageIcon(SNL.class.getResource("../images/jump_left_monster.png"));			
		
		monster.setImage(icon);
		if (monster.getPosX() < 0) {
			System.out.println("���� ��");
			monster.setPosX(0);
			monster.changeDirection();
		}
		if (monster.getPosX() > (SNL.SCREEN_WIDTH)) {
			System.out.println("������ ��");
			monster.setPosX(SNL.SCREEN_WIDTH);
			monster.changeDirection();
		}

		monster.setJumpIdx(jumpIdx);

	}

	@Override
	public void jumpTimeEnded() {
		monster.setImage(new ImageIcon(SNL.class.getResource("../images/front_monster.png")));
		monster.setJumpIdx(0);

	}

	public void onStop() {
		isLive = false;
	}
}