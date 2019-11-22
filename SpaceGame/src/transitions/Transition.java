package transitions;

import renderEngine.DisplayManager;

public class Transition {
	
	private float transitionLength;
	private float currentTime;
	private boolean isPaused;
	private boolean isFinished;
	
	public Transition() {
		this.currentTime = 0;
		this.isFinished = true;
	}
	
	public void startTransition(float length) {
		this.transitionLength = length;
		this.isPaused = false;
		this.currentTime = 0;
		if(isFinished) {
			TransitionManager.addActiveTransition(this);
		}
		this.isFinished = false;
	}
	
	public void update() {
		if(!isPaused) {
			currentTime += DisplayManager.getFrameTimeSeconds()/transitionLength;
			if(currentTime >= 1) {
				currentTime = 1;
				TransitionManager.addFinihsedTransition(this);
				isFinished = true;
			}
		}
	}

	public float getCurrentTime() {
		return currentTime;
	}
	
	public boolean isFinished() {
		return isFinished;
	}

	public void pause() {
		this.isPaused = true;
	}
	
	public void unpause() {
		this.isPaused = false;
	}

}
