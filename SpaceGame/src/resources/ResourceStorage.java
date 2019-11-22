package resources;

import org.lwjgl.util.vector.Vector3f;

import entities.Building;
import entities.Entity;
import menus.MenuManager;
import models.TexturedModel;
import planets.Planet;
import popups.Popup;

public class ResourceStorage extends Building{
	
	private static final float COLLECTION_DISTANCE = 5;
	
	private int[] resourceAmounts = new int[Resource.getResourceNames().size()];

	public ResourceStorage(TexturedModel model, float scale, Planet planet, float theta, float phi, int maxCapacity) {
		super(model, scale, planet, theta, phi);
		super.setResourceStorageSize(maxCapacity);
	}
	
	public void storeResources(Entity storer) {
		if(storer.getResourceTypeHeld() != null) {
			if(Vector3f.angle(super.getUpDirection(), storer.getUpDirection()) * super.getRadiusDistance() <= super.getRadius() + storer.getRadius() + COLLECTION_DISTANCE) {
				int resourceID = storer.getResourceTypeHeld().getResourceID();
				if(storer.getResourceAmountHeld() <= super.getResourceStorageSize() - resourceAmounts[resourceID]) {
					new Popup(storer.getUnitResourceAmountHeld() + " " + storer.getResourceTypeHeld().getResourceName() + " Stored", "store");
					resourceAmounts[resourceID] += storer.getResourceAmountHeld();
					storer.setResourceAmountHeld(0);
					storer.setResourceTypeHeld(null);
				}else {
					int storingAmount = super.getResourceStorageSize() - resourceAmounts[resourceID];
					if(storingAmount == 0) {
						new Popup("Storage Full", "error");
					}else {
						new Popup(storer.getResourceTypeHeld().getUnitAmount(storingAmount) + " " + storer.getResourceTypeHeld().getResourceName() + " Stored", "store");
					}
					resourceAmounts[resourceID] = super.getResourceStorageSize();
					storer.addResourceAmountHeld(-storingAmount);
				}
			}else {
				new Popup("Too Far Away", "error");
			}
		}else {
			new Popup("No Resource Held", "error");
		}
	}
	
	public void useResource(Resource resource, int amount) {
		int resourceID = resource.getResourceID();
		if(resourceAmounts[resourceID] >= amount) {
			resourceAmounts[resourceID] -= amount;
		}
	}
	
	public void performClickedOnActions() {
		MenuManager.clearSceneMenus();
		MenuManager.createResourceStorageMenu(this);
	}

	public int[] getResourceAmounts() {
		return resourceAmounts;
	}

}
