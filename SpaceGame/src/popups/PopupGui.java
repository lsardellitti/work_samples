package popups;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Entity;
import guis.GuiTexture;
import renderEngine.MasterRenderer;
import toolbox.Maths;
import transitions.Transition;

public class PopupGui {
	
	private static final float STAY_TIME = 1.5f;
	
	private GuiTexture gui;
	private Transition popupTransition = new Transition();
	private Entity entity;
	private Vector2f initialScale;
	private boolean skipFadeIn;
	
	public PopupGui(GuiTexture gui) {
		this.gui = gui;
		this.initialScale = Maths.copyVector(gui.getScale());
		popupTransition.startTransition(STAY_TIME);
		PopupManager.addActivePopupGui(this);
	}
	
	public PopupGui(GuiTexture gui, Entity entity) {
		this.gui = gui;
		this.initialScale = Maths.copyVector(gui.getScale());
		this.entity = entity;
		popupTransition.startTransition(STAY_TIME);
		PopupManager.addActivePopupGui(this);
	}
	
	public void update() {
		//update position and size of the gui, if it is linked to an entity
		//father away the object is, the smaller the gui will be
		if(entity != null) {
			if(!entity.isUseBasicPopupDirection()) {
				Vector4f totalGLPos = Maths.getGLCoords(Vector3f.add(entity.getCentrePosition(), Maths.normScaledVector(entity.getUpDirection(), entity.getHeight()), null), PopupManager.getCamera().getViewMatrix(), MasterRenderer.getProjectionMatrix());
				gui.setPosition(new Vector2f(totalGLPos.x/totalGLPos.w, totalGLPos.y/totalGLPos.w));
				gui.setScale(Maths.normScaledVector(initialScale, 10/totalGLPos.w));
				Vector4f totalGLUpDir = Maths.getGLCoords(entity.getUpDirection(), PopupManager.getCamera().getViewMatrix(), MasterRenderer.getProjectionMatrix());
				if(totalGLUpDir.x > 0) {
					gui.setRotation(Vector2f.angle(new Vector2f(totalGLUpDir.x, totalGLUpDir.y), new Vector2f(0,-1)));
				}else {
					gui.setRotation(-Vector2f.angle(new Vector2f(totalGLUpDir.x, totalGLUpDir.y), new Vector2f(0,-1)));
				}
			}else {
				Vector4f totalGLPos = Maths.getGLCoords(entity.getCentrePosition(), PopupManager.getCamera().getViewMatrix(), MasterRenderer.getProjectionMatrix());
				gui.setPosition(new Vector2f(totalGLPos.x/totalGLPos.w, (totalGLPos.y + entity.getHeight())/totalGLPos.w));
				gui.setScale(Maths.normScaledVector(initialScale, Maths.clampFloat(0.15f-totalGLPos.w/7000, 0, 0.15f)));
			}
		}
		gui.setTranslucnecy(fadeInFunction(popupTransition.getCurrentTime()));
		if(popupTransition.getCurrentTime() >= 1) {
			PopupManager.addFinishedPopupGui(this);
		}	
	}

	public GuiTexture getGui() {
		return gui;
	}
	
	private float fadeInFunction(float time) {
		if(time >= 0.5 || !skipFadeIn) {
		return Maths.clampFloat(-5*time*(time-1), 0, 1);
		}else {
			return 1;
		}
	}

	public Entity getEntity() {
		return entity;
	}

	public void setSkipFadeIn(boolean skipFadeIn) {
		this.skipFadeIn = skipFadeIn;
	}

}
