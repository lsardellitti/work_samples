package postProcessing;

import shaders.ShaderProgram;

public class MouseOverHighlightShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/postProcessing/simpleVertex.txt";
	private static final String FRAGMENT_FILE = "/postProcessing/mouseOverHighlightFragment.txt";
	
	private int location_colourTexture;
	private int location_entityDepthTexture;
	private int location_sceneDepthTexture;
	private int location_glowTransition;
	
	protected MouseOverHighlightShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_colourTexture = super.getUniformLocation("colourTexture");
		location_entityDepthTexture = super.getUniformLocation("entityDepthTexture");
		location_sceneDepthTexture = super.getUniformLocation("sceneDepthTexture");
		location_glowTransition = super.getUniformLocation("glowTransition");
	}
	
	protected void connectTextureUnits(){
		super.loadInt(location_colourTexture, 0);
		super.loadInt(location_sceneDepthTexture, 1);
		super.loadInt(location_entityDepthTexture, 2);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	public void loadGlowTransition(float glowTransition) {
		super.loadFloat(location_glowTransition, glowTransition);
	}

}
