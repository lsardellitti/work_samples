package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import mouseManager.MouseMaster;
import toolbox.Maths;
import transitions.Transition;

public class Camera {
	
	private static final float MIN_DISTANCE = 20;
	private static final float MAX_DISTANCE = 200;
	private static final float MIN_PITCH = 0.2f;
	private static final float MAX_PITCH = 1.2f;
	private static final float TRANSITION_TIME = 1.0f;
	
	private int directionOption;
	
	private float distanceFromPlayer = 100;
	
	private Matrix4f viewMatrix;
	private Vector3f position;
	private Vector3f facingDirection;
	private float roll = 0;
	private float pitch = 0.5f;
	private float yaw = 0;
	
	private Entity centredEntity;
	private Vector3f relativeUpDirection;
	private Vector3f relativeBinormalDirection;
	
	private Transition moveTypeTransition = new Transition();
	private Vector3f initialUpDirection;
	private Vector3f initialBinormalDirection;
	private Vector3f transitionUpCross;
	private Vector3f transitionBiCross;
	private float transitionUpAngle;
	private float transitionBiAngle;
	
	public Camera(Entity centredEntity) {
		this.centredEntity = centredEntity;
		initializeMoveAroundEntity(0);
	}
	
	public Camera() {
		initializeFreeMove();
	}
	
	public void transistionMoveType(int directionOption) {//used for smooth transitioning camera
		this.directionOption = directionOption;
		initialBinormalDirection = centredEntity.getBinormalDirection();
		initialUpDirection = centredEntity.getUpDirection();
		Vector3f finalBinormalDirection;
		Vector3f finalUpDirection;
		if (directionOption == 0) {
			finalBinormalDirection = centredEntity.getBinormalDirection();
			finalUpDirection = centredEntity.getUpDirection();
		}
		else {// (directionOption == 1) {
			finalBinormalDirection = centredEntity.getForwardDirection();
			finalUpDirection = centredEntity.getBinormalDirection();
		}
		transitionUpCross = Vector3f.cross(initialUpDirection, finalUpDirection, null);
		transitionBiCross = Vector3f.cross(initialBinormalDirection, finalBinormalDirection, null);
		transitionUpAngle = Vector3f.angle(initialUpDirection, finalUpDirection);
		transitionBiAngle = Vector3f.angle(initialBinormalDirection, finalBinormalDirection);
		moveTypeTransition.startTransition(TRANSITION_TIME);
	}
	
	public void initializeMoveAroundEntity(int directionOption) {//set initial camera position for an entity
		this.directionOption = directionOption;
		roll = 0;
		pitch = 0.5f;
		yaw = 0;
		//different direction vectors for different camera angles
		if (directionOption == 0) {
			this.relativeBinormalDirection = centredEntity.getBinormalDirection();
			this.relativeUpDirection = centredEntity.getUpDirection();
		}
		if (directionOption == 1) {
			this.relativeBinormalDirection = centredEntity.getForwardDirection();
			this.relativeUpDirection = centredEntity.getBinormalDirection();
		}
		facingDirection = Maths.negateVector(relativeUpDirection);
		facingDirection = Maths.rotateVector(facingDirection, relativeBinormalDirection, -pitch);
		facingDirection = Maths.rotateVector(facingDirection, relativeUpDirection, yaw);
		facingDirection.normalise();
		position = Vector3f.sub(centredEntity.getCentrePosition(), Maths.scaledVector(facingDirection, distanceFromPlayer), null);
	}
	
