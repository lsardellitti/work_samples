package postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class MouseOverHighlight {
	
	private ImageRenderer renderer;
	private MouseOverHighlightShader shader;
	
	public MouseOverHighlight(){
		shader = new MouseOverHighlightShader();
		shader.start();
		shader.connectTextureUnits();
		shader.stop();
		renderer = new ImageRenderer();
	}
	
	public void render(int colourTexture, int sceneDepthTexture, int entityDepthTexture, float glowTransition){
		shader.start();
		shader.loadGlowTransition(glowTransition);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colourTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, sceneDepthTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, entityDepthTexture);
		renderer.renderQuad();
		shader.stop();
	}
	
	public int getOutputTexture(){
		return renderer.getOutputTexture();
	}
	
	public void cleanUp(){
		renderer.cleanUp();
		shader.cleanUp();
	}
	

}
