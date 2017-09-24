package thread;
import java.beans.IntrospectionException;

import customInterface.JumpListener;


public class JumpThread extends Thread {

    boolean jumping=false;
    
    int jumpingy[]=new int[]{0, -41, -36, -25, -20, -15,-11,-11,-6, 0, 6, 11,11,15,20, 25, 36, 41}; // y좌표 감소 후 재 증가 
    
    int jumpIdx;
    
    JumpListener jumpListener;
    
    public JumpThread(JumpListener jumpListener) {
    	super();
    	this.jumpIdx=1;
    	this.jumpListener=jumpListener;
    }
    
    public void run() {
    	
    	while(this.jumpIdx<this.jumpingy.length) { // index < array length
    		
    		this.jumpListener.jumpTimeArrived(this.jumpIdx, this.jumpingy[this.jumpIdx]);
//    		System.out.println(jumpIdx+" / "+jumpingy[this.jumpIdx]);
	    	try {
	    		Thread.sleep(50);
	    	} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	
	    	this.jumpIdx++;
    	}
    	this.jumpListener.jumpTimeEnded();
    		
    }
	
}
