package resources;

import org.lwjgl.util.vector.Vector3f;

import entities.Building;
import entities.Entity;
import menus.MenuManager;
import models.TexturedModel;
import planets.Planet;
import popups.Popup;
import transitions.Transition;

public class ResourceCollector extends Building{
	
	private static final float COLLECTION_DISTANCE = 5;
	
	private float collectionRate;
	private Transition collectingTransition = new Transition();
	
	public ResourceCollector(TexturedModel model, float scale, Planet planet, float theta, float phi, Resource resource, float collectionRate, int maxCapacity) {
		super(model, scale, planet, theta, phi);
		this.collectionRate = collectionRate;
		super.setResourceTypeHeld(resource);
		super.setResourceStorageSize(maxCapacity);
		collectingTransition.startTransition(maxCapacity/collectionRate);		
	}
	
	public int getCurrentResourceAmount() {
		return (int) (super.getResourceAmountHeld() + collectingTransition.getCurrentTime()*(super.getResourceStorageSize()-super.getResourceAmountHeld()));
	}
	
	public String getCurrentResourceAmountString() {
		return super.getResourceTypeHeld().getUnitAmount((int) (super.getResourceAmountHeld() + collectingTransition.getCurrentTime()*(super.getResourceStorageSize()-super.getResourceAmountHeld())));
	}
	
	public boolean gatherResources(Entity gatherer) {
		if(Vector3f.angle(super.getUpDirection(), gatherer.getUpDirection()) * super.getRadiusDistance() <= super.getRadius() + gatherer.getRadius() + COLLECTION_DISTANCE) {
			if(gatherer.getResourceTypeHeld() == null || gatherer.getResourceTypeHeld().getResourceID() == super.getResourceTypeHeld().getResourceID()) {
				int storageAvailable = gatherer.getResourceStorageSize() - gatherer.getResourceAmountHeld();
				if(storageAvailable == 0) {
					new Popup("Resource Holding Full", "error");
					return false;
				}
				int currentResourceAmount = this.getCurrentResourceAmount();
				int gatherAmount = gatherer.getResourceCollectionSize();
				if(storageAvailable < gatherer.getResourceCollectionSize()) {
					gatherAmount = storageAvailable;
				}
				if(currentResourceAmount < gatherAmount) {
					super.setResourceAmountHeld(0);
					gatherer.addResourceAmountHeld(currentResourceAmount);
					new Popup("+" + getCurrentResourceAmountString() + " " + super.getResourceTypeHeld().getResourceName(), "collect");
				}else {
					super.setResourceAmountHeld(currentResourceAmount - gatherAmount);
					gatherer.addResourceAmountHeld(gatherAmount);
					new Popup("+" + super.getResourceTypeHeld().getUnitAmount(gatherAmount) + " " + super.getResourceTypeHeld().getResourceName(), "collect");
				}
				gatherer.setResourceTypeHeld(super.getResourceTypeHeld());
				collectingTransition.startTransition((super.getResourceStorageSize() - super.getResourceAmountHeld())/collectionRate);
				return true;
			}	
			else {
				new Popup("Wrong Resource", "error");
				return false;
			}
		}else {
			new Popup("TOO FAR AWAY", "error");
			return false;
		}
	}
	
	public void performClickedOnActions() {
		MenuManager.clearSceneMenus();
		MenuManager.createResourceCollectorMenu(this);
	}

	public float getCollectionRate() {
		return collectionRate;
	}

}
