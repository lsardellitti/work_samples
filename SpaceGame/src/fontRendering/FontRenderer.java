package fontRendering;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import renderEngine.DisplayManager;

public class FontRenderer {

	private FontShader shader;

	public FontRenderer() {
		shader = new FontShader();
	}
	
	public void render(Map<FontType,List<GUIText>> texts, float translucency, int top, int bottom, int titleTop) {
		prepare();
		shader.loadTranslucentFactors(translucency);
		for (FontType font:texts.keySet()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());
			for(GUIText text : texts.get(font)) {
				if(text.isStationary()) {
					shader.loadPixelBoundaries(top, bottom);
				}else {
					shader.loadPixelBoundaries(titleTop, bottom);
				}
				renderText(text);
			}
			//HERE
		}	
		endRendering();    // this used to be above SEE HERE
	}
	
	public void render(Map<FontType,List<GUIText>> texts, float translucency, int top, int bottom) {
		prepare();
		shader.loadTranslucentFactors(translucency);
		shader.loadPixelBoundaries(top, bottom);
		for (FontType font:texts.keySet()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());
			for(GUIText text : texts.get(font)) {
				renderText(text);
			}
			//HERE
		}	
		endRendering();    // this used to be above SEE HERE
	}
	
	public void render(Map<FontType,List<GUIText>> texts) {
		prepare();
		shader.loadPixelBoundaries(DisplayManager.getHeight(), 0);
		for (FontType font:texts.keySet()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());
			for(GUIText text : texts.get(font)) {
				shader.loadTranslucentFactors(text.getTranslucency());
				renderText(text);
			}
			//HERE
		}	
		endRendering();    // this used to be above SEE HERE
	}

	public void cleanUp(){
		shader.cleanUp();
	}
	
	private void prepare(){
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.start();
	}
	
	private void renderText(GUIText text){
		GL30.glBindVertexArray(text.getMesh());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		shader.loadColour(text.getColour());
		shader.loadTranslation(text.getPosition());
		shader.loadSmoothFactors(text.getWidth(), text.getEdge());
		shader.loadOutlineFactors(text.getBorderWidth(), text.getBorderEdge(), text.getBorderOffset(), text.getBorderColour());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	private void endRendering(){
		shader.stop();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

}
