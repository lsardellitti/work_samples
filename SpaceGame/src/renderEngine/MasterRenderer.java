package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import planets.Planet;
import planets.PlanetRenderer;
import planets.PlanetShader;
import shaders.StaticShader;
import skybox.SkyboxRenderer;

public class MasterRenderer {
	
	
	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 4000f;
	
	public static final float RED = 0;
	public static final float GREEN = 0;
	public static final float BLUE = 0;
	
	private static final float FOG_DENSITY = 0.0004f;
	private static final float FOG_GRADIENT = 10f;
	private static final float AMBIENT_LIGHT = 0.3f;
	
	private static Matrix4f projectionMatrix;
	
	private static StaticShader shader = new StaticShader();
	private static EntityRenderer renderer;
	
	private static PlanetRenderer planetRenderer;
	private static PlanetShader planetShader = new PlanetShader();
	
	private static Map<TexturedModel,List<Entity>> entities = new HashMap<TexturedModel,List<Entity>>();
	private static List<Planet> planets = new ArrayList<Planet>();
	
	private static SkyboxRenderer skyboxRenderer;
	
	public static void init(Loader loader) {
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader,projectionMatrix);
		planetRenderer = new PlanetRenderer(planetShader,projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader,projectionMatrix);
	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public static void renderScene(List<Entity> entities,List<Planet> planets,List<Light> lights, Camera camera, Vector4f clipPlane) {
		if (clipPlane == null) {
			clipPlane = new Vector4f(0,0,0,0);
		}
		for (Entity entity:entities) {
			processEntity(entity);
		}
		
		if (planets != null) {
			for(Planet planet:planets) {
				processPlanet(planet);
			}
		}
		
		render(lights,camera,clipPlane, true);
	}
	
	public static void renderScene(Entity entity,List<Light> lights, Camera camera, Vector4f clipPlane) {
		if (clipPlane == null) {
			clipPlane = new Vector4f(0,0,0,0);
		}
		if (entity != null) {
			processEntity(entity);
		}
		render(lights,camera,clipPlane, false);
	}
	
	public static void render(List<Light> lights, Camera camera, Vector4f clipPlane, boolean useSkybox) {
		prepare();
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadFog(FOG_DENSITY, FOG_GRADIENT);
		shader.loadLights(lights);
		shader.loadAmbientLight(AMBIENT_LIGHT);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		
		planetShader.start();
		planetShader.loadClipPlane(clipPlane);
		planetShader.loadSkyColour(RED, GREEN, BLUE);
		planetShader.loadFog(FOG_DENSITY, FOG_GRADIENT);
		planetShader.loadLights(lights);
		planetShader.loadAmbientLight(AMBIENT_LIGHT);
		planetShader.loadViewMatrix(camera);
		planetRenderer.render(planets);
		planetShader.stop();	
		if (useSkybox) {
		skyboxRenderer.render(camera,RED, GREEN, BLUE);
		}
		planets.clear();
		entities.clear();
		
	}
	
	public static void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
	}
	
	public static void processPlanet(Planet planet) {
		planets.add(planet);
	}
	
	public static void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch!=null) {
			batch.add(entity);
		}else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}	
	
	public static Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	 private static void createProjectionMatrix(){
	    	projectionMatrix = new Matrix4f();
			float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
			float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
			float x_scale = y_scale / aspectRatio;
			float frustum_length = FAR_PLANE - NEAR_PLANE;

			projectionMatrix.m00 = x_scale;
			projectionMatrix.m11 = y_scale;
			projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
			projectionMatrix.m23 = -1;
			projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
			projectionMatrix.m33 = 0;
	    }
	 
	public static void cleanUp() {
		planetShader.cleanUp();
		shader.cleanUp();
	}
}
