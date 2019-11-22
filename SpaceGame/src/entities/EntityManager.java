package entities;

import java.util.ArrayList;
import java.util.List;

import gameManager.GameStateManager;

public class EntityManager {
	
	private static List<Entity> sceneEntities = new ArrayList<Entity>();
	private static List<Entity> addSceneEntities = new ArrayList<Entity>();
	private static List<Entity> removableEntities = new ArrayList<Entity>();
	private static List<Entity> clickableEntities = new ArrayList<Entity>();
	private static List<Entity> addClickableEntities = new ArrayList<Entity>();
	private static List<Entity> collisionEntities = new ArrayList<Entity>();
	private static List<Entity> addCollisionEntities = new ArrayList<Entity>();
	private static List<Entity> movingEntities = new ArrayList<Entity>();
	private static List<Entity> addMovingEntities = new ArrayList<Entity>();
	private static Entity mouseOverEntity;
	private static Entity userMovingEntity;
	
	private static Camera camera;
	private static Light sun;
	
	public static void init(Camera theCamera, Light theSun) {
		camera = theCamera;
		sun = theSun;
	}
	
	public static void moveEntities() {
		if(!GameStateManager.isPaused()) {
			if(userMovingEntity != null) {
				userMovingEntity.userMove();
				camera.moveAroundEntity();
				sun.followEntity();
			}else {
				camera.freeMove();
				sun.followCamera();
			}
			for(Entity entity : movingEntities) {
				if(!removableEntities.contains(entity)) {
					entity.move(); 		// change to take into account entities movement stats
				}
			}
		}
	}
	
	public static void updateCollectors() {
		
	}
	
	public static void addEntityToScene(Entity entity) {
		if(!addSceneEntities.contains(entity)) {
			addSceneEntities.add(entity);
		}
	}
	
	public static void removeEntityFromScene(Entity entity) {
		sceneEntities.remove(entity);
		clickableEntities.remove(entity);
		collisionEntities.remove(entity);
		movingEntities.remove(entity);
		if(mouseOverEntity == entity) {
			mouseOverEntity = null;
		}
		if(userMovingEntity == entity) {
			userMovingEntity = null;
		}
	}
	
	public static void makeEntityMoving(Entity entity) {
		if(!addMovingEntities.contains(entity)) {
			addMovingEntities.add(entity);
		}
	}
	
	public static void makeEntityNotMoving(Entity entity) {
		movingEntities.remove(entity);
	}
	
	public static void setUserMovingEntity(Entity entity, int cameraDirection) {
		userMovingEntity = entity;
		camera.setCentredEntity(userMovingEntity, cameraDirection);
	}
	
	public static void makeCameraFreeMove() {
		userMovingEntity = null;
		camera.initializeFreeMove();
	}
	
	public static void makeEntityCollision(Entity entity) {
		if(!addCollisionEntities.contains(entity)) {
			addCollisionEntities.add(entity);
		}
	}
	
	public static void removeEntityCollision(Entity entity) {
		collisionEntities.remove(entity);
	}
	
	public static void addRemovbleEntity(Entity entity) {
		if(!removableEntities.contains(entity)) {
			removableEntities.add(entity);
		}
	}
	
	public static void setMouseOverEntity(Entity entity) {
		mouseOverEntity = entity;
	}
	
	public static void makeEntityClickable(Entity entity) {
		if(!addClickableEntities.contains(entity)) {
			addClickableEntities.add(entity);
		}
	}
	
	public static void makeEntityNotClickable(Entity entity) {
		clickableEntities.remove(entity);
	}
	
	public static void update() {
		for (Entity entity : addSceneEntities) {
			if(!sceneEntities.contains(entity)) {
				sceneEntities.add(entity);
			}
		}
		addSceneEntities.clear();
		for (Entity entity : addCollisionEntities) {
			if(!collisionEntities.contains(entity)) {
				collisionEntities.add(entity);
			}
		}
		addCollisionEntities.clear();
		for (Entity entity : addClickableEntities) {
			if(!clickableEntities.contains(entity)) {
				clickableEntities.add(entity);
			}
		}
		addClickableEntities.clear();
		for (Entity entitiy : addMovingEntities) {
			if(!movingEntities.contains(entitiy)) {
				movingEntities.add(entitiy);
			}
		}
		addMovingEntities.clear();
		for (Entity entity : removableEntities) {
			removeEntityFromScene(entity);
		}
		removableEntities.clear();
	}

	public static List<Entity> getSceneEntities() {
		return sceneEntities;
	}

	public static List<Entity> getClickableEntities() {
		return clickableEntities;
	}

	public static Entity getUserMovingEntity() {
		return userMovingEntity;
	}

	public static Entity getMouseOverEntity() {
		return mouseOverEntity;
	}

	public static List<Entity> getCollisionEntities() {
		return collisionEntities;
	}

	public static Camera getCamera() {
		return camera;
	}
	
}
