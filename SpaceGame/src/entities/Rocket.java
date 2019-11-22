package entities;

import org.lwjgl.util.vector.Vector3f;

import menus.MenuManager;
import models.StandardModels;
import models.TexturedModel;
import planets.Planet;
import planets.PlanetManager;
import renderEngine.DisplayManager;
import shooting.Shooter;
import toolbox.Maths;

public class Rocket extends Entity{

	
	private static final float MAX_SPEED = 100;
	private static final float MAX_SPEED_COLLISION = 1;
	private static final float COLLISION_BUFFER = 0.02f;
	private static final float TAKEOFF_SPEED = 50;
	private static final float MIN_PLANET_DISTANCE = 100;
	
	private float speed = 0;
	private float shootingRadius;
	private Shooter shooter;
	private int motionStage;
	private Vector3f landingVelocity;
	private float landingTheta;
	private float landingPhi;
	private boolean justLanded;
	private Entity passenger;
	private boolean takeoff;
	
	public Rocket(TexturedModel model, float scale, Planet planet, float theta, float phi) {
		super(model, scale, planet);
		super.setToPlanet(theta, phi);
		super.setHealth(1000);
		super.setMaxHealth(1000);
		super.setDestructible(true);
		super.setUseBasicPopupDirection(true);
		this.landingTheta = theta;
		this.landingPhi = phi;
		shooter = new Shooter(StandardModels.bullet, 0.006f, 600, 600, 0.2f, 1000, 100, 2, 10);
		this.shootingRadius = super.getModel().getShootingRadius() * scale;
	}
	
	public void stagedMotion(boolean isThrust, float turnSpeedX, float turnSpeedY, float thrustPower, float airDrag) {
		//different stages of motion for takeoff landing and space
		if(motionStage == 0) {
			takeOff(isThrust);
		}else if(motionStage == 1) {
			spaceMove(isThrust, turnSpeedX, turnSpeedY, thrustPower, airDrag);
		}else {
			land(isThrust, turnSpeedX, turnSpeedY, thrustPower);
		}
	}
	
	private void takeOff(boolean isThrust) {
		if(isThrust && !justLanded) {
			speed = TAKEOFF_SPEED;
			if(!takeoff) {
				EntityManager.getCamera().transistionMoveType(1);
				takeoff = true;
			}
		}
		if(justLanded && !isThrust) {
			justLanded = false;
		}
		super.setRadiusDistance(super.getRadiusDistance() + speed * DisplayManager.getFrameTimeSeconds());
		super.setPosition(Vector3f.add(super.getPlanet().getPosition(), Maths.scaledVector(super.getUpDirection(), super.getRadiusDistance()), null));
		if(super.getRadiusDistance() > super.getPlanet().getRadius() + MIN_PLANET_DISTANCE) {
			motionStage = 1;
			super.setPlanet(null);
			takeoff = false;
		}
	}
	
	private void land(boolean isThrust, float turnSpeedX, float turnSpeedY, float thrustPower) {
		super.setUpDirection(Maths.rotateVector(super.getUpDirection(), super.getBinormalDirection(), turnSpeedY*DisplayManager.getFrameTimeSeconds()));
		super.setForwardDirection(Maths.rotateVector(super.getForwardDirection(), super.getBinormalDirection(), turnSpeedY*DisplayManager.getFrameTimeSeconds()));
		super.setUpDirection(Maths.rotateVector(super.getUpDirection(), super.getForwardDirection(), turnSpeedX*DisplayManager.getFrameTimeSeconds()));
		super.setBinormalDirection(Maths.rotateVector(super.getBinormalDirection(), super.getForwardDirection(), turnSpeedX*DisplayManager.getFrameTimeSeconds()));
		if(isThrust) {
			Vector3f.add(landingVelocity, Maths.normScaledVector(super.getUpDirection(), thrustPower * DisplayManager.getFrameTimeSeconds()), landingVelocity);
		}
		Vector3f planetToRocket = Vector3f.sub(super.getCentrePosition(), super.getPlanet().getPosition(), null);
		float planetDistance = planetToRocket.length();
		Vector3f planetToRocketNorm = Maths.normScaledVector(planetToRocket, 1);
		landingPhi = (float) Math.asin(planetToRocketNorm.z);
		landingTheta = (float) Math.atan(planetToRocketNorm.y/planetToRocketNorm.x);
		if (planetToRocketNorm.x < 0) {
			landingTheta += Math.PI;
		}
		if (landingTheta < 0) {
			landingTheta = (float) (2*Math.PI + landingTheta);
		}
		float planetRadius = super.getPlanet().getOnPlanetHeight(landingTheta, landingPhi);
		float gravityEffect = super.getPlanet().getGravity();
		Vector3f.sub(landingVelocity, Maths.normScaledVector(planetToRocket, gravityEffect * DisplayManager.getFrameTimeSeconds()), landingVelocity);
		super.setPosition(Vector3f.add(super.getPosition(), Maths.scaledVector(landingVelocity, DisplayManager.getFrameTimeSeconds()), null));		
		if(planetDistance < planetRadius + super.getHeight()/2) {
			super.setToPlanet(landingTheta, landingPhi);
			motionStage = 0;
			speed = 0;
			justLanded = true;
			EntityManager.setUserMovingEntity(this, 0);
		}
		if(planetDistance > planetRadius + MIN_PLANET_DISTANCE) {
			motionStage = 1;
			speed = landingVelocity.length();
			super.setPlanet(null);
		}
	}
	
