package entities;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import enemies.Asteroid;
import fontMeshCreator.FontType;
import fontRendering.Fonts;
import guis.HealthBar;
import menus.MenuManager;
import menus.StandardMenuTextures;
import models.TexturedModel;
import planets.Planet;
import popups.PopupGui;
import renderEngine.DisplayManager;
import resources.Resource;
import toolbox.Maths;

public class Entity {
	
	private TexturedModel model;
	private Vector3f position;
	private Vector3f centrePosition;
	private Vector3f upDirection;
	private Vector3f forwardDirection;
	private Vector3f binormalDirection;
	private float radius;
	private float height;
	private float radiusDistance;
	private float scale;
	private float upwardSpeed = 0;
	private boolean isInAir = false;
	
	private float maxHealth;
	private float health;
	private boolean isDestructible;
	private boolean useBasicPopupDirection;

	private FontType font = Fonts.candara;
	
	private int textureIndex = 0;
	
	private Planet planet;
	
	private int resourceStorageSize;
	private int resourceCollectionSize;
	private int resourceAmountHeld;
	private Resource resourceTypeHeld;
	
	public Entity(TexturedModel model, float scale) {
		super();
		this.model = model;
		this.upDirection = Maths.copyVector(model.getUpwardDirection());
		this.forwardDirection = Maths.copyVector(model.getForwardDirection());
		this.binormalDirection = Vector3f.cross(upDirection, forwardDirection, null);
		this.scale = scale;
		this.radius = model.getRadius()*scale;
		this.height = model.getHeight()*scale;
		EntityManager.addEntityToScene(this);
	}
	
	public Entity(TexturedModel model, float scale, Planet planet) {
		super();
		this.model = model;
		this.upDirection = Maths.copyVector(model.getUpwardDirection());
		this.forwardDirection = Maths.copyVector(model.getForwardDirection());
		this.binormalDirection = Vector3f.cross(upDirection, forwardDirection, null);
		this.scale = scale;
		this.radius = model.getRadius()*scale;
		this.height = model.getHeight()*scale;
		this.planet = planet;
		EntityManager.addEntityToScene(this);
	}
	
	public float getRadiusDistance() {
		return radiusDistance;
	}

	public void setRadiusDistance(float radiusDistance) {
		this.radiusDistance = radiusDistance;
	}

	public Vector3f getUpDirection() {
		return upDirection;
	}
	
	public void setUpDirection(Vector3f direction) {
		this.upDirection = Maths.copyVector(direction);
	}
	
	public Planet getPlanet() {
		return this.planet;
	}
	
	public void setPlanet(Planet planet) {
		this.planet = planet;
	}
	
	public void setToPlanet(float theta, float phi) {//initialize position and directions on a planet		
		this.upDirection = new Vector3f((float) (Math.cos(phi)*Math.cos(theta)), (float) (Math.cos(phi)*Math.sin(theta)), (float) (Math.sin(phi)));
		this.radiusDistance = planet.getOnPlanetHeight(theta, phi);
		Vector3f upCross = Vector3f.cross(model.getUpwardDirection(), upDirection, null);
		if(!Maths.isVectorZero(upCross)) {
			upCross.normalise();
			this.forwardDirection = Maths.rotateVector(model.getForwardDirection(), upCross, Vector3f.angle(model.getUpwardDirection(), upDirection));
		}
		else {
			this.forwardDirection = model.getForwardDirection();
		}
		this.binormalDirection = Vector3f.cross(upDirection, forwardDirection, null); 
		this.position = Vector3f.add(planet.getPosition(), Maths.scaledVector(upDirection, radiusDistance), null);
		this.centrePosition = Vector3f.add(position, Maths.normScaledVector(upDirection, model.getRadius()*scale), null);
	}
	
