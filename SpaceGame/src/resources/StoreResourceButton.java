package resources;

import org.lwjgl.util.vector.Vector2f;

import entities.EntityManager;
import menus.Button;
import menus.MenuLayer;

public class StoreResourceButton extends Button{
	
	private ResourceStorage storage;

	public StoreResourceButton(int texture, MenuLayer layer, Vector2f relativePos, Vector2f relativeSize, ResourceStorage storage) {
		super(texture, layer, relativePos, relativeSize);
		this.storage = storage;
	}
	
	public void performClickedOnActions() {
		if(EntityManager.getUserMovingEntity() != null) {
			storage.storeResources(EntityManager.getUserMovingEntity());
		}
	}

}
