package shooting;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fontMeshCreator.GUIText;
import fontRendering.Fonts;
import models.TexturedModel;
import popups.PopupManager;
import toolbox.Maths;
import transitions.Transition;

public class Shooter {
	
	private static final float POPUP_DELAY = 0.5f;
	
	private TexturedModel bullet;
	private Transition recharge = new Transition();
	private Transition reload = new Transition();
	private Transition popupWait = new Transition();
	private float fireRecharge;
	private float reloadTime;
	private float bulletSize;
	private float shootSpeed;
	private float bulletDistance;
	private float damage;
	private int maxAmmo;
	private int clipSize;
	private int ammoCount;
	private int clipCount;
	private GUIText ammoDisplay = new GUIText("", 2, Fonts.candara, new Vector2f(0.1f,0.9f), 1, false);
	private GUIText reloadMessage = new GUIText("Press r to Reload", 2, Fonts.candara, new Vector2f(0.0f,0.75f), 1, true);
	
	private boolean reloading;
	
	public Shooter(TexturedModel bullet, float bulletSize, float shootSpeed, float bulletDistance, float fireRecharge, int maxAmmo, int clipSize, float reloadTime, float damage) {
		this.bullet = bullet;
		this.bulletSize = bulletSize;
		this.shootSpeed = shootSpeed;
		this.bulletDistance = bulletDistance;
		this.fireRecharge = fireRecharge;
		this.maxAmmo = maxAmmo;
		this.ammoCount = maxAmmo;
		this.clipSize = clipSize;
		this.clipCount = clipSize;
		this.reloadTime = reloadTime;
		this.damage = damage;
		this.ammoDisplay = new GUIText("", 3, Fonts.candara, new Vector2f(0.01f,0.85f), 1, false);
		ammoDisplay.setColour(1, 1, 1);
		reloadMessage.setColour(1,0,0);
	}
	
	public void shoot(Vector3f shooterVelocity, Vector3f shooterPosition, float shooterSpeed) {
		if(recharge.isFinished()) {
			if(reload.isFinished()) {
				if(clipCount > 0) {
					new Bullet(bullet, bulletSize, Maths.normScaledVector(shooterVelocity, shooterSpeed + shootSpeed), shooterPosition, bulletDistance, damage);
					clipCount -= 1;
				}else {
					if(popupWait.isFinished()) {
						//new Popup("RELOAD", "error");
						popupWait.startTransition(POPUP_DELAY);
					}
				}
			}else {
				if(reloading) {
					if(popupWait.isFinished()) {
						//new Popup("RELOADING", "error");
						popupWait.startTransition(POPUP_DELAY);
					}
				}
			}
			recharge.startTransition(fireRecharge);
		}
	}
	
	public void reloadClip() {
		if(reload.isFinished()) {
			int reloadAmount = clipSize - clipCount;
			if(reloadAmount != 0) {
				if(ammoCount != 0) {
					if(reloadAmount <= ammoCount) {
						clipCount = clipSize;
						ammoCount -= reloadAmount;
					}else {
						clipCount += ammoCount;
						ammoCount = 0;
					}
					reloading = true;
				}else {
					if(popupWait.isFinished()) {
						//new Popup("Out of Ammo", "error");
						reloading = false;
						popupWait.startTransition(POPUP_DELAY);
					}
					return;
				}
			}else {
				if(popupWait.isFinished()) {
					//new Popup("Clip Full", "error");
					reloading = false;
					popupWait.startTransition(POPUP_DELAY);
				}
				return;
			}
			reload.startTransition(reloadTime);
		}
	}
	
	//make more complicated later
	public void reloadStock() {
		ammoCount = maxAmmo;
	}
	
	public void displayAmmo() {
		if(!reload.isFinished()) {
			ammoDisplay.setColour(0,0,1);
		}else if(clipCount == 0){
			ammoDisplay.setColour(1,0,0);
			reloadMessage.load(PopupManager.getTextMaster());
		}else {
			ammoDisplay.setColour(1,1,1);
		}
		ammoDisplay.setTextString(Integer.toString(clipCount) +" - " + Integer.toString(ammoCount));
		ammoDisplay.load(PopupManager.getTextMaster());
	}

	public int getClipCount() {
		return clipCount;
	}

}
