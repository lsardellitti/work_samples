package menus;

import java.util.ArrayList;
import java.util.List;

import fontMeshCreator.GUIText;
import guis.GuiTexture;
import toolbox.Maths;

//creates layer which GUIs and Text can be added to in a menu
public class MenuLayer {
	
	private Menu menu;
	private float scrollPosition = 0;
	private float totalScroll = 0;
	
	private List<GuiTexture> guis = new ArrayList<GuiTexture>();
	private List<GUIText> texts = new ArrayList<GUIText>();
	
	public MenuLayer(Menu menu) {
		this.menu = menu;
		menu.addLayer(this);
	}
	
	public void verticalScroll(float dy) {
		scrollPosition += dy;
		if(scrollPosition > 0) {
			dy = dy - scrollPosition;
			scrollPosition = 0;
		}
		if(scrollPosition < -totalScroll) {
			dy = dy - (scrollPosition + totalScroll);
			scrollPosition = -totalScroll;
		}
		for(GuiTexture gui : guis) {
			gui.verticalScroll(-dy);
		}
		for(GUIText text : texts) {
			text.verticalScroll(-dy);
		}
	}
		
	public float getScrollPosition() {
		return scrollPosition;
	}

	public void setTotalScroll(float totalScroll) {
		if(totalScroll > 0) {
			this.totalScroll = totalScroll;
			new Scrollbar(StandardMenuTextures.white, this, Maths.clampFloat(0.1f/totalScroll, 0.2f, 1));
		}
	}

	public float getTotalScroll() {
		return totalScroll;
	}

	public void addGui(GuiTexture gui) {
		guis.add(gui);
	}
	
	public void removeGui(GuiTexture gui) {
		guis.remove(gui);
	}
	
	public void addText(GUIText text) {
		texts.add(text);
	}
	
	public void removeText(GUIText text) {
		texts.remove(text);
	}
	
	public Menu getMenu() {
		return menu;
	}

	public List<GuiTexture> getGuis() {
		return guis;
	}
	public List<GUIText> getTexts() {
		return texts;
	}
	
}
