package thread;

import java.beans.IntrospectionException;

import customInterface.JumpListener;

public class JumpThread extends Thread {

	private boolean isDownJumping = false;

	// int jumpingy[]=new int[]{0, -41, -36, -25, -20, -15,-11,-11,-6, 0}; // y좌표 감소
	// 후 재 증가
	private int jumpingy[] = new int[] { 0, -41, -36, -25, -20, -15, -11, -11, -6, 0, 6, 11, 11, 15, 20, 25, 36, 41 }; // y좌표
																														// 감소
	// int jumpingUp
	private int jumpIdx;
	private boolean isJumping;
	
	private JumpListener jumpListener;
	
	public JumpThread(JumpListener jumpListener) {
		super();
		this.jumpIdx = 1;
		this.jumpListener = jumpListener;
		isJumping = true;
	}

	public void run() {
		isJumping = true;
		while (this.jumpIdx < this.jumpingy.length) { // index < array length
			if(!isJumping) {
				jumpListener.jumpTimeEnded(true);
				return;
			}
			this.jumpListener.jumpTimeArrived(this.jumpIdx, this.jumpingy[this.jumpIdx], isDownJumping); // TODO : isDownJumping 도 파라미터로 넘겨줌
			// System.out.println(jumpIdx+" / "+jumpingy[this.jumpIdx]);
			
			if(jumpingy[this.jumpIdx] > 0)
				isDownJumping = true;
			else isDownJumping = false;
			
//			System.out.println(isDownJumping);
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			this.jumpIdx++;
		}	
		jumpListener.jumpTimeEnded(false);
	}
	
	public void stopJump() {
		isJumping = false;
	}
}
