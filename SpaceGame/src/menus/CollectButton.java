package menus;

import org.lwjgl.util.vector.Vector2f;

import entities.EntityManager;
import resources.ResourceCollector;

public class CollectButton extends Button{
	
	private ResourceCollector collector;

	public CollectButton(int texture, MenuLayer layer, ResourceCollector collector, Vector2f relativePos, Vector2f relativeSize) {
		super(texture, layer, relativePos, relativeSize);
		this.collector = collector;
	}
	
	public void performClickedOnActions() {
		if(EntityManager.getUserMovingEntity() != null) {
			collector.gatherResources(EntityManager.getUserMovingEntity());
		}
	}

}
