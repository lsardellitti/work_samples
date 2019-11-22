package models;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.Loader;
import renderEngine.OBJLoader;
import textures.ModelTexture;

public class StandardModels {
	
	public static TexturedModel crate;
	public static TexturedModel bullet;
	public static TexturedModel bunny;
	public static TexturedModel tree;
	public static TexturedModel asteroid;
	public static TexturedModel banana;
	public static TexturedModel storage;
	public static TexturedModel well;
	
	public static void init(Loader loader) {
		crate = new TexturedModel(OBJLoader.loadObjModel("crate", loader), new ModelTexture(loader.loadTexture("crate")), new Vector3f(0,1,0), new Vector3f(0,0,1), 100, 200);
		bullet = new TexturedModel(OBJLoader.loadObjModel("crate", loader), new ModelTexture(loader.loadTexture("white")), new Vector3f(0,1,0), new Vector3f(0,0,1), 100, 200);
		bunny = new TexturedModel(OBJLoader.loadObjModel("stanfordBunny",loader),new ModelTexture(loader.loadTexture("white")), new Vector3f(0,1,0), new Vector3f(0,0,1), 5, 10);
		tree = new TexturedModel(OBJLoader.loadObjModel("tree",loader),new ModelTexture(loader.loadTexture("tree")), new Vector3f(0,1,0), new Vector3f(0,0,1), 1, 3, 1.5f);		
		asteroid = new TexturedModel(OBJLoader.loadObjModel("asteroid", loader), new ModelTexture(loader.loadTexture("asteroid")),  new Vector3f(0,1,0), new Vector3f(0,0,1), 1, 2, 1.5f);
		banana = new TexturedModel(OBJLoader.loadObjModel("banana3", loader), new ModelTexture(loader.loadTexture("banana")),  new Vector3f(1,0,0), new Vector3f(0,0,-1), 0.08f, 0.2f, 0.15f);
		storage = new TexturedModel(OBJLoader.loadObjModel("tree",loader),new ModelTexture(loader.loadTexture("storage")), new Vector3f(0,1,0), new Vector3f(0,0,1), 1, 3, 1.5f);		
		well = new TexturedModel(OBJLoader.loadObjModel("tree",loader),new ModelTexture(loader.loadTexture("well")), new Vector3f(0,1,0), new Vector3f(0,0,1), 1, 3, 1.5f);		
	
	}

}
