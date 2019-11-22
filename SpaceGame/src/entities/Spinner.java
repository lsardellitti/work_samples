package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import parametrizedMotion.CircularMotion;
import parametrizedMotion.ParametrizedMotion;

public class Spinner extends Entity{

	private ParametrizedMotion spinnerMotion;
	private Vector3f centre = new Vector3f(0,0,100);
	private Vector3f upDirection = new Vector3f(0,0,1);
	private Vector3f initialForwardDirection = new Vector3f(0,1,0);
	private float radius = 100;
	private float numRotations = 10;
	private float timeDuration = 10;
	
	public Spinner(TexturedModel model, float scale) {
		super(model, scale);
		this.spinnerMotion = new CircularMotion(timeDuration, radius, centre, upDirection, initialForwardDirection, numRotations);
	}
	
	public void startCircularMotion() {
		spinnerMotion.startMotion();
	}
	
	public void circularMove() {
		super.setPosition(spinnerMotion.getCurrentPosition());
	}
	
	public void pauseMotion() {
		spinnerMotion.pauseMotion();
	}
	
	public void unpauseMotion() {
		spinnerMotion.unpauseMotion();
	}

}
