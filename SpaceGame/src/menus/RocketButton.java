package menus;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.EntityManager;
import entities.Rocket;
import popups.Popup;

public class RocketButton extends Button{
	
	private static final float ENTER_DISTANCE = 10;
	
	private Rocket rocket;
	
	public RocketButton(int texture, MenuLayer layer, Rocket rocket, Vector2f relativePos, Vector2f relativeSize) {
		super(texture, layer, relativePos, relativeSize);
		this.rocket = rocket;
	}
	
	public void performClickedOnActions() {
		//if not already inside of the rocket, offer to enter
		if(EntityManager.getUserMovingEntity() != rocket) {
			if(Vector3f.angle(rocket.getUpDirection(), EntityManager.getUserMovingEntity().getUpDirection()) * rocket.getRadiusDistance() <= rocket.getRadius() + EntityManager.getUserMovingEntity().getRadius() + ENTER_DISTANCE) {
				rocket.setPassenger(EntityManager.getUserMovingEntity());
				EntityManager.getUserMovingEntity().destroy();
				EntityManager.setUserMovingEntity(rocket, 0);
				EntityManager.getCamera().setPitchToMax();
			}
			else {
				new Popup("TOO FAR AWAY", "error");
			}
		//only exit rocket if on a planet
		}else if(rocket.getPlanet() != null) {
			rocket.getPassenger().setPlanet(rocket.getPlanet());
			rocket.getPassenger().setToPlanet(rocket.calculateTheta(), rocket.calculatePhi());
			EntityManager.makeEntityClickable(rocket.getPassenger());
			EntityManager.makeEntityCollision(rocket.getPassenger());
			EntityManager.addEntityToScene(rocket.getPassenger());
			EntityManager.setUserMovingEntity(rocket.getPassenger(), 0);
			rocket.setPassenger(null);
		}else {
			new Popup("NOT POSSIBLE","error");
		}
		MenuManager.removeMenuFromScene(this.layer.getMenu());
	}

}
