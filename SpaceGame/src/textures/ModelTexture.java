package textures;

public class ModelTexture {

	private int textureID;
	private int normalMap;
	private int specularMap;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean hasTransperancy = false;
	private boolean useFakeLighting = false;
	private boolean hasSpecularMap = false;
	
	private int numberOfRows = 1;

	public ModelTexture(int id) {
		this.textureID = id;
	}
	
	public void setSpecularMap(int specMap) {
		this.specularMap = specMap;
		this.hasSpecularMap = true;
	}
	
	public boolean hasSpecularMap() {
		return hasSpecularMap;
	}
	
	public int getSpecularMap() {
		return specularMap;
	}
	
	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}
	
	public int getNormalMap() {
		return normalMap;
	}

	public void setNormalMap(int normalMap) {
		this.normalMap = normalMap;
	}

	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}
	
	public boolean isHasTransperancy() {
		return hasTransperancy;
	}

	public void setHasTransperancy(boolean hasTransperancy) {
		this.hasTransperancy = hasTransperancy;
	}

	public int getID() {
		return this.textureID;
	}

	public int getTextureID() {
		return textureID;
	}

	public void setTextureID(int textureID) {
		this.textureID = textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	
}
