package menus;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import fontMeshCreator.FontType;
import guis.GuiTexture;
import renderEngine.DisplayManager;

public class Menu {
	
	private Vector2f position;
	private Vector2f size;
	private FontType font;
	private List<MenuLayer> layers = new ArrayList<MenuLayer>();
	private MenuLayer baseLayer;
	private MenuLayer activeLayer;
	private MenuLayer tabLayer;
	private int topPixel;
	private int bottomPixel;
	private int titleTopPixel;
	
	public Menu(Vector2f position, Vector2f size, FontType font) {
		this.position = position;
		this.size = size;
		this.font = font;
		this.tabLayer = new MenuLayer(this);
		this.topPixel = (int) ((position.y/2 + 0.5f + size.y/2)*DisplayManager.getHeight());
		this.bottomPixel = (int) ((position.y/2 + 0.5f - size.y/2)*DisplayManager.getHeight());
		this.titleTopPixel = topPixel;
	}
	
	public void setTitleHeight(float height) {
		titleTopPixel = (int) (topPixel - height*size.y*DisplayManager.getHeight());
	}
	
	public void addLayer(MenuLayer layer) {
		activeLayer = layer;
		layers.add(layer);
	}
	
	public void removeLayer(MenuLayer layer) {
		layers.remove(layer);
	}
	
	public boolean isMouseOver() {
		float x = (2.0f * Mouse.getX()) / Display.getWidth() - 1f;
		float y = (2.0f * Mouse.getY()) / Display.getHeight() - 1f;
		if(x < this.position.x + this.size.x && x > this.position.x - this.size.x && y < this.position.y + this.size.y && y > this.position.y - this.size.y) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void update() {
		for(MenuLayer layer:layers) {
			for(GuiTexture gui:layer.getGuis()) {
				gui.update();
			}
		}
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public Vector2f getSize() {
		return size;
	}

	public void setSize(Vector2f size) {
		this.size = size;
	}

	public FontType getFont() {
		return font;
	}

	public List<MenuLayer> getLayers() {
		return layers;
	}

	public MenuLayer getTabLayer() {
		return tabLayer;
	}

	public MenuLayer getBaseLayer() {
		return baseLayer;
	}

	public MenuLayer getActiveLayer() {
		return activeLayer;
	}

	public void setActiveLayer(MenuLayer activeLayer) {
		this.activeLayer = activeLayer;
	}

	public void setBaseLayer(MenuLayer baseLayer) {
		this.baseLayer = baseLayer;
	}

	public int getTopPixel() {
		return topPixel;
	}

	public int getBottomPixel() {
		return bottomPixel;
	}

	public int getTitleTopPixel() {
		return titleTopPixel;
	}
	
}
