package menus;

import org.lwjgl.util.vector.Vector2f;

import entities.Building;
import entities.BuildingPlaceHolder;
import models.StandardModels;
import resources.Resource;
import resources.ResourceCollector;
import resources.ResourceStorage;

public class BuildingCreator extends Button{
	
	BuildingPlaceHolder placeholder;
	String type;

	public BuildingCreator(int texture, MenuLayer layer, Vector2f relativePos, Vector2f relativeSize, BuildingPlaceHolder placeholder, String type) {
		super(texture, layer, relativePos, relativeSize);
		this.type = type;
		this.placeholder = placeholder;
	}
	
	public void performClickedOnActions() {//based on type, create an entity in place of this building
		if(type == "tree") {
			new Building(StandardModels.tree, 5, placeholder.getPlanet(), placeholder.getTheta(), placeholder.getPhi());
		}
		else if(type == "bunny") {
			new Building(StandardModels.bunny, 1, placeholder.getPlanet(), placeholder.getTheta(), placeholder.getPhi());
		}
		else if(type == "waterCollector") {
			new ResourceCollector(StandardModels.well, 7, placeholder.getPlanet(), placeholder.getTheta(), placeholder.getPhi(), new Resource("Water"), 1000, 10000);
		}
		else if(type == "resourceStorage") {
			new ResourceStorage(StandardModels.storage, 10, placeholder.getPlanet(), placeholder.getTheta(), placeholder.getPhi(), 10000);
		}
		placeholder.destroy();
		MenuManager.removeMenuFromScene(this.layer.getMenu());
	}

}
