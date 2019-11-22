package mouseManager;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.EntityManager;
import gameManager.GameStateManager;
import guis.GuiTexture;
import menus.Menu;
import menus.MenuManager;
import toolbox.Maths;
import toolbox.MousePicker;
import transitions.Transition;

public class MouseMaster {
	
	private static final int SPLIT_INTERVALS = 200;
	private static final float MAX_CLICK_DISTANCE = 200;
	private static final float GLOW_TRANSITION_LENGTH = 0.2f;
	
	private static float frameMouseDX;
	private static float frameMouseDY;
	private static float frameMouseDWheel;
	
	private static MousePicker mousePicker;
	private static int clickStage = 0;
	private static boolean isOverMenu;
	
	private static Transition glowTransition;
	
	private static GuiTexture mouseOverGui;
	
	public static void init(Camera camera, Matrix4f projectionMatrix) {
		mousePicker = new MousePicker(camera, projectionMatrix);
		glowTransition = new Transition();
	}
	
	public static void update() {
		frameMouseDX = Mouse.getDX();
		frameMouseDY = Mouse.getDY();
		frameMouseDWheel = Mouse.getDWheel();
		isOverMenu = false;
		mousePicker.update();
		//only check for click if game is not paused
		if(!GameStateManager.isPaused()) {
			//check menu objects first
			for (Menu menu : MenuManager.getSceneMenus()) {
				if(menu.isMouseOver()) {
					menu.getActiveLayer().verticalScroll(frameMouseDWheel*0.001f);
					isOverMenu = true;
					//check tab layer first
					for (GuiTexture gui : menu.getTabLayer().getGuis()) {
						if(gui.isMouseOver()) {
							if(mouseOverGui != gui) {
								mouseOverGui = gui;
								clickStage = 0;
							}
							EntityManager.setMouseOverEntity(null);
							if(clickStage == 0 && !Mouse.isButtonDown(0)) {
								clickStage = 1;
							}
							else if(clickStage == 1 && Mouse.isButtonDown(0)) {
								clickStage = 2;
							}
							else if(clickStage == 2 && !Mouse.isButtonDown(0)) {
								gui.performClickedOnActions();
								clickStage = 0;
							}
							return;
						}
					}
					//then active layer
					for (GuiTexture gui : menu.getActiveLayer().getGuis()) {
						if(gui.isMouseOver()) {
							if(mouseOverGui != gui) {
								mouseOverGui = gui;
								clickStage = 0;
							}
							EntityManager.setMouseOverEntity(null);
							if(clickStage == 0 && !Mouse.isButtonDown(0)) {
								clickStage = 1;
							}
							else if(clickStage == 1 && Mouse.isButtonDown(0)) {
								clickStage = 2;
							}
							else if(clickStage == 2 && !Mouse.isButtonDown(0)) {
								gui.performClickedOnActions();
								clickStage = 0;
							}
							return;
						}
					}
					//then base layer
					for (GuiTexture gui : menu.getBaseLayer().getGuis()) {
						if(gui.isMouseOver()) {
							if(mouseOverGui != gui) {
								mouseOverGui = gui;
								clickStage = 0;
							}
							EntityManager.setMouseOverEntity(null);
							if(clickStage == 0 && !Mouse.isButtonDown(0)) {
								clickStage = 1;
							}
							else if(clickStage == 1 && Mouse.isButtonDown(0)) {
								clickStage = 2;
							}
							else if(clickStage == 2 && !Mouse.isButtonDown(0)) {
								gui.performClickedOnActions();
								clickStage = 0;
							}
							return;
						}
					}
				}
			}
			//then check 3-D object clicks based on mouse array from screen
			for(float distance=0; distance<MAX_CLICK_DISTANCE; distance=distance+MAX_CLICK_DISTANCE/SPLIT_INTERVALS) {
				for(Entity entity : EntityManager.getClickableEntities()) {
					if(Maths.isSphericalCollision(mousePicker.getPointOnMouseRay(distance), entity.getCentrePosition(), 0, entity.getModel().getRadius()*entity.getScale())) {
						if(EntityManager.getMouseOverEntity() != entity) {
							EntityManager.setMouseOverEntity(entity);
							glowTransition.startTransition(GLOW_TRANSITION_LENGTH);
							clickStage = 0;
						}
						mouseOverGui = null;
						if(clickStage == 0 && !Mouse.isButtonDown(0)) {
							clickStage = 1;
						}
						else if(clickStage == 1 && Mouse.isButtonDown(0)) {
							clickStage = 2;
						}
						else if(clickStage == 2 && !Mouse.isButtonDown(0)) {
							entity.performClickedOnActions();
							clickStage = 0;
						}
						return;
					}
				}
			}
		}
		clickStage = 0;
		EntityManager.setMouseOverEntity(null);
		mouseOverGui = null;
			
	}

	public static int getClickStage() {
		return clickStage;
	}

	public static GuiTexture getMouseOverGui() {
		return mouseOverGui;
	}
	
	public static float getGlowTransitionTime() {
		return glowTransition.getCurrentTime();
	}

	public static float getFrameMouseDX() {
		return frameMouseDX;
	}

	public static float getFrameMouseDY() {
		return frameMouseDY;
	}

	public static float getFrameMouseDWheel() {
		return frameMouseDWheel;
	}

	public static boolean isOverMenu() {
		return isOverMenu;
	}
	
}
