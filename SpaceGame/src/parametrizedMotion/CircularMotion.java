package parametrizedMotion;

import org.lwjgl.util.vector.Vector3f;

import toolbox.Maths;

public class CircularMotion extends ParametrizedMotion{

	private float radius;
	private Vector3f centre;
	private Vector3f upDirection;
	private Vector3f initialForwardDirection;
	private float numRotations;
	
	public CircularMotion(float timeLength, float radius, Vector3f centre, Vector3f upDirection, Vector3f initialForwardDirection, float numRotations) {
		super(timeLength);
		this.radius = radius;
		this.centre = centre;
		this.upDirection = upDirection;
		this.initialForwardDirection = initialForwardDirection;
		this.numRotations = numRotations;
	}
	
	public Vector3f getCurrentPosition() {
		float currentTime = super.getMotionTransition().getCurrentTime();
		Vector3f newForwardDirection = Maths.rotateVector(initialForwardDirection, upDirection, (float) (Math.PI*2*numRotations*currentTime));
		return Vector3f.add(centre, Maths.normScaledVector(newForwardDirection, radius), null);
	}
	

}
