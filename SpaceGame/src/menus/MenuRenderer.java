package menus;

import java.util.List;

import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import renderEngine.Loader;

public class MenuRenderer {
	
	private static final float GUI_TRANSLUCENCY = 0.6f;
	private static final float TEXT_TRANSLUCENCY = 0.9f;
	
	private GuiRenderer guiRenderer;
	private TextMaster textMaster;
	
	public MenuRenderer(Loader loader) {
		this.guiRenderer = new GuiRenderer(loader);
		this.textMaster = new TextMaster(loader);
	}
	
	public void render(List<Menu> menus) {
		for (Menu menu : menus) {
			
			guiRenderer.render(menu.getBaseLayer().getGuis(), GUI_TRANSLUCENCY, menu.getTopPixel(), menu.getBottomPixel());
			for (GUIText text : menu.getBaseLayer().getTexts()) {
				text.load(textMaster);
			}
			textMaster.render(TEXT_TRANSLUCENCY, menu.getTopPixel(), menu.getBottomPixel());
			textMaster.clearText();
			
			guiRenderer.render(menu.getActiveLayer().getGuis(), GUI_TRANSLUCENCY, menu.getTopPixel(), menu.getBottomPixel(), menu.getTitleTopPixel());
			for (GUIText text : menu.getActiveLayer().getTexts()) {
				text.load(textMaster);
			}
			textMaster.render(TEXT_TRANSLUCENCY, menu.getTopPixel(), menu.getBottomPixel(), menu.getTitleTopPixel());
			textMaster.clearText();
		
			guiRenderer.render(menu.getTabLayer().getGuis(), GUI_TRANSLUCENCY, menu.getTopPixel(), menu.getBottomPixel());
			for (GUIText text : menu.getTabLayer().getTexts()) {
				text.load(textMaster);
			}
			textMaster.render(TEXT_TRANSLUCENCY, menu.getTopPixel(), menu.getBottomPixel());
			textMaster.clearText();
		}
	}
	
	public void cleanUp() {
		textMaster.cleanUp();
		guiRenderer.cleanUp();
	}

}
