package menus;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import entities.BuildingPlaceHolder;
import entities.Entity;
import entities.EntityManager;
import entities.Player;
import entities.Rocket;
import fontMeshCreator.GUIText;
import fontRendering.Fonts;
import guis.GuiTexture;
import resources.Resource;
import resources.ResourceBar;
import resources.ResourceCollector;
import resources.ResourceStorage;
import resources.ResourceStorageBar;
import resources.StoreResourceButton;

public class MenuManager {
	
	private static List<Menu> sceneMenus = new ArrayList<Menu>();
	
	public static void addMenuToScene(Menu menu) {
		sceneMenus.add(menu);
	}
	
	public static void removeMenuFromScene(Menu menu) {
		sceneMenus.remove(menu);
	}
	
	public static void clearSceneMenus() {
		sceneMenus.clear();
	}
	
	public static void createEntityMenu(Entity entity) {
		Menu menu = new Menu(new Vector2f(0.5f,0.0f), new Vector2f(0.5f,0.5f), entity.getFont());
		MenuLayer layer1 = new MenuLayer(menu);
		menu.setBaseLayer(layer1);
		GuiTexture gui1 = new GuiTexture(StandardMenuTextures.brown, layer1, new Vector2f(0,0), new Vector2f(1,1f));
		GUIText text1 = new GUIText("TESTING", 10, gui1, new Vector2f(-1,1), 1, true);
		MenuLayer layer2 = new MenuLayer(menu);
		layer2.setTotalScroll(1);
		Button button1 = new Button(StandardMenuTextures.white, layer2, new Vector2f(0,0), new Vector2f(0.3f,0.3f));
		GUIText text2 = new GUIText(entity.getUnitResourceAmountHeld(), 10, button1, new Vector2f(-1,1), 1, true);
		Button button2 = new Button(StandardMenuTextures.white, layer2, new Vector2f(0,-2f), new Vector2f(0.3f,0.3f));
		GUIText text3 = new GUIText("Third", 10, button2, new Vector2f(-1,1), 1, true);
		Button button3 = new Button(StandardMenuTextures.white, layer2, new Vector2f(0,-1f), new Vector2f(0.3f,0.3f));
		GUIText text4 = new GUIText("Second", 10, button3, new Vector2f(-1,1), 1, true);
		Tab goToL1 = new Tab(StandardMenuTextures.white, layer1, new Vector2f(-0.7f, 0.8f), new Vector2f(0.2f, 0.2f));
		GUIText textGT1 = new GUIText("L1", 15, goToL1, new Vector2f(-1,0), 1, true);
		Tab goToL2 = new Tab(StandardMenuTextures.white, layer2, new Vector2f(0.7f, 0.8f), new Vector2f(0.2f, 0.2f));
		GUIText textGT2 = new GUIText("L2", 15, goToL2, new Vector2f(-1,0), 1, true);
		addMenuToScene(menu);
	}
	
	public static void createBuldingPlaceHolderMenu(BuildingPlaceHolder placeholder) {
		Menu menu = new Menu(new Vector2f(0.5f,0.5f), new Vector2f(0.5f,0.5f), placeholder.getFont());
		MenuLayer layer1 = new MenuLayer(menu);
		menu.setBaseLayer(layer1);
		GuiTexture gui1 = new GuiTexture(StandardMenuTextures.brown, layer1, new Vector2f(0,0), new Vector2f(1,1f));
		GUIText text1 = new GUIText("What Would you like to Build?", 4, gui1, new Vector2f(-1,1), 1, true);
		MenuLayer layer2 = new MenuLayer(menu);
		Button wellButton = new BuildingCreator(StandardMenuTextures.white, layer2, new Vector2f(-0.5f,-0.3f), new Vector2f(0.3f,0.3f), placeholder, "waterCollector");
		GUIText wellText = new GUIText("WELL", 10, wellButton, new Vector2f(-1,0.2f), 1, true);
		Button storageButton = new BuildingCreator(StandardMenuTextures.white, layer2, new Vector2f(0.5f,-0.3f), new Vector2f(0.3f,0.3f), placeholder, "resourceStorage");
		GUIText storageText = new GUIText("STORAGE", 10, storageButton, new Vector2f(-1,0.2f), 1, true);
		addMenuToScene(menu);
	}
	
