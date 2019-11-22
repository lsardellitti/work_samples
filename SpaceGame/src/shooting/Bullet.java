package shooting;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.EntityManager;
import models.TexturedModel;
import renderEngine.DisplayManager;
import toolbox.Maths;

public class Bullet extends Entity{
	
	private Vector3f velocity;
	private float speed;
	private float distance;
	private float lifeDistance;
	private float damage;

	public Bullet(TexturedModel model, float scale, Vector3f velocity, Vector3f position, float lifeDistance, float damage) {
		super(model, scale);
		super.setPosition(position);
		super.setUpDirection(velocity);
		EntityManager.makeEntityMoving(this);
		this.velocity = velocity;
		this.speed = velocity.length();
		this.lifeDistance = lifeDistance;
		this.damage = damage;
	}
	
	public void move() {
		float frameTime = DisplayManager.getFrameTimeSeconds();
		super.setPosition(Vector3f.add(super.getPosition(), Maths.normScaledVector(velocity, frameTime*speed), null));
		distance += speed * frameTime;
		if(distance >= lifeDistance) {
			super.destroy();
		}
		for(Entity entity : EntityManager.getCollisionEntities()) {
			if(Maths.isSphericalCollision(super.getCentrePosition(), entity.getCentrePosition(), super.getRadius(), entity.getRadius())) {
				entity.takeDamage(damage);
				super.destroy();
			}
		}
	}

}