	private void spaceMove(boolean isThrust, float turnSpeedX, float turnSpeedY, float thrustPower, float airDrag) {
		if(isThrust) {
			speed += thrustPower * DisplayManager.getFrameTimeSeconds();
			if(speed > MAX_SPEED) {
				speed = MAX_SPEED;
			}
		}
		else {
			speed -= airDrag * DisplayManager.getFrameTimeSeconds();
			if(speed < 0) {
				speed = 0;
			}
		}
		//set new direction vectors based on velocity
		super.setUpDirection(Maths.rotateVector(super.getUpDirection(), super.getBinormalDirection(), turnSpeedY*DisplayManager.getFrameTimeSeconds()));
		super.setForwardDirection(Maths.rotateVector(super.getForwardDirection(), super.getBinormalDirection(), turnSpeedY*DisplayManager.getFrameTimeSeconds()));
		super.setUpDirection(Maths.rotateVector(super.getUpDirection(), super.getForwardDirection(), turnSpeedX*DisplayManager.getFrameTimeSeconds()));
		super.setBinormalDirection(Maths.rotateVector(super.getBinormalDirection(), super.getForwardDirection(), turnSpeedX*DisplayManager.getFrameTimeSeconds()));
		super.setPosition(Vector3f.add(super.getPosition(), Maths.normScaledVector(super.getUpDirection(), speed*DisplayManager.getFrameTimeSeconds()), null));
		for(Entity entity:EntityManager.getCollisionEntities()) {
			if(entity != this) {
				Vector3f vectorBetween = Vector3f.sub(super.getCentrePosition(), entity.getCentrePosition(), null);
				float absoluteRadius = entity.getAbsoluteRadius();
				float thisAbsoluteRadius = super.getAbsoluteRadius();
				if(vectorBetween.lengthSquared() < (absoluteRadius + thisAbsoluteRadius)*(absoluteRadius + thisAbsoluteRadius)) {
					Vector3f newCentrePosition = Vector3f.add(entity.getCentrePosition(), Maths.normScaledVector(vectorBetween, absoluteRadius + thisAbsoluteRadius + COLLISION_BUFFER), null);
					super.setPosition(Vector3f.sub(newCentrePosition, Maths.normScaledVector(super.getUpDirection(), thisAbsoluteRadius), null));
					this.speed = Maths.clampFloat(speed, 0, MAX_SPEED_COLLISION);
					entity.collideWith(this);
				}
			}
		}
		//check for approaching planets to go into landing mode
		for(Planet planet : PlanetManager.getScenePlanets()) {
			if(Maths.isSphericalCollision(super.getCentrePosition(), planet.getPosition(), super.getRadius(), planet.getRadius() + MIN_PLANET_DISTANCE)) {
				motionStage = 2;
				super.setPlanet(planet);
				landingVelocity = Maths.normScaledVector(super.getUpDirection(), speed);
			}
		}
	}
	
	public void shoot() {
		shooter.shoot(super.getUpDirection(), Vector3f.add(super.getCentrePosition(), Maths.normScaledVector(super.getUpDirection(), shootingRadius), null), speed);
	}
	
	public void reloadClip() {
		shooter.reloadClip();
	}

	public Shooter getShooter() {
		return shooter;
	}
	
	public Entity getPassenger() {
		return passenger;
	}

	public void setPassenger(Entity passenger) {
		this.passenger = passenger;
	}

	public void performClickedOnActions() {
		MenuManager.clearSceneMenus();
		MenuManager.createRocketMenu(this);
	}

	public int getMotionStage() {
		return motionStage;
	}
	
}
