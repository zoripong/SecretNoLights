package customInterface;

public interface JumpListener {
	
	int IMAGE_SIZE = 5;	
	public void jumpTimeArrived(int jumpIdx, int jumpy, boolean isDown);
	
	public void jumpTimeEnded(boolean isStop);
	
}
