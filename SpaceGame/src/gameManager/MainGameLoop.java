package gameManager;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;

import enemies.Asteroid;
import entities.BuildingPlaceHolder;
import entities.Camera;
import entities.EntityManager;
import entities.Light;
import entities.Player;
import entities.PlayerRocket;
import fontRendering.Fonts;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import menus.MenuManager;
import menus.MenuRenderer;
import menus.StandardMenuTextures;
import models.StandardModels;
import models.TexturedModel;
import mouseManager.MouseMaster;
import planets.Planet;
import planets.PlanetManager;
import planets.PlanetTexture;
import planets.PlanetTexturePack;
import popups.PopupManager;
import postProcessing.Fbo;
import postProcessing.PostProcessing;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import transitions.TransitionManager;

public class MainGameLoop {

	public static void main(String[] args) {
		
		TransitionManager.init();
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		Camera camera = new Camera();
		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(camera, new Vector3f(1,1,1));
		lights.add(sun);
		EntityManager.init(camera, sun);
		
		Fonts.init(loader);
		TextMaster textMaster = new TextMaster(loader);
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		StandardMenuTextures.init(loader);
		StandardModels.init(loader);
		MenuRenderer menuRenderer = new MenuRenderer(loader);
		PopupManager.init(loader, camera);
		
		MasterRenderer.init(loader);
		
		PlanetTexture backgroundTexturePlanet = new PlanetTexture(loader.loadTexture("grassy"));
		PlanetTexture rTexturePlanet = new PlanetTexture(loader.loadTexture("dirt"));
		PlanetTexture gTexturePlanet = new PlanetTexture(loader.loadTexture("grassFlowers"));
		PlanetTexture bTexturePlanet = new PlanetTexture(loader.loadTexture("path"));
		
		PlanetTexturePack texturePackPlanet = new PlanetTexturePack(backgroundTexturePlanet,rTexturePlanet,gTexturePlanet,bTexturePlanet);
	
		PlanetTexture blendMapPlanet = new PlanetTexture(loader.loadTexture("blendMap"));
		
		Planet testPlanet = new Planet(new Vector3f(0,0,0), 100, loader, texturePackPlanet, blendMapPlanet,"heightmap");
		Planet planet2 = new Planet(new Vector3f(1500,0,0), 40, loader, texturePackPlanet, blendMapPlanet,"heightmap");
		Planet planet3 = new Planet(new Vector3f(-1200,0,3000), 50, loader, texturePackPlanet, blendMapPlanet,"heightmap");
		Planet planet4 = new Planet(new Vector3f(0,1500,600), 300, loader, texturePackPlanet, blendMapPlanet,"heightmap");
		Planet planet5 = new Planet(new Vector3f(0,-900,-300), 100, loader, texturePackPlanet, blendMapPlanet,"heightmap");
		Planet planet6 = new Planet(new Vector3f(-900,60,90), 200, loader, texturePackPlanet, blendMapPlanet,"heightmap");
		Planet planet7 = new Planet(new Vector3f(30,600,1500), 70, loader, texturePackPlanet, blendMapPlanet,"heightmap");
		
		TexturedModel bunnyModel = StandardModels.bunny;
		Player player = new Player(bunnyModel,1,10000,1000);
		player.setPlanet(testPlanet);
		player.setToPlanet(0,0);
		
		//TexturedModel tree = StandardModels.tree;	
		//Entity testTree = new Entity(tree, 5, testPlanet);
		//testTree.setToPlanet(0.4f,0.4f);
		
		PlayerRocket rocket = new PlayerRocket(StandardModels.banana, 100, testPlanet, 0.2f,0.2f);
		
		new Asteroid(StandardModels.asteroid, 100, 5, new Vector3f(0,200,-500), new Vector3f(1,1,1), 10, 600);
		
		BuildingPlaceHolder testHolder = new BuildingPlaceHolder(testPlanet, 0, 0.5f);
		BuildingPlaceHolder testHolder2 = new BuildingPlaceHolder(testPlanet, 0, 1f);
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
			
		Fbo multisampleFbo = new Fbo(Display.getWidth(), Display.getHeight());
		Fbo outputFbo = new Fbo(Display.getWidth(), Display.getHeight(),Fbo.DEPTH_TEXTURE);
		Fbo clickedEntityMultiSampleFbo = new Fbo(Display.getWidth(), Display.getHeight());
		Fbo clickedEntityOutputFbo = new Fbo(Display.getWidth(), Display.getHeight(),Fbo.DEPTH_TEXTURE);
		PostProcessing.init(loader);
		
		MouseMaster.init(camera,MasterRenderer.getProjectionMatrix());
		//EntityManager.makeEntityClickable(testTree);
		EntityManager.makeEntityClickable(player);
		EntityManager.makeEntityClickable(rocket);
		//EntityManager.makeEntityCollision(testTree);
		EntityManager.makeEntityCollision(player);
		EntityManager.makeEntityCollision(testHolder);
		EntityManager.makeEntityCollision(testHolder2);
		EntityManager.makeEntityCollision(rocket);
		EntityManager.setUserMovingEntity(player, 0);
		
		DisplayManager.startGameTimer(); //this should be run just before starting the game loop
		GameStateManager.pauseGame();
		while(!Display.isCloseRequested()) {
			
			//begin game
			if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
				GameStateManager.unpauseGame();
			}
			
			//Motion and Camera
			EntityManager.moveEntities();			
			camera.updateViewMatrix();
			
			//Rendering
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			
			multisampleFbo.bindFrameBuffer();
			MasterRenderer.renderScene(EntityManager.getSceneEntities(), PlanetManager.getScenePlanets(), lights, camera, null);
			multisampleFbo.unbindFrameBuffer();
			
			//multisampleFbo.resolveToScreen(); 	//No Post Processing
			
			//Post Processing
			multisampleFbo.resolveToFbo(outputFbo);							
			
			clickedEntityMultiSampleFbo.bindFrameBuffer();
			MasterRenderer.renderScene(EntityManager.getMouseOverEntity(), lights, camera, null);
			clickedEntityMultiSampleFbo.unbindFrameBuffer();
			
			clickedEntityMultiSampleFbo.resolveToFbo(clickedEntityOutputFbo);
			PostProcessing.doPostProcessing(outputFbo.getColourTexture(), outputFbo.getDepthTexture(), clickedEntityOutputFbo.getDepthTexture(), MouseMaster.getGlowTransitionTime());	//Post Processing
			
			//Updates
			EntityManager.update();
			MouseMaster.update();	
			TransitionManager.update();
			MenuManager.update(); 
			PopupManager.update();
			PopupManager.render();
			menuRenderer.render(MenuManager.getSceneMenus());
			textMaster.render();
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
			
		}
		
		clickedEntityMultiSampleFbo.cleanUp();
		clickedEntityOutputFbo.cleanUp();
		PostProcessing.cleanUp();
		multisampleFbo.cleanUp();
		outputFbo.cleanUp();
		textMaster.cleanUp();
		//waterShader.cleanUp();
		guiRenderer.cleanUp();
		menuRenderer.cleanUp();
		PopupManager.cleanUp();
		MasterRenderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
