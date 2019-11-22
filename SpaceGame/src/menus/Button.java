package menus;

import org.lwjgl.util.vector.Vector2f;

import guis.GuiTexture;

//general button class, used as a prototype for all types of buttons

public class Button extends GuiTexture{

	public Button(int texture, Vector2f position, Vector2f scale) {
		super(texture, position, scale);
	}
	
	public Button(int texture, MenuLayer layer, Vector2f relativePos, Vector2f relativeSize) {
		super(texture, layer, relativePos, relativeSize);
	}
	
	public void performClickedOnActions() {
		MenuManager.removeMenuFromScene(this.layer.getMenu());
	}

}
