package menus;

import renderEngine.Loader;

public class StandardMenuTextures {
	
	public static int brown;
	public static int white;
	public static int healthBar;
	public static int healthHealthMap;
	
	public static void init(Loader loader) {
		brown = loader.loadTexture("brown");
		white = loader.loadTexture("white");
		healthBar = loader.loadTexture("health");
		healthHealthMap = loader.loadTexture("healthHealthMap");
	}

}