	public void moveOnPlanet(float forwardSpeed, float rotationSpeed, float jumpSpeed, boolean isJumping) {
		//entity moving based on speed and inputs
		this.upDirection = Maths.rotateVector(upDirection, binormalDirection, forwardSpeed*DisplayManager.getFrameTimeSeconds()/this.radiusDistance);
		this.forwardDirection = Vector3f.cross(binormalDirection, upDirection, null);
		this.forwardDirection = Maths.rotateVector(forwardDirection, upDirection, rotationSpeed*DisplayManager.getFrameTimeSeconds());
		this.binormalDirection = Vector3f.cross(upDirection, forwardDirection, null);
		upDirection.normalise();
		float phi = (float) Math.asin(upDirection.z);
		float theta = (float) Math.atan(upDirection.y/upDirection.x);
		if (upDirection.x == 0) {
			theta = 0;
		}
		if (upDirection.x < 0) {
			theta += Math.PI;
		}
		if (theta < 0) {
			theta = (float) (2*Math.PI + theta);
		}
		if (isJumping && !this.isInAir) {
			this.upwardSpeed = jumpSpeed;
			this.isInAir = true;
		}
		//find effect of planet's gravity
		this.upwardSpeed -= planet.getGravity() * DisplayManager.getFrameTimeSeconds();
		this.radiusDistance += this.upwardSpeed*DisplayManager.getFrameTimeSeconds();
		float planetRadius = planet.getOnPlanetHeight(theta, phi);
		if (this.radiusDistance < planetRadius) {
			this.radiusDistance = planetRadius;
			this.upwardSpeed = 0;
			this.isInAir = false;
		}
		//calculate new position
		this.position = Vector3f.add(planet.getPosition(), Maths.scaledVector(upDirection, radiusDistance), null);
		this.centrePosition = Vector3f.add(position, Maths.normScaledVector(upDirection, model.getRadius()*scale), null);
		//check for collisions with other objects
		for(Entity entity:EntityManager.getCollisionEntities()) {
			if(entity != this) {
				if(entity.getClass() != Asteroid.class) {
					//only check for collisions with objects on the same planet
					if(entity.getPlanet() == planet) {
						if(this.radiusDistance >= entity.getRadiusDistance() - this.height && this.radiusDistance <= entity.getRadiusDistance() + entity.getHeight()) {
							float otherTheta = entity.getRadius()/this.radiusDistance;
							float thisTheta = radius/this.radiusDistance;
							float angleBetween = Vector3f.angle(upDirection, entity.getUpDirection());
							if(angleBetween < otherTheta + thisTheta) {
								Vector3f changingCross = Vector3f.cross(entity.getUpDirection(), upDirection, null);
								while(Maths.isVectorZero(changingCross)) {
									System.out.println("0 vector Issue : IN ENTITY CLASS");
									changingCross = Vector3f.cross(entity.getUpDirection(), Maths.generateRandomVector(), null);
								}
								this.upDirection = Maths.rotateVector(upDirection, changingCross, otherTheta + thisTheta - angleBetween);
								this.forwardDirection = Maths.rotateVector(forwardDirection, changingCross, otherTheta + thisTheta - angleBetween);
								this.binormalDirection = Vector3f.cross(upDirection, forwardDirection, null);
								this.position = Vector3f.add(planet.getPosition(), Maths.scaledVector(upDirection, radiusDistance), null);
								this.centrePosition = Vector3f.add(position, Maths.normScaledVector(upDirection, model.getRadius()*scale), null);
							}
						}
					}
				}
			}
		}
	}
	
	public void destroy() {
		EntityManager.addRemovbleEntity(this);
	}
	
	public void performClickedOnActions() {
		MenuManager.clearSceneMenus();
		MenuManager.createEntityMenu(this);
	}
	
	public void move() {
		
	}
	
	public void userMove() {
	
	}
	
	public void collideWith(Entity entity) {
		//only override in classes which do not support
		//double collision errors like asteroids because
		//they will be destroyed
	}
	
	public float calculateTheta() {
		float theta = (float) Math.atan(upDirection.y/upDirection.x);
		if (upDirection.x == 0) {
			theta = 0;
		}
		if (upDirection.x < 0) {
			theta += Math.PI;
		}
		if (theta < 0) {
			theta = (float) (2*Math.PI + theta);
		}
		return theta;
	}
	
	public float calculatePhi() {
		return (float) Math.asin(upDirection.z);
	}
	
	public void transferResource(Entity fromEntity, Entity toEntity) {
		if(toEntity.getResourceTypeHeld() == null || toEntity.getResourceTypeHeld().getResourceID() == fromEntity.getResourceTypeHeld().getResourceID()) {
			if(toEntity.getResourceAmountHeld() < toEntity.getResourceStorageSize()) {
				if(fromEntity.getResourceAmountHeld() < toEntity.getResourceStorageSize() - toEntity.getResourceAmountHeld()) {
					fromEntity.addResourceAmountHeld(-fromEntity.getResourceAmountHeld());
					toEntity.addResourceAmountHeld(fromEntity.getResourceAmountHeld());
				}else {
					fromEntity.addResourceAmountHeld(toEntity.getResourceAmountHeld() - toEntity.getResourceStorageSize());
					toEntity.addResourceAmountHeld(toEntity.getResourceStorageSize() - toEntity.getResourceAmountHeld());
				}
			}
		}
	}
	
