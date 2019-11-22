package entities;

import org.lwjgl.util.vector.Vector3f;

import toolbox.Maths;

public class Light {
	
	private static final float PLANET_LIGHT_DISTANCE = 5000;
	private static final float SPACE_LIGHT_DISTANCE = 100;
	
	private Vector3f position;
	private Vector3f colour;
	private Vector3f attenuation = new Vector3f(1,0,0);
	private Camera camera;
	
	public Light(Camera camera, Vector3f colour) {
		this.camera = camera;
		this.colour = colour;
		this.position = Vector3f.sub(camera.getPosition(), Maths.normScaledVector(camera.getFacingDirection(), PLANET_LIGHT_DISTANCE), null);;
	}
	
	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
	}
	
	public Light(Vector3f position, Vector3f colour, Vector3f attenuation) {
		this.position = position;
		this.colour = colour;
		this.attenuation = attenuation;
	}
	
	public void followEntity() {//decide light position based on camera type of object following
		if(camera.getDirectionOption() == 0) {
			this.position = Vector3f.add(camera.getCentredEntity().getPosition(), Maths.normScaledVector(camera.getRelativeUpDirection(), PLANET_LIGHT_DISTANCE), null);
		}else {
			this.position = Vector3f.add(camera.getCentredEntity().getPosition(), Maths.normScaledVector(camera.getRelativeUpDirection(), SPACE_LIGHT_DISTANCE), null);
		}
	}
	
	public void followCamera() {
		this.position = Vector3f.sub(camera.getPosition(), Maths.normScaledVector(camera.getFacingDirection(), 0), null);
	}
	
	public Vector3f getAttenuation() {
		return attenuation;
	}

	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	public Vector3f getColour() {
		return colour;
	}
	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
	
}
