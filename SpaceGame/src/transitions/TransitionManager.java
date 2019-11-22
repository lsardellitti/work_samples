package transitions;

import java.util.ArrayList;
import java.util.List;

public class TransitionManager {
	
	private static List<Transition> activeTransitions;
	private static List<Transition> finishedTransitions;
	private static boolean isPaused;
	private static boolean hasRemovableTransitions;
	
	public static void init() {
		activeTransitions = new ArrayList<Transition>();
		finishedTransitions = new ArrayList<Transition>();
		isPaused = false;
		hasRemovableTransitions = false;
	}
	
	public static void addFinihsedTransition(Transition transition) {
		finishedTransitions.add(transition);
		hasRemovableTransitions = true;
	}
	
	public static void addActiveTransition(Transition transition) {
		activeTransitions.add(transition);
	}
	
	public static void removeActiveTransition(Transition transition) {
		activeTransitions.remove(transition);
	}
	
	public static void pauseTransitions() {
		isPaused = true;
	}
	
	public static void unpauseTransitions() {
		isPaused = false;
	}
	
	public static void update() {
		if(!isPaused) {
			for(Transition transition:activeTransitions) {
				transition.update();
			}
			if(hasRemovableTransitions) {
				for(Transition transition:finishedTransitions) {
					activeTransitions.remove(transition);
				}
				finishedTransitions.clear();
				hasRemovableTransitions = false;
			}
		}
	}
	
	

}
