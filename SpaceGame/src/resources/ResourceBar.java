package resources;

import org.lwjgl.util.vector.Vector2f;

import fontMeshCreator.GUIText;
import guis.HealthBar;
import menus.MenuLayer;

public class ResourceBar extends HealthBar{
	
	private ResourceCollector collector;
	private GUIText amountDisplay;

	public ResourceBar(int texture, int healthTexture, MenuLayer layer, Vector2f relativePos, Vector2f relativeSize, ResourceCollector collector) {
		super(texture, healthTexture, layer, relativePos, relativeSize);
		super.setHealthFactor(collector.getCurrentResourceAmount()/(float)(collector.getResourceStorageSize()));
		this.collector = collector;
		this.amountDisplay = new GUIText(collector.getCurrentResourceAmountString() + "/" + collector.getResourceTypeHeld().getUnitAmount(collector.getResourceStorageSize()), 6, this, new Vector2f(-1,-0.3f), 1, true);
	}
	
	public void update() {
		super.setHealthFactor(collector.getCurrentResourceAmount()/(float)(collector.getResourceStorageSize()));
		amountDisplay.setTextString(collector.getCurrentResourceAmountString() + "/" + collector.getResourceTypeHeld().getUnitAmount(collector.getResourceStorageSize()));
	}

}
