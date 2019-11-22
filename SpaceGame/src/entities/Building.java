package entities;

import models.TexturedModel;
import planets.Planet;

public class Building extends Entity{

	public Building(TexturedModel model, float scale, Planet planet, float theta, float phi) {
		super(model, scale, planet);
		super.setToPlanet(theta, phi);
		EntityManager.makeEntityClickable(this);
		EntityManager.makeEntityCollision(this);
	}

}
