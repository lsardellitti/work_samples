package menus;

import org.lwjgl.util.vector.Vector2f;

public class Tab extends Button{
	
	private MenuLayer tabLayer;

	public Tab(int texture, MenuLayer layer, Vector2f relativePos, Vector2f relativeSize) {
		super(texture, layer.getMenu().getTabLayer(), relativePos, relativeSize);
		this.tabLayer = layer;
	}
	
	public void performClickedOnActions() {
		tabLayer.getMenu().setActiveLayer(tabLayer);
	}

}
