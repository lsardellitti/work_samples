package fontRendering;

import java.io.File;

import fontMeshCreator.FontType;
import renderEngine.Loader;

public class Fonts {
	
	public static FontType candara;
	
	public static void init(Loader loader) {
		candara = new FontType(loader.loadTexture("candara"), new File("res/candara.fnt"));
	}

}