	public void moveAroundEntity() {
		//only control camera if transition is finished
		if(!moveTypeTransition.isFinished()) {
			relativeUpDirection = Maths.rotateVector(initialUpDirection, transitionUpCross, transitionUpAngle * moveTypeTransition.getCurrentTime());
			relativeBinormalDirection = Maths.rotateVector(initialBinormalDirection, transitionBiCross, transitionBiAngle * moveTypeTransition.getCurrentTime());
			facingDirection = Maths.negateVector(relativeUpDirection);
			facingDirection = Maths.rotateVector(facingDirection, relativeBinormalDirection, -pitch);
			facingDirection = Maths.rotateVector(facingDirection, relativeUpDirection, yaw);
			facingDirection.normalise();
			position = Vector3f.sub(centredEntity.getCentrePosition(), Maths.scaledVector(facingDirection, distanceFromPlayer), null);
		}else {//user control camera
			if (directionOption == 0) {
				this.relativeBinormalDirection = centredEntity.getBinormalDirection();
				this.relativeUpDirection = centredEntity.getUpDirection();
			}
			if (directionOption == 1) {
				this.relativeBinormalDirection = centredEntity.getForwardDirection();
				this.relativeUpDirection = centredEntity.getBinormalDirection();
			}
			//only move if move is not over a menu
			if(!MouseMaster.isOverMenu()) {
				distanceFromPlayer -= MouseMaster.getFrameMouseDWheel()*0.1f;
				distanceFromPlayer = Maths.clampFloat(distanceFromPlayer, MIN_DISTANCE, MAX_DISTANCE);
			}
			if (Mouse.isButtonDown(1)){
				pitch += MouseMaster.getFrameMouseDY()*0.01f;
				pitch = Maths.clampFloat(pitch, MIN_PITCH, MAX_PITCH);
				yaw -=MouseMaster.getFrameMouseDX()*0.01f;
			}
			facingDirection = Maths.negateVector(relativeUpDirection);
			facingDirection = Maths.rotateVector(facingDirection, relativeBinormalDirection, -pitch);
			facingDirection = Maths.rotateVector(facingDirection, relativeUpDirection, yaw);
			facingDirection.normalise();
			position = Vector3f.sub(centredEntity.getCentrePosition(), Maths.scaledVector(facingDirection, distanceFromPlayer), null);
		}
	}
	
	public void initializeFreeMove() {
		if(this.centredEntity == null) {
		this.relativeUpDirection = new Vector3f(0,1,0);
		this.relativeBinormalDirection = new Vector3f(1,0,0);
		this.facingDirection = new Vector3f(0,0,1);
		this.position = new Vector3f(0,0,0);
		} else {
			this.facingDirection = Maths.negateVector(this.centredEntity.getUpDirection());
			this.relativeUpDirection = this.centredEntity.getBinormalDirection();
			this.relativeBinormalDirection = Vector3f.cross(relativeUpDirection, facingDirection, null);
			this.position = Vector3f.add(this.centredEntity.getPosition(), Maths.normScaledVector(this.centredEntity.getUpDirection(), distanceFromPlayer), null);
		}
	}
	
	public void freeMove() {//move without a tie to any object
		float mouseDX = MouseMaster.getFrameMouseDX();
		float mouseDY = MouseMaster.getFrameMouseDY();
		if (Mouse.isButtonDown(0)){
			this.position = Vector3f.add(this.position, Maths.normScaledVector(relativeUpDirection, (float) (-mouseDY)), null);
			this.position = Vector3f.add(this.position, Maths.normScaledVector(relativeBinormalDirection, (float) (mouseDX)), null);
		}
		else if (Mouse.isButtonDown(1)) {
			this.relativeUpDirection = Maths.rotateVector(relativeUpDirection, relativeBinormalDirection, (float) (-mouseDY*0.005f));
			this.facingDirection = Maths.rotateVector(facingDirection, relativeBinormalDirection, (float) (-mouseDY*0.005f));
			this.relativeBinormalDirection = Maths.rotateVector(relativeBinormalDirection, relativeUpDirection, (float) (-mouseDX*0.005f));
			this.facingDirection = Maths.rotateVector(this.facingDirection, relativeUpDirection, (float) (-mouseDX*0.005f));
		}else {
			if(!MouseMaster.isOverMenu()) {
				this.position = Vector3f.add(position, Maths.normScaledVector(facingDirection, MouseMaster.getFrameMouseDWheel()*0.1f), null);
			}
		}
	}
	
	public void updateViewMatrix() {
		this.viewMatrix = Maths.createViewMatrix(this);
	}
	
	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public void setPitchToMin() {
		this.pitch = MIN_PITCH;
	}
	
	public void setPitchToMax() {
		this.pitch = MAX_PITCH;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public Vector3f getFacingDirection() {
		return facingDirection;
	}

	public void setFacingDirection(Vector3f absoluteFacingDirection) {
		this.facingDirection = absoluteFacingDirection;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Entity getCentredEntity() {
		return centredEntity;
	}

	public void setCentredEntity(Entity entity, int directionOption) {
		this.centredEntity = entity;
		initializeMoveAroundEntity(directionOption);
	}

	public Vector3f getRelativeUpDirection() {
		return relativeUpDirection;
	}

	public Vector3f getRelativeBinormalDirection() {
		return relativeBinormalDirection;
	}

	public int getDirectionOption() {
		return directionOption;
	}

	public void setDirectionOption(int directionOption) {
		this.directionOption = directionOption;
	}

	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}	

}
