package enemies;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.EntityManager;
import models.TexturedModel;
import planets.Planet;
import planets.PlanetManager;
import renderEngine.DisplayManager;
import toolbox.Maths;

public class Asteroid extends Entity{
	
	private static final float MIN_PLANET_DISTANCE = 100;
	private static final float MIN_SPLIT_ANGLE = 0.1f;
	private static final float MAX_SPLIT_ANGLE = 0.4f;
	
	private int layer;
	private float speed;
	private int deathDamage;

	public Asteroid(TexturedModel model, float scale, int layer, Vector3f centrePosition, Vector3f forwardDirection, float speed, int deathDamage) {
		super(model, scale);
		super.setAllDirections(forwardDirection);
		this.layer = layer;
		this.speed = speed;
		this.deathDamage = deathDamage;
		super.setCentrePosition(centrePosition);
		super.setUseBasicPopupDirection(true);
		super.setHealth(100);
		super.setMaxHealth(100);
		super.setDestructible(true);
		EntityManager.makeEntityCollision(this);
		EntityManager.makeEntityMoving(this);
	}
	
	public void move() {
		//set new position and direction based on velocity
		super.setCentrePosition(Vector3f.add(super.getCentrePosition(), Maths.normScaledVector(super.getForwardDirection(), speed * DisplayManager.getFrameTimeSeconds()), null));	
		super.setBinormalDirection(Maths.rotateVector(super.getBinormalDirection(), super.getForwardDirection(), speed * DisplayManager.getFrameTimeSeconds()/10));
		super.setUpDirection(Maths.rotateVector(super.getUpDirection(), super.getForwardDirection(), speed * DisplayManager.getFrameTimeSeconds()/10));
		if(super.getPlanet() == null) {
			//has it come close to a planet
			for(Planet planet : PlanetManager.getScenePlanets()) {
				if(Maths.isSphericalCollision(super.getCentrePosition(), planet.getPosition(), super.getRadius(), planet.getRadius() + MIN_PLANET_DISTANCE)) {
					super.setPlanet(planet);
				}
			}
		}else {//wait until it collides with the planet
			Vector3f planetToAsteroid = Vector3f.sub(super.getCentrePosition(), super.getPlanet().getPosition(), null);
			float planetDistance = planetToAsteroid.length();
			Vector3f planetToAsteroidNorm = Maths.normScaledVector(planetToAsteroid, 1);
			float phi = (float) Math.asin(planetToAsteroidNorm.z);
			float theta = (float) Math.atan(planetToAsteroidNorm.y/planetToAsteroidNorm.x);
			if (planetToAsteroidNorm.x == 0) {
				theta = 0;
			}
			if (planetToAsteroidNorm.x < 0) {
				theta += Math.PI;
			}
			if (theta < 0) {
				theta = (float) (2*Math.PI + theta);
			}
			float planetRadius = super.getPlanet().getOnPlanetHeight(theta, phi);
			if(planetDistance < planetRadius + super.getRadius()) {
				destroyFully();
				//finish interaction with planet
			}
			if(planetDistance > planetRadius + MIN_PLANET_DISTANCE + super.getRadius()) {
				super.setPlanet(null);
			}
		}
		//check for collisions with all collision entities
		for(Entity entity : EntityManager.getCollisionEntities()) {
			if(entity != this) {
				if(Maths.isSphericalCollision(super.getCentrePosition(), entity.getCentrePosition(), super.getRadius(), entity.getRadius())) {
					entity.takeDamage(deathDamage);
					destroy();
				}
			}
		}
	}
	
	public void collideWith(Entity entity) {
		entity.takeDamage(deathDamage, super.getScale()*10);
		destroy();
	}
	
	public void destroy() {//when destroyed split into two smaller asteroids
		if(layer > 1) {
			split();
		}
		EntityManager.addRemovbleEntity(this);
	}
	
	public void destroyFully() {
		EntityManager.addRemovbleEntity(this);
	}
	
	private void split() {//split at random angles, half the size new asteroids
		Vector3f rotationAxis = Maths.rotateVector(super.getBinormalDirection(), super.getForwardDirection(), (float) (Math.random() * Math.PI * 2));
		Vector3f positionAxis = Vector3f.cross(rotationAxis, super.getForwardDirection(), null);
		float rotationAngle = (float) (Math.random() * (MAX_SPLIT_ANGLE - MIN_SPLIT_ANGLE) + MIN_SPLIT_ANGLE);
		Vector3f forward1 = Maths.rotateVector(super.getForwardDirection(), rotationAxis, rotationAngle);
		Vector3f forward2 = Maths.rotateVector(super.getForwardDirection(), rotationAxis, -rotationAngle);
		new Asteroid(super.getModel(), super.getScale()/2, layer - 1, Vector3f.add(super.getCentrePosition(), Maths.normScaledVector(positionAxis, super.getRadius()), null), forward1, speed*1.1f, deathDamage/2);
		new Asteroid(super.getModel(), super.getScale()/2, layer - 1, Vector3f.add(super.getCentrePosition(), Maths.normScaledVector(Maths.negateVector(positionAxis), super.getRadius()), null), forward2, speed*1.1f, deathDamage/2);
	}

}
