package popups;

import java.util.ArrayList;
import java.util.List;

import entities.Camera;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import renderEngine.Loader;

public class PopupManager {
	
	private static final float MIN_Y_OFFSET = 0.25f;
	
	private static List<Popup> activePopups = new ArrayList<Popup>();
	private static List<Popup> finishedPopups = new ArrayList<Popup>();
	private static boolean hasFinishedPopups;
	
	private static List<PopupGui> activePopupGuis = new ArrayList<PopupGui>();
	private static List<PopupGui> finishedPopupGuis = new ArrayList<PopupGui>();
	private static List<GuiTexture> renderingGuis = new ArrayList<GuiTexture>();
	private static boolean hasFinishedPopupGuis;
	
	private static TextMaster textMaster;
	private static GuiRenderer guiRenderer;
	
	private static Camera camera;
	
	public static void init(Loader loader, Camera theCamera) {
		textMaster = new TextMaster(loader);
		guiRenderer = new GuiRenderer(loader);
		camera = theCamera;
	}
	
	public static void addActivePopup(Popup popup) {
		for (Popup activePopup : activePopups) {
			if(activePopup.getPosition().getY() > MIN_Y_OFFSET) {
				float yChange = MIN_Y_OFFSET - activePopup.getPosition().getY();
				for(Popup activePopupChange : activePopups) {
					activePopupChange.pushUp(yChange);
				}
			}
		}
		activePopups.add(popup);
	}
	
	public static void addFinishedPopup(Popup popup) {
		finishedPopups.add(popup);
		hasFinishedPopups = true;
	}
	
	public static void addActivePopupGui(PopupGui popupGui) {
		if(popupGui.getEntity() != null) {
			for(PopupGui popGui : activePopupGuis) {
				if(popGui.getEntity() == popupGui.getEntity()) {
					finishedPopupGuis.add(popGui);
					hasFinishedPopupGuis = true;
					popupGui.setSkipFadeIn(true);
				}
			}
		}
		activePopupGuis.add(popupGui);
	}
	
	public static void addFinishedPopupGui(PopupGui popupGui) {
		finishedPopupGuis.add(popupGui);
		hasFinishedPopupGuis = true;
	}
	
	public static void update() {
		for(Popup popup : activePopups) {
			popup.update();
		}
		if(hasFinishedPopups) {
			for(Popup popup : finishedPopups) {
				activePopups.remove(popup);
			}
			finishedPopups.clear();
			hasFinishedPopups = false;
		}
		for(PopupGui popupGui : activePopupGuis) {
			popupGui.update();
		}
		if(hasFinishedPopupGuis) {
			for(PopupGui popupGui : finishedPopupGuis) {
				activePopupGuis.remove(popupGui);
			}
			finishedPopupGuis.clear();
			hasFinishedPopupGuis = false;
		}
	}
	
	public static void render() {
		for(Popup popup : activePopups) {
			popup.load(textMaster);
		}
		textMaster.render();
		textMaster.clearText();
		for(PopupGui popupGui : activePopupGuis) {
			renderingGuis.add(popupGui.getGui());
		}
		guiRenderer.render(renderingGuis);
		renderingGuis.clear();
	}
	
	public static void cleanUp() {
		textMaster.cleanUp();
	}

	public static Camera getCamera() {
		return camera;
	}

	public static TextMaster getTextMaster() {
		return textMaster;
	}
}
