package guis;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import menus.MenuLayer;
import toolbox.Maths;

public class GuiTexture {
	
	private int texture;
	private Vector2f position;
	private Vector2f scale;
	protected MenuLayer layer;
	private boolean stationary;
	private float translucnecy = 1;
	private float rotation;
	
	private int healthTexture;
	private boolean healthMap;
	private float healthFactor;
	
	public GuiTexture(int texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}
	
	public GuiTexture(int texture, MenuLayer layer, Vector2f relativePos, Vector2f relativeSize) {
		this.texture = texture;
		this.position = Vector2f.add(Maths.scaledComponentwiseVector(relativePos, layer.getMenu().getSize()), layer.getMenu().getPosition(), null);
		this.scale = Maths.scaledComponentwiseVector(relativeSize, layer.getMenu().getSize());
		this.layer = layer;
		layer.addGui(this);
	}
	
	public GuiTexture(int texture, int healthTexture, MenuLayer layer, Vector2f relativePos, Vector2f relativeSize) {
		this.texture = texture;
		this.healthTexture = healthTexture;
		this.healthMap = true;
		this.position = Vector2f.add(Maths.scaledComponentwiseVector(relativePos, layer.getMenu().getSize()), layer.getMenu().getPosition(), null);
		this.scale = Maths.scaledComponentwiseVector(relativeSize, layer.getMenu().getSize());
		this.layer = layer;
		layer.addGui(this);
	}
	
	public GuiTexture(int texture, int healthTexture, Vector2f position, Vector2f size) {
		this.texture = texture;
		this.healthTexture = healthTexture;
		this.healthMap = true;
		this.position = position;
		this.scale = size;
	}
	
	public void performClickedOnActions() {
		
	}
	
	public void update() {
		
	}
	
	public boolean isMouseOver() {
		float x = (2.0f * Mouse.getX()) / Display.getWidth() - 1f;
		float y = (2.0f * Mouse.getY()) / Display.getHeight() - 1f;
		if(x < this.position.x + this.scale.x && x > this.position.x - this.scale.x && y < this.position.y + this.scale.y && y > this.position.y - this.scale.y) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void verticalScroll(float dy) {
		if(!stationary) {
			this.position.y += dy;
		}
	}

	public MenuLayer getLayer() {
		return layer;
	}

	public int getTexture() {
		return texture;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}

	public float getHealthFactor() {
		return healthFactor;
	}

	public void setHealthFactor(float healthFactor) {
		this.healthFactor = healthFactor;
	}

	public int getHealthTexture() {
		return healthTexture;
	}

	public boolean hasHealthMap() {
		return healthMap;
	}

	public boolean isStationary() {
		return stationary;
	}

	public void setStationary(boolean stationary) {
		this.stationary = stationary;
	}

	public float getTranslucnecy() {
		return translucnecy;
	}

	public void setTranslucnecy(float translucnecy) {
		this.translucnecy = translucnecy;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public void setScale(Vector2f scale) {
		this.scale = scale;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
		
}
