package guis;
 
import org.lwjgl.util.vector.Matrix4f;
 
import shaders.ShaderProgram;
 
public class GuiShader extends ShaderProgram{
     
    private static final String VERTEX_FILE = "/guis/guiVertexShader.txt";
    private static final String FRAGMENT_FILE = "/guis/guiFragmentShader.txt";
     
    private int location_transformationMatrix;
    private int location_translucentFactor;
    private int location_topPixel;
    private int location_bottomPixel;
    private int location_guiTexture;
    private int location_healthMap;
    private int location_hasHealthMap;
    private int location_healthFactor;
 
    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }
 
    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_translucentFactor = super.getUniformLocation("translucentFactor");
        location_topPixel = super.getUniformLocation("topPixel");
        location_bottomPixel = super.getUniformLocation("bottomPixel");
        location_guiTexture = super.getUniformLocation("guiTexture");
        location_healthMap = super.getUniformLocation("healthMap");
        location_hasHealthMap = super.getUniformLocation("hasHealthMap");
        location_healthFactor = super.getUniformLocation("healthFactor");
    }
    
    public void loadHealthMapFactors(boolean hasHealthMap, float healthFactor) {
    	super.loadBoolean(location_hasHealthMap, hasHealthMap);
    	super.loadFloat(location_healthFactor, healthFactor);
    }
    
	public void connectTextureUnits() {
		super.loadInt(location_guiTexture, 0);
		super.loadInt(location_healthMap, 1);
	}
    
    public void loadPixelBoundaries(int top, int bottom) {
    	super.loadInt(location_topPixel, top);
    	super.loadInt(location_bottomPixel, bottom);
    }
    
    public void loadTranslucentFactors(float translucentFactor) {
    	super.loadFloat(location_translucentFactor, translucentFactor);
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
     
     
     
 
}