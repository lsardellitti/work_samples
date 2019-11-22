package entities;

import org.lwjgl.input.Keyboard;

import menus.MenuManager;
import models.TexturedModel;

public class Player extends Entity{
	
	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 2;
	private static final float JUMP_POWER = 20;
	
	private float forwardSpeed;
	private float turningSpeed;
	private boolean isJumping;
	
	public Player(TexturedModel model, float scale, int resourceStorage, int resourceCollection) {
		super(model, scale);
		super.setResourceStorageSize(resourceStorage);
		super.setResourceCollectionSize(resourceCollection);
		super.setHealth(1000);
		super.setMaxHealth(1000);
		super.setDestructible(true);
	}
	
	public void userMove() {//add in user key inputs then call normal move on planet for entities
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.forwardSpeed = RUN_SPEED;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.forwardSpeed = -RUN_SPEED;
		}else {
			this.forwardSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.turningSpeed = -TURN_SPEED;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.turningSpeed = TURN_SPEED;
		}else {
			this.turningSpeed = 0;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			isJumping = true;
		}
		else {
			isJumping = false;
		}
		
		super.moveOnPlanet(forwardSpeed, turningSpeed, JUMP_POWER, isJumping);
	}	
	
	public void performClickedOnActions() {
		MenuManager.clearSceneMenus();
		MenuManager.createPlayerMenu(this);
	}
}