	public static void createResourceCollectorMenu(ResourceCollector collector) {
		Menu menu = new Menu(new Vector2f(0.5f,0.5f), new Vector2f(0.5f,0.5f), collector.getFont());
		MenuLayer layer1 = new MenuLayer(menu);
		menu.setBaseLayer(layer1);
		GuiTexture gui1 = new GuiTexture(StandardMenuTextures.brown, layer1, new Vector2f(0,0), new Vector2f(1,1f));
		GUIText text1 = new GUIText(collector.getResourceTypeHeld().getResourceName() + " Collector", 4, gui1, new Vector2f(-1,1), 1, true);
		GUIText text2 = new GUIText("What Would you like to do?", 4, gui1, new Vector2f(-1,0.7f), 1, true);
		MenuLayer layer2 = new MenuLayer(menu);
		ResourceBar resourceBar = new ResourceBar(StandardMenuTextures.healthBar,StandardMenuTextures.healthHealthMap, layer2, new Vector2f(0,0.4f), new Vector2f(0.4f,0.4f), collector);
		Button collectButton = new CollectButton(StandardMenuTextures.white, layer2, collector, new Vector2f(-0.5f,-0.3f), new Vector2f(0.3f,0.3f));
		GUIText treeText = new GUIText("Collect " + collector.getResourceTypeHeld().getResourceName(), 10, collectButton, new Vector2f(-1,0.2f), 1, true);
		Button button2 = new Button(StandardMenuTextures.white, layer2, new Vector2f(0.5f,-0.3f), new Vector2f(0.3f,0.3f));
		GUIText text3 = new GUIText("Close", 10, button2, new Vector2f(-1,0.2f), 1, true);
		addMenuToScene(menu);
	}
	
	public static void createResourceStorageMenu(ResourceStorage storage) {
		Menu menu = new Menu(new Vector2f(0.5f,0.5f), new Vector2f(0.5f,0.5f), storage.getFont());
		menu.setTitleHeight(0.2f);
		MenuLayer baseLayer = new MenuLayer(menu);
		menu.setBaseLayer(baseLayer);
		GuiTexture baseGui = new GuiTexture(StandardMenuTextures.brown, baseLayer, new Vector2f(0,0), new Vector2f(1,1f));
		GuiTexture closeGui = new Button(StandardMenuTextures.white, menu.getTabLayer(), new Vector2f(0.9f,0.9f), new Vector2f(0.1f,0.1f));
		GUIText close = new GUIText("X", 30, closeGui, new Vector2f(-1,1), 1, true);
		MenuLayer resourceLayer = new MenuLayer(menu);
		GUIText resourceTitle = new GUIText("Resources Storage", 7, baseGui, new Vector2f(-1,1), 1, true);
		resourceLayer.setTotalScroll(0.4f * (storage.getResourceAmounts().length - 2));
		StoreResourceButton storeButton = new StoreResourceButton(StandardMenuTextures.white, resourceLayer, new Vector2f(0.5f,0.2f), new Vector2f(0.4f, 0.4f), storage);
		storeButton.setStationary(true);
		new GUIText("Store Current Resources", 10, storeButton, new Vector2f(-1,0.5f), 1, true);
		for(int i = 0; i < storage.getResourceAmounts().length; i++) {
			Resource currentResource = new Resource(i);
			float currentYPos = 0.2f - 0.8f * i;
			ResourceStorageBar storageBar = new ResourceStorageBar(StandardMenuTextures.healthBar,StandardMenuTextures.healthHealthMap, resourceLayer, new Vector2f(-0.5f,currentYPos), new Vector2f(0.4f,0.4f), storage, currentResource);
			new GUIText(currentResource.getResourceName(), 6, storageBar, new Vector2f(-1,0.5f), 1, true);
		}
		
		addMenuToScene(menu);
	}
	
