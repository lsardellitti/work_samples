package planets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import toolbox.Maths;

public class Planet {
	
	private static final float MAX_HEIGHT_PERCENT = 0.2f;
	private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;
	private static final float VERTEX_DENSITY = 0.5f;
	private static final int MAX_VERTEXES = 150;
	
	private float gravity=50;
	private float radius;
	private int vertexCount;
	
	private Vector3f position;
	private RawModel model;
	private PlanetTexturePack texturePack;
	private PlanetTexture blendMap;
	
	private float[][] distances;
	private Vector3f[][] intermNormals;
	
	public Planet(Vector3f position, float radius, Loader loader, PlanetTexturePack texturePack,PlanetTexture blendMap, String heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.position = position;
		this.radius = radius;
		this.vertexCount = (int) ((2 * Math.PI * radius) * VERTEX_DENSITY);
		if(this.vertexCount > MAX_VERTEXES) {
			this.vertexCount = MAX_VERTEXES;
		}
		this.model = generatePlanet(loader,heightMap);
		PlanetManager.addPlanetToScene(this);
	}
	
	private RawModel generatePlanet(Loader loader,String heightMap){
		//using height map, create planet verticies, stored in an array, indexed by spherical coordinades [theta],[phi]
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("res/"+ heightMap + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		distances = new float[vertexCount][vertexCount];	
		intermNormals = new Vector3f[vertexCount][vertexCount];
		int count = vertexCount * vertexCount;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(vertexCount-1)*(vertexCount)];
		int vertexPointer = 0;
		//calculating conversions from array inicies to angles
		double thetaIncrement = 2 * Math.PI / (vertexCount-1);
		double phiIncrement = Math.PI / (vertexCount-1);
		double theta=0;
		double phi= (float) (-Math.PI/2);
		
		float xFactor;
		float yFactor;
		float zFactor;
		for(int i=0;i<vertexCount;i++){
			phi= (float) (-Math.PI/2);
			for(int j=0;j<vertexCount;j++){
				if(i != vertexCount - 1) {
					float height = getHeight(i,j,image);
					distances[i][j] = height + radius;
					//use angles to convert to x,y,z position
					xFactor = (float) (Math.cos(phi) * Math.cos(theta));
					yFactor = (float) (Math.cos(phi) * Math.sin(theta));
					zFactor = (float) (Math.sin(phi));
				}
				else {
					distances[i][j] = distances[0][j];
					xFactor = (float) (Math.cos(phi));
					yFactor = 0;
					zFactor = (float) (Math.sin(phi));
				}
				//convert to proper distance from origin
				vertices[vertexPointer*3] = distances[i][j] * xFactor;
				vertices[vertexPointer*3+1] = distances[i][j] * yFactor;
				vertices[vertexPointer*3+2] = distances[i][j] * zFactor;
				intermNormals[i][j] = new Vector3f(xFactor, yFactor, zFactor);
//				Vector3f normal = new Vector3f(xFactor, yFactor, zFactor);
//				normal.normalise();
//				normals[vertexPointer*3] = normal.x;
//				normals[vertexPointer*3+1] = normal.y;
//				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = 1 - (float)i/((float)vertexCount - 1);
				textureCoords[vertexPointer*2+1] = (float)j/((float)vertexCount - 1);
				vertexPointer++;
				phi+=phiIncrement;
			}
			theta+=thetaIncrement;
		}
		vertexPointer = 0;
		for(int i=0;i<vertexCount;i++){
			for(int j=0;j<vertexCount;j++){
				Vector3f normal = calculateNormal(i, j);
				normal.normalise();
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				vertexPointer++;
			}
		}
		int pointer = 0;
		int topLeft;
		int bottomLeft;
		int topRight;
		int bottomRight;
		for(int index=0;index<(vertexCount-1)*(vertexCount);index++){
			topLeft = index+1;
			bottomLeft = index;
			topRight = index + vertexCount + 1;
			bottomRight = index + vertexCount;
			indices[pointer++] = topLeft;
			indices[pointer++] = bottomLeft;
			indices[pointer++] = topRight;
			indices[pointer++] = topRight;
			indices[pointer++] = bottomLeft;
			indices[pointer++] = bottomRight;
		}
		
		return loader.loadtoVAO(vertices, textureCoords, normals, indices);
	}

	private float getHeight(int x, int y, BufferedImage image) {
		int imageX = (int) (image.getHeight() * ((float)(x)/ (float)(vertexCount)));
		int imageY = (int) (image.getHeight() * ((float)(y)/ (float)(vertexCount)));
		float height = image.getRGB(imageX, imageY);
		height += MAX_PIXEL_COLOUR/2f;
		height /= MAX_PIXEL_COLOUR/2f;
		height *= MAX_HEIGHT_PERCENT * radius;
		return height;
	}
	
	public float getOnPlanetHeight(float theta, float inPhi) {
		//retrieve hight of planet at given spherical coordinate [theta],[phi]
		float phi = (float) (inPhi + Math.PI/2);
		float iFloat = (float) (theta*(vertexCount-1)/(2*Math.PI));
		float jFloat = (float) (phi*(vertexCount-1)/Math.PI);
		int i = (int) iFloat;
		if(i == vertexCount-1) {
			i--;
		}
		int j = (int) jFloat;
		if(j == vertexCount-1) {
			j--;
		}
		//calculate correct index value in the height array
		float iCoord = iFloat % 1;
		float jCoord = jFloat % 1;
		float answer;
		if (iCoord <= (1-jCoord)) {
			answer = Maths.barryCentric(new Vector3f(0, distances[i][j], 0), new Vector3f(1,
							distances[i + 1][j], 0), new Vector3f(0,
									distances[i][j + 1], 1), new Vector2f(iCoord, jCoord));
		} else {
			answer = Maths.barryCentric(new Vector3f(1, distances[i + 1][j], 0), new Vector3f(1,
					distances[i + 1][j + 1], 1), new Vector3f(0,
							distances[i][j + 1], 1), new Vector2f(iCoord, jCoord));
		}
		return answer;
	}
	
	private Vector3f calculateNormal(int i, int j) {
		Vector3f standardNormal;
		if(j == 0) {
			return new Vector3f(0,0,-1);
		}
		else if (j == vertexCount - 1) {
			return new Vector3f(0,0,1);
		}
		else {
			
			float leftHeight;
			if(i != 0) {
				leftHeight = distances[i-1][j];
			}
			else {
				leftHeight = distances[vertexCount-1][j];
			}
			float rightHeight;
			if(i != vertexCount - 1) {
				rightHeight = distances[i+1][j];
			}
			else {
				rightHeight = distances[0][j];
			}
			float downHeight = distances[i][j-1];
			float upHeight = distances[i][j+1];
			standardNormal = new Vector3f(leftHeight-rightHeight,2f,downHeight-upHeight);
			Vector3f normalCross = Vector3f.cross(new Vector3f(0,1,0), standardNormal, null);
			float normalAngle = Vector3f.angle(new Vector3f(0,1,0), standardNormal);
			if(Maths.isVectorZero(normalCross)) {
				return intermNormals[i][j];
			}
			else {
				return Maths.rotateVector(intermNormals[i][j], normalCross, normalAngle);
			}
		}
	}
	
	
	public float getGravity() {
		return gravity;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public RawModel getModel() {
		return model;
	}

	public PlanetTexturePack getTexturePack() {
		return texturePack;
	}

	public PlanetTexture getBlendMap() {
		return blendMap;
	}

	public float getRadius() {
		return radius;
	}

}
