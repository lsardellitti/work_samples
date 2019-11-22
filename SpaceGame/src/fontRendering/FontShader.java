package fontRendering;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import shaders.ShaderProgram;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE = "/fontRendering/fontVertex.txt";
	private static final String FRAGMENT_FILE = "/fontRendering/fontFragment.txt";
	
	
	private int location_colour;
	private int location_translation;
	private int location_width;
	private int location_edge;
	private int location_borderWidth;
	private int location_borderEdge;
	private int location_borderColour;
	private int location_borderOffset;
    private int location_translucentFactor;
    private int location_topPixel;
    private int location_bottomPixel;
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_colour = super.getUniformLocation("colour");
		location_translation = super.getUniformLocation("translation");
		location_width = super.getUniformLocation("width");
		location_edge = super.getUniformLocation("edge");
		location_borderWidth = super.getUniformLocation("borderWidth");
		location_borderEdge = super.getUniformLocation("borderEdge");
		location_borderColour = super.getUniformLocation("outlineColour");
		location_borderOffset = super.getUniformLocation("offset");
        location_translucentFactor = super.getUniformLocation("translucentFactor");
        location_topPixel = super.getUniformLocation("topPixel");
        location_bottomPixel = super.getUniformLocation("bottomPixel");
	}

	@Override

	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");

	}
	
	public void loadPixelBoundaries(int top, int bottom) {
		super.loadInt(location_topPixel, top);
		super.loadInt(location_bottomPixel, bottom);
	}
	
    public void loadTranslucentFactors(float translucentFactor) {
    	super.loadFloat(location_translucentFactor, translucentFactor);
    }
	
	protected void loadOutlineFactors(float borderWidth, float borderEdge, Vector2f offset, Vector3f outlineColour) {
		super.loadFloat(location_borderWidth, borderWidth);
		super.loadFloat(location_borderEdge, borderEdge);
		super.load2DVector(location_borderOffset, offset);
		super.loadVector(location_borderColour, outlineColour);
	}
	
	protected void loadSmoothFactors(float width, float edge) {
		super.loadFloat(location_width, width);
		super.loadFloat(location_edge, edge);
	}
	
	protected void loadColour(Vector3f colour) {
		super.loadVector(location_colour, colour);
	}
	
	protected void loadTranslation(Vector2f translation) {
		super.load2DVector(location_translation, translation);
	}


}