	public static void createRocketMenu(Rocket rocket) {
		Menu menu = new Menu(new Vector2f(0.5f,0.5f), new Vector2f(0.5f,0.5f), rocket.getFont());
		MenuLayer baseLayer = new MenuLayer(menu);
		menu.setBaseLayer(baseLayer);
		GuiTexture baseGui = new GuiTexture(StandardMenuTextures.brown, baseLayer, new Vector2f(0,0), new Vector2f(1,1f));
		GuiTexture closeGui = new Button(StandardMenuTextures.white, menu.getTabLayer(), new Vector2f(0.9f,0.9f), new Vector2f(0.1f,0.1f));
		GUIText close = new GUIText("X", 30, closeGui, new Vector2f(-1,1), 1, true);
		MenuLayer rocketLayer = new MenuLayer(menu);
		RocketButton rocketButton = new RocketButton(StandardMenuTextures.white, rocketLayer, rocket, new Vector2f(0,0), new Vector2f(0.4f, 0.4f));
		if(EntityManager.getUserMovingEntity() != rocket) {
			GUIText rocketText = new GUIText("Enter Rocket", 10, rocketButton, new Vector2f(-1,0.2f), 1, true);
		}else {
			GUIText rocketText = new GUIText("Exit Rocket", 10, rocketButton, new Vector2f(-1,0.2f), 1, true);
		}
		addMenuToScene(menu);
	}
	
	public static void createPlayerMenu(Player player) {
		Menu menu = new Menu(new Vector2f(0.5f,0.0f), new Vector2f(0.5f,0.5f), player.getFont());
		menu.setTitleHeight(0.3f);
		MenuLayer layer1 = new MenuLayer(menu);
		menu.setBaseLayer(layer1);
		GuiTexture gui1 = new GuiTexture(StandardMenuTextures.brown, layer1, new Vector2f(0,0), new Vector2f(1,1f));
		GUIText text1 = new GUIText("Player Stats", 10, gui1, new Vector2f(-1,1), 1, true);
		MenuLayer layer2 = new MenuLayer(menu);
		layer2.setTotalScroll(1);
		Button button1 = new Button(StandardMenuTextures.white, layer2, new Vector2f(0,0), new Vector2f(0.3f,0.3f));
		if(player.getResourceTypeHeld() != null) {
			GUIText text2 = new GUIText(player.getUnitResourceAmountHeld() + " " + player.getResourceTypeHeld().getResourceName(), 10, button1, new Vector2f(-1,1), 1, true);
		}else {
			GUIText text2 = new GUIText(player.getUnitResourceAmountHeld(), 10, button1, new Vector2f(-1,1), 1, true);
		}
		Button button2 = new Button(StandardMenuTextures.white, layer2, new Vector2f(0,-2f), new Vector2f(0.3f,0.3f));
		GUIText text3 = new GUIText("Close", 10, button2, new Vector2f(-1,1), 1, true);
		Button button3 = new Button(StandardMenuTextures.white, layer2, new Vector2f(0,-1f), new Vector2f(0.3f,0.3f));
		GUIText text4 = new GUIText("Close", 10, button3, new Vector2f(-1,1), 1, true);
		addMenuToScene(menu);
	}
	
	public static void createPauseMenu() {
		Menu menu = new Menu(new Vector2f(0,0), new Vector2f(0.8f,0.8f), Fonts.candara);
		MenuLayer layer1 = new MenuLayer(menu);
		menu.setBaseLayer(layer1);
		GuiTexture gui1 = new GuiTexture(StandardMenuTextures.brown, layer1, new Vector2f(0,0), new Vector2f(1,1f));
		GUIText text1 = new GUIText("       Welcome to the Space Game Engine!                                                                                       Contorls:     WASD - Move Player                               Mouse Right Click - Move camera            Mouse Left Click - Interact with objects                                                                                          Press Enter to begin!", 4, gui1, new Vector2f(-1,1), 1, true);
		addMenuToScene(menu);
	}

	public static List<Menu> getSceneMenus() {
		return sceneMenus;
	}
	
	public static void update() {
		for (Menu menu : sceneMenus) {
			menu.update();
		}
	}
	
	
}
