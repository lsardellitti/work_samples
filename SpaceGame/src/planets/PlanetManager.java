package planets;

import java.util.ArrayList;
import java.util.List;

public class PlanetManager {
	
	private static List<Planet> scenePlanets = new ArrayList<Planet>();
	
	public static void addPlanetToScene(Planet planet) {
		if(!scenePlanets.contains(planet)) {
			scenePlanets.add(planet);
		}
	}
	
	public static void removePlanetFromScene(Planet planet) {
		scenePlanets.remove(planet);
	}

	public static List<Planet> getScenePlanets() {
		return scenePlanets;
	}

}
