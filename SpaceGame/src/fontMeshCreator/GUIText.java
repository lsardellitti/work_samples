package fontMeshCreator;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fontRendering.TextMaster;
import guis.GuiTexture;
import menus.MenuLayer;
import toolbox.Maths;

public class GUIText {

	private String textString;
	private float fontSize;
	private float width;
	private float edge;
	private float translucency = 1;
	private boolean stationary;
	
	private float borderWidth = 0.0f;
	private float borderEdge = 0.4f;
	private Vector2f borderOffset = new Vector2f(0f,0f);
	private Vector3f borderColour = new Vector3f(0f,0f,0f);

	private int textMeshVao;
	private int vertexCount;
	private Vector3f colour = new Vector3f(0f, 0f, 0f);

	private Vector2f position;
	private float lineMaxSize;
	private int numberOfLines;

	private FontType font;

	private boolean centerText = false;

	public GUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
			boolean centered) {
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.position = position;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
		this.width = 0.50f + fontSize/100;
		this.edge = 0.1f * (3.0f / fontSize);	
	}
	
	public GUIText(String text, float fontSize, GuiTexture gui, Vector2f relativePosition, float maxLineLength,
			boolean centered) {
		this.textString = text;
		MenuLayer layer = gui.getLayer();
		this.fontSize = fontSize*gui.getScale().y;
		this.font = layer.getMenu().getFont();
		this.position = Vector2f.add(Maths.scaledComponentwiseVector(relativePosition, gui.getScale()), gui.getPosition(), null);
		this.position = Maths.convertToTopLeft(position);
		this.lineMaxSize = gui.getScale().x*maxLineLength;
		this.centerText = centered;
		this.width = 0.50f + fontSize/100;
		this.edge = 0.1f * (3.0f / fontSize);
		this.stationary = gui.isStationary();
		layer.addText(this);
	}
	
	public void verticalScroll(float dy) {
		if(!stationary) {
			this.position.y -= dy/2;
		}
	}
	
	// load text	
	public void load(TextMaster textMaster) {
		textMaster.loadText(this);
	}
	
	public void remove(TextMaster textMaster) {
		textMaster.removeText(this);
		// remove text
	}
	
	public void setBorderSettings(float width, float edge, Vector2f offset, Vector3f colour) {
		this.borderWidth = width;
		this.borderEdge = edge;
		this.borderOffset = offset;
		this.borderColour = colour;
	}

	public void setTextString(String textString) {
		this.textString = textString;
	}

	public float getBorderWidth() {
		return borderWidth;
	}

	public float getBorderEdge() {
		return borderEdge;
	}

	public Vector2f getBorderOffset() {
		return borderOffset;
	}

	public Vector3f getBorderColour() {
		return borderColour;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getEdge() {
		return edge;
	}

	public void setEdge(float edge) {
		this.edge = edge;
	}

	public FontType getFont() {
		return font;
	}

	public void setColour(float r, float g, float b) {
		colour.set(r, g, b);
	}
	
	public void setColour(Vector3f colour) {
		this.colour.set(colour.x, colour.y, colour.z);
	}

	public Vector3f getColour() {
		return colour;
	}

	public int getNumberOfLines() {
		return numberOfLines;
	}

	public Vector2f getPosition() {
		return position;
	}

	public int getMesh() {
		return textMeshVao;
	}

	public void setMeshInfo(int vao, int verticesCount) {
		this.textMeshVao = vao;
		this.vertexCount = verticesCount;
	}

	public int getVertexCount() {
		return this.vertexCount;
	}

	protected float getFontSize() {
		return fontSize;
	}

	protected void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}

	protected boolean isCentered() {
		return centerText;
	}

	protected float getMaxLineSize() {
		return lineMaxSize;
	}

	protected String getTextString() {
		return textString;
	}

	public float getTranslucency() {
		return translucency;
	}

	public void setTranslucency(float translucency) {
		this.translucency = translucency;
	}

	public boolean isStationary() {
		return stationary;
	}
	

}
