package thread;

import java.awt.geom.Rectangle2D;
import java.beans.IntrospectionException;

import adapter.MapReader;
import customInterface.JumpListener;
import gui.SNL;
import model.GameObj;
import model.Monster;
import model.Player;

public class JumpThread extends Thread {

	private boolean isDownJumping = false;

	private int jumpingy[] = new int[] { 0, -41, -36, -25, -20, -15, -11, -11, -6, 0, 6, 11, 11, 15, 20, 25, 36, 41 };
	// private int jumpingDownY[] = new int[] { 0, 6, 11, 11, 15, 20, 25, 36, 41 };
	// // length = 8m
	// int jumpingUp
	private int jumpIdx;
	private boolean isJumping;

	private JumpListener jumpListener;

	private MapReader mMapReader;
	private Player player;
	private Monster monster;

	private Rectangle2D mCeilingArea;

	public JumpThread(JumpListener jumpListener, MapReader mapReader, Player player) {
		super();
		this.jumpIdx = 1;
		this.jumpListener = jumpListener;
		isJumping = true;
		this.mMapReader = mapReader;
		this.player = player;
		this.monster = null;
		mCeilingArea = new Rectangle2D.Double(0, 0, SNL.SCREEN_WIDTH, mMapReader.getBlockHeight());
	}

	public JumpThread(JumpListener jumpListener, MapReader mapReader, Monster monster) {
		super();
		this.jumpIdx = 1;
		this.jumpListener = jumpListener;
		isJumping = true;
		this.mMapReader = mapReader;
		this.monster = monster;
		this.player = null;
		mCeilingArea = new Rectangle2D.Double(0, 0, SNL.SCREEN_WIDTH, mMapReader.getBlockHeight());
	}

	public void run() {
		isJumping = true;
		while (this.jumpIdx < this.jumpingy.length) { // index < array length
			if (jumpingy[this.jumpIdx] > 0)
				isDownJumping = true;
			else
				isDownJumping = false;

			if (!isJumping) {
				jumpListener.jumpTimeEnded(true);
				return;
			}
			if (player != null) {
				if (isDownJumping == false) {
					Rectangle2D playerArea = new Rectangle2D.Double(player.getPosX(), player.getPosY(),
							player.getWidth(), player.getHeight());
					if (playerArea.intersects(mCeilingArea)) {
						jumpIdx = jumpingy.length - jumpIdx - 1;
					}
				}
				if (isDownJumping && (mMapReader.isBlock(player.getLocation('B').getX(), player.getLocation('B').getY())
						|| mMapReader.isBlock(player.getLocation('C').getX(), player.getLocation('C').getY()))) {

					if (player.getLocation('C').getY() % mMapReader.getBlockHeight() < mMapReader.getBlockHeight()
							/ 2) {
						player.setPosY(player.getPosY() - player.getLocation('C').getY() % mMapReader.getBlockHeight());
						jumpListener.jumpTimeEnded(false);
						return;

					}
				}
			}
			if (monster != null) {
				if (isDownJumping == false) {
					Rectangle2D monsterArea = new Rectangle2D.Double(monster.getPosX(), monster.getPosY(),
							monster.getWidth(), monster.getHeight());
					if (monsterArea.intersects(mCeilingArea)) {
						jumpIdx = jumpingy.length - jumpIdx - 1;
					}
				}
				if (isDownJumping && (mMapReader.isBlock(monster.getLocation('B').getX(),
						monster.getLocation('B').getY())
						|| mMapReader.isBlock(monster.getLocation('C').getX(), monster.getLocation('C').getY()))) {

					if (monster.getLocation('C').getY() % mMapReader.getBlockHeight() < mMapReader.getBlockHeight()
							/ 2) {
						monster.setPosY(
								monster.getPosY() - monster.getLocation('C').getY() % mMapReader.getBlockHeight() - 2);
						jumpListener.jumpTimeEnded(false);
						return;

					}
				}
			}
			this.jumpListener.jumpTimeArrived(this.jumpIdx, this.jumpingy[this.jumpIdx]); 

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			this.jumpIdx++;
		}
		if (player != null)
			player.setPosY(player.getPosY() - player.getLocation('C').getY() % mMapReader.getBlockHeight());
		if (monster != null)
			monster.setPosY(monster.getPosY() - monster.getLocation('C').getY() % mMapReader.getBlockHeight());

		jumpListener.jumpTimeEnded(false);
	}

	public void setMap(MapReader mapReader, Player player) {
		mMapReader = mapReader;
		this.player = player;
	}

	public void stopJump() {
		isJumping = false;
	}

}
