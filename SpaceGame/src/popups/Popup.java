package popups;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fontMeshCreator.GUIText;
import fontRendering.Fonts;
import toolbox.Maths;
import transitions.Transition;

public class Popup extends GUIText{
	
	private static final float FONT_SIZE = 2;
	private static final float MAX_LINE = 0.6f;
	private static final float MOVE_TIME = 1;
	private static final float INITIAL_X = 0;
	private static final float INITIAL_Y = 0.3f;
	
	private float initialY = INITIAL_Y;
	private float moveDistance = 0.1f;
	
	private Transition popupTransition = new Transition();
	
	public Popup(String text, String type) {
		super(text, FONT_SIZE, Fonts.candara, new Vector2f(INITIAL_X, INITIAL_Y), MAX_LINE, true);
		//change colour based on use
		if(type == "error") {
			super.setColour(new Vector3f(1,0,0));
		}else if(type == "collect"){
			super.setColour(new Vector3f(0,1,0));
		}else if(type == "store"){
			super.setColour(new Vector3f(0,0,1));
		}
		else {
			super.setColour(new Vector3f(1,1,1));
		}
		popupTransition.startTransition(MOVE_TIME);
		PopupManager.addActivePopup(this);
	}
	
	public Popup(String text, String type, Vector2f initialPosition, float moveDistance, float time) {
		super(text, FONT_SIZE, Fonts.candara, initialPosition, MAX_LINE, true);
		if(type == "error") {
			super.setColour(new Vector3f(1,0,0));
		}else if(type == "collect"){
			super.setColour(new Vector3f(0,1,0));
		}else if(type == "store"){
			super.setColour(new Vector3f(0,0,1));
		}
		else {
			super.setColour(new Vector3f(1,1,1));
		}
		popupTransition.startTransition(time);
		PopupManager.addActivePopup(this);
		this.moveDistance = moveDistance;
	}
	
	public void update() {
		//update position of the popup
		super.getPosition().setY(initialY - moveDistance * popupTransition.getCurrentTime());
		super.setTranslucency(fadeInFunction(popupTransition.getCurrentTime()));
		if(popupTransition.getCurrentTime() >= 1) {
			PopupManager.addFinishedPopup(this);
		}		
	}
	
	private float fadeInFunction(float time) {
		return Maths.clampFloat(-5*time*(time-1), 0, 1);
	}
	
	public void pushUp(float push) {
		initialY += push;
	}
	
	public boolean isDone() {
		return popupTransition.isFinished();
	}

}
