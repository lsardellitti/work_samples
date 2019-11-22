package parametrizedMotion;

import org.lwjgl.util.vector.Vector3f;

import transitions.Transition;

public class ParametrizedMotion {
	
	private Transition motionTransition;
	private float timeLength;
	
	public ParametrizedMotion(float timeLength) {
		this.timeLength = timeLength;
		this.motionTransition = new Transition();
	}
	
	public void startMotion() {
		motionTransition.startTransition(timeLength);
	}
	
	public void pauseMotion() {
		motionTransition.pause();
	}
	
	public void unpauseMotion() {
		motionTransition.unpause();
	}
	
	public Vector3f getCurrentPosition() {
		return new Vector3f(0,0,0);
	}

	public Transition getMotionTransition() {
		return motionTransition;
	}	

}
