package menus;

import org.lwjgl.util.vector.Vector2f;

import guis.GuiTexture;

public class Scrollbar extends GuiTexture{
	
	private static final float WIDTH = 0.04f;
	private static final float X_POSITION = 0.95f;

	public Scrollbar(int texture, MenuLayer layer, float height) {
		super(texture, layer, new Vector2f(X_POSITION,1 - height), new Vector2f(WIDTH, height));
		super.setStationary(true);
	}
	
	public void update() {
		float currentScroll = super.getLayer().getScrollPosition();
		float totalScroll = super.getLayer().getTotalScroll();
		float height = super.getScale().y;
		float menuY = super.getLayer().getMenu().getPosition().y;
		float menuHeight = super.getLayer().getMenu().getSize().y;
		super.getPosition().setY(menuY + (menuHeight - height + (2*menuHeight - 2*height)*(currentScroll/totalScroll)));
	}

}
