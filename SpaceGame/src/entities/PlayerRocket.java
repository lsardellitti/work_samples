package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import fontMeshCreator.GUIText;
import fontRendering.Fonts;
import models.TexturedModel;
import planets.Planet;
import popups.PopupManager;

public class PlayerRocket extends Rocket{
	
	private float thrustPower = 10;
	private float airDrag = 5;
	private float turnSpeed = 1.2f;
	
	private boolean isThrust = false;
	private float currentTurnSpeedX = 0;
	private float currentTurnSpeedY = 0;
	private GUIText instructionText = new GUIText("Press M for thrust, Space to shoot", 2, Fonts.candara, new Vector2f(0,0.85f), 1, true);
	
	public PlayerRocket(TexturedModel model, float scale, Planet planet, float theta, float phi) {
		super(model, scale, planet, theta, phi);
		instructionText.setColour(1,1,1);
	}
	
	public void userMove() {//read user key inputs then call normal rocket motion
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			super.shoot();
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_R)) {
			super.reloadClip();
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_M)) {
			isThrust = true;
		}
		else {
			isThrust = false;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			currentTurnSpeedX = turnSpeed;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
				currentTurnSpeedX = -turnSpeed;
		}else {
			currentTurnSpeedX = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			currentTurnSpeedY = turnSpeed;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
				currentTurnSpeedY = -turnSpeed;
		}else {
			currentTurnSpeedY = 0;
		}
		super.getShooter().displayAmmo();
		super.stagedMotion(isThrust, currentTurnSpeedX, currentTurnSpeedY, thrustPower, airDrag);
		if(super.getMotionStage() == 0) {
			instructionText.load(PopupManager.getTextMaster());
		}
	}

}
