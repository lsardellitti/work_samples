package models;

import org.lwjgl.util.vector.Vector3f;

import textures.ModelTexture;

//stores model information, including default directions
public class TexturedModel {

	private RawModel rawModel;
	private ModelTexture texture;
	private Vector3f upwardDirection;
	private Vector3f forwardDirection;
	private float radius;
	private float shootingRadius;
	private float height;
	
	
	public TexturedModel(RawModel model, ModelTexture texture) {
		this.rawModel = model;
		this.texture = texture;
	}
	
	public TexturedModel(RawModel model, ModelTexture texture, Vector3f upwardDirection, Vector3f forwardDirection, float radius, float height) {
		this.rawModel = model;
		this.texture = texture;
		this.upwardDirection = upwardDirection.normalise(null);
		this.forwardDirection = forwardDirection.normalise(null);
		this.radius = radius;
		this.shootingRadius = radius;
		this.height = height;
	}
	
	public TexturedModel(RawModel model, ModelTexture texture, Vector3f upwardDirection, Vector3f forwardDirection, float radius, float height, float shootingRadius) {
		this.rawModel = model;
		this.texture = texture;
		this.upwardDirection = upwardDirection.normalise(null);
		this.forwardDirection = forwardDirection.normalise(null);
		this.radius = radius;
		this.shootingRadius = shootingRadius;
		this.height = height;
	}
	
	public Vector3f getForwardDirection() {
		return forwardDirection;
	}
	
	public Vector3f getUpwardDirection() {
		return upwardDirection;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}

	public float getRadius() {
		return radius;
	}

	public float getHeight() {
		return height;
	}

	public float getShootingRadius() {
		return shootingRadius;
	}

}