	public void takeDamage(float damage) {
		if(isDestructible) {
			health -= damage;
			new PopupGui(new HealthBar(StandardMenuTextures.healthBar, StandardMenuTextures.healthHealthMap, new Vector2f(-0.5f,0), new Vector2f(0.2f, 0.2f), this), this);
			if(health <= 0) {
				destroy();
			}
		}
	}
	
	public void takeDamage(float damage, float guiSize) {
		if(isDestructible) {
			health -= damage;
			new PopupGui(new HealthBar(StandardMenuTextures.healthBar, StandardMenuTextures.healthHealthMap, new Vector2f(-0.5f,0), Maths.scaledVector(new Vector2f(0.2f, 0.2f), guiSize), this), this);
			if(health <= 0) {
				destroy();
			}
		}
	}
	
	public float getTextureXOffset() {
		int column = textureIndex%model.getTexture().getNumberOfRows();
		return (float)column/(float)model.getTexture().getNumberOfRows();
	}
	
	public float getTextureYOffset() {
		int row = textureIndex/model.getTexture().getNumberOfRows();
		return (float)row/(float)model.getTexture().getNumberOfRows();
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = Maths.copyVector(position);
		this.centrePosition = Vector3f.add(position, Maths.normScaledVector(upDirection, model.getRadius()*scale), null);
	}
	
	public void setCentrePosition(Vector3f centrePosition) {
		this.centrePosition = Maths.copyVector(centrePosition);
		this.position = Vector3f.sub(centrePosition, Maths.normScaledVector(upDirection,this.radius), null);
	}

	public float getScale() {
		return scale;
	}
	
	public float getAbsoluteRadius() {
		return scale*model.getRadius();
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public void setAllDirections(Vector3f forwardDirection) {
		this.forwardDirection = Maths.normScaledVector(forwardDirection, 1);
		Vector3f forCross = Vector3f.cross(model.getForwardDirection(), this.forwardDirection, null);
		if(!Maths.isVectorZero(forCross)) {
			forCross.normalise();
			this.upDirection = Maths.rotateVector(model.getUpwardDirection(), forCross, Vector3f.angle(model.getForwardDirection(), this.forwardDirection));
		}
		else {
			this.upDirection = model.getUpwardDirection();
		}
		this.binormalDirection = Vector3f.cross(upDirection, forwardDirection, null); 
	}

	public Vector3f getForwardDirection() {
		return forwardDirection;
	}

	public void setForwardDirection(Vector3f forwardDirection) {
		this.forwardDirection = Maths.copyVector(forwardDirection);
	}

	public Vector3f getBinormalDirection() {
		return binormalDirection;
	}

	public void setBinormalDirection(Vector3f binormalDirection) {
		this.binormalDirection = Maths.copyVector(binormalDirection);
	}
	
	public Vector3f getCentrePosition() {
		return centrePosition;
	}

	public FontType getFont() {
		return font;
	}

	public float getRadius() {
		return radius;
	}

	public float getHeight() {
		return height;
	}

	public int getResourceCollectionSize() {
		return resourceCollectionSize;
	}

	public void setResourceCollectionSize(int resourceCollectionSize) {
		this.resourceCollectionSize = resourceCollectionSize;
	}

	public int getResourceAmountHeld() {
		return resourceAmountHeld;
	}
	
	public String getUnitResourceAmountHeld() {
		if(this.resourceTypeHeld != null) {
			return resourceTypeHeld.getUnitAmount(resourceAmountHeld);
		}else {
			return "NO RESOURCE HELD";
		}
	}

	public void setResourceAmountHeld(int resourceAmountHeld) {
		this.resourceAmountHeld = resourceAmountHeld;
	}
	
	public void addResourceAmountHeld(int resourceAmountAdded) {
		this.resourceAmountHeld += resourceAmountAdded;
	}

	public Resource getResourceTypeHeld() {
		return resourceTypeHeld;
	}

	public void setResourceTypeHeld(Resource resourceTypeHeld) {
		this.resourceTypeHeld = resourceTypeHeld;
	}

	public int getResourceStorageSize() {
		return resourceStorageSize;
	}

	public void setResourceStorageSize(int resourceStorageSize) {
		this.resourceStorageSize = resourceStorageSize;
	}

	public float getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(float maxHealth) {
		this.maxHealth = maxHealth;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	public boolean isDestructible() {
		return isDestructible;
	}

	public void setDestructible(boolean isDestructible) {
		this.isDestructible = isDestructible;
	}

	public boolean isUseBasicPopupDirection() {
		return useBasicPopupDirection;
	}

	public void setUseBasicPopupDirection(boolean useBasicPopupDirection) {
		this.useBasicPopupDirection = useBasicPopupDirection;
	}
	
}
