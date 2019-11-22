package gameManager;

import menus.MenuManager;
import transitions.TransitionManager;

public class GameStateManager {
	
	private static boolean isPaused;
	
	public static void pauseGame() {//handle pausing the game
		if(!isPaused) {
			TransitionManager.pauseTransitions();
			isPaused = true;
			MenuManager.createPauseMenu();
		}
	}
	
	public static void unpauseGame() {
		if(isPaused) {
			TransitionManager.unpauseTransitions();
			isPaused = false;
			MenuManager.clearSceneMenus();
		}
	}

	public static boolean isPaused() {
		return isPaused;
	}

}
