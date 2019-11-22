package guis;

import org.lwjgl.util.vector.Vector2f;

import entities.Entity;
import menus.MenuLayer;

public class HealthBar extends GuiTexture{
	
	private Entity entity;

	public HealthBar(int texture, int healthTexture, MenuLayer layer, Vector2f relativePos, Vector2f relativeSize) {
		super(texture, healthTexture, layer, relativePos, relativeSize);
	}
	
	public HealthBar(int texture, int healthTexture, MenuLayer layer, Vector2f relativePos, Vector2f relativeSize, Entity entity) {
		super(texture, healthTexture, layer, relativePos, relativeSize);
		this.entity = entity;
		if(entity.getMaxHealth() != 0) {
			super.setHealthFactor(entity.getHealth() / entity.getMaxHealth());
		}else {
			super.setHealthFactor(1);
		}
	}
	
	public HealthBar(int texture, int healthTexture, Vector2f relativePos, Vector2f relativeSize, Entity entity) {
		super(texture, healthTexture, relativePos, relativeSize);
		this.entity = entity;
		if(entity.getMaxHealth() != 0) {
			super.setHealthFactor(entity.getHealth() / entity.getMaxHealth());
		}else {
			super.setHealthFactor(1);
		}
	}
	
	public void update() {
		if(entity.getMaxHealth() != 0) {
			super.setHealthFactor(entity.getHealth() / entity.getMaxHealth());
		}else {
			super.setHealthFactor(1);
		}
	}

}
