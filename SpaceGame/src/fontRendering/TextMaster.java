package fontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.TextMeshData;
import renderEngine.Loader;

public class TextMaster {
	
	private Loader loader;
	private Map<FontType,List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private FontRenderer renderer;
	
	public TextMaster(Loader theLoader) {
		renderer = new FontRenderer();
		loader = theLoader;
	}
	
	public void render() {
		renderer.render(texts);
	}
	
	public void render(float translucency, int top, int bottom) {
		renderer.render(texts, translucency, top, bottom);
	}
	
	public void render(float translucency, int top, int bottom, int titleTop) {
		renderer.render(texts, translucency, top, bottom, titleTop);
	}
	
	public void loadText(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadTextToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		if(textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(font,textBatch);
		}
		textBatch.add(text);
	}
	
	public void removeText(GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if(textBatch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}
	
	public void clearText() {
		loader.cleanLoadedText();
		texts.clear();
	}
	
	public void cleanUp() {
		renderer.cleanUp();
	}
}
