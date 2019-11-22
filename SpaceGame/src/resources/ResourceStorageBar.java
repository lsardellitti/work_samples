package resources;

import org.lwjgl.util.vector.Vector2f;

import fontMeshCreator.GUIText;
import guis.HealthBar;
import menus.MenuLayer;

public class ResourceStorageBar extends HealthBar{
	
	private ResourceStorage storage;
	private Resource resource;
	private GUIText amountDisplay;
	private int resourceID;

	public ResourceStorageBar(int texture, int healthTexture, MenuLayer layer, Vector2f relativePos, Vector2f relativeSize, ResourceStorage storage, Resource resource) {
		super(texture, healthTexture, layer, relativePos, relativeSize);
		this.storage = storage;
		this.resource = resource;
		this.resourceID = resource.getResourceID();
		this.amountDisplay = new GUIText(resource.getUnitAmount(storage.getResourceAmounts()[resourceID]) + "/" + resource.getUnitAmount(storage.getResourceStorageSize()), 6, this, new Vector2f(-1,-0.3f), 1, true);
	}
	
	public void update() {
		super.setHealthFactor(storage.getResourceAmounts()[resourceID]/(float)(storage.getResourceStorageSize()));
		amountDisplay.setTextString(resource.getUnitAmount(storage.getResourceAmounts()[resourceID]) + "/" + resource.getUnitAmount(storage.getResourceStorageSize()));
	}

}
