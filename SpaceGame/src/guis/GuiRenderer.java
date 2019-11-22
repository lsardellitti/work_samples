package guis;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import toolbox.Maths;

public class GuiRenderer {
	
	private final RawModel quad;
	private GuiShader shader;
	
	public GuiRenderer(Loader loader) {
		float[] positions = {-1,1,-1,-1,1,1,1,-1};		
		quad = loader.loadToVAO(positions,2);
		shader = new GuiShader();
		shader.start();
		shader.connectTextureUnits();
		shader.stop();
	}
	
	public void render(List<GuiTexture> guis) {
		shader.start();
		shader.loadPixelBoundaries(DisplayManager.getHeight(), 0);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for (GuiTexture gui:guis) {
			shader.loadTranslucentFactors(gui.getTranslucnecy());
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			if(gui.hasHealthMap()) {
				GL13.glActiveTexture(GL13.GL_TEXTURE1);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getHealthTexture());
				shader.loadHealthMapFactors(true, gui.getHealthFactor());
			}else {
				shader.loadHealthMapFactors(false, 0);
			}
			Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getRotation(), gui.getScale());
			shader.loadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void render(List<GuiTexture> guis, float translucency, int top, int bottom) {
		shader.start();
		shader.loadTranslucentFactors(translucency);
		shader.loadPixelBoundaries(top, bottom);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for (GuiTexture gui:guis) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			if(gui.hasHealthMap()) {
				GL13.glActiveTexture(GL13.GL_TEXTURE1);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getHealthTexture());
				shader.loadHealthMapFactors(true, gui.getHealthFactor());
			}else {
				shader.loadHealthMapFactors(false, 0);
			}
			Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
			shader.loadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void render(List<GuiTexture> guis, float translucency, int top, int bottom, int titleTop) {
		shader.start();
		shader.loadTranslucentFactors(translucency);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for (GuiTexture gui:guis) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			if(gui.isStationary()) {
				shader.loadPixelBoundaries(top, bottom);
			}else {
				shader.loadPixelBoundaries(titleTop, bottom);
			}
			if(gui.hasHealthMap()) {
				GL13.glActiveTexture(GL13.GL_TEXTURE1);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getHealthTexture());
				shader.loadHealthMapFactors(true, gui.getHealthFactor());
			}else {
				shader.loadHealthMapFactors(false, 0);
			}
			Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
			shader.loadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}

}
