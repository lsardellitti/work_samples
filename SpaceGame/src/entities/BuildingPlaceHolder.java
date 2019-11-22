package entities;

import menus.MenuManager;
import models.StandardModels;
import planets.Planet;

public class BuildingPlaceHolder extends Entity{
	
	private float theta;
	private float phi;

	public BuildingPlaceHolder(Planet planet, float theta, float phi) {
		super(StandardModels.crate, 0.01f, planet);
		EntityManager.makeEntityClickable(this);
		this.theta = theta;
		this.phi = phi;
		super.setToPlanet(theta, phi);
	}
	
	public void performClickedOnActions() {
		MenuManager.clearSceneMenus();
		MenuManager.createBuldingPlaceHolderMenu(this);
	}

	public float getTheta() {
		return theta;
	}

	public float getPhi() {
		return phi;
	}

}
