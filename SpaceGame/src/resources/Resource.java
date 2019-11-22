package resources;

import java.util.Arrays;
import java.util.List;

public class Resource {
	
	private static final List<String> RESOURCE_NAMES = Arrays.asList("Water", "Rock", "Diamond");
	private static final List<String> RESOURCE_UNITS = Arrays.asList("L", "g", "g");
	
	private int resourceID;
	private String resourceName;
	private String resourceUnit;

	public Resource(int resourceID) {
		if(resourceID >= 0 && resourceID < RESOURCE_NAMES.size()) {
			this.resourceID = resourceID;
			this.resourceName = RESOURCE_NAMES.get(resourceID);
			this.resourceUnit = RESOURCE_UNITS.get(resourceID);
		}
		else {
			this.resourceID = 0;
			this.resourceName = RESOURCE_NAMES.get(0);
			this.resourceUnit = RESOURCE_UNITS.get(0);
		}
	}
	
	public Resource(String resourceName) {
		if(RESOURCE_NAMES.contains(resourceName)) {
			this.resourceName = resourceName;
			this.resourceID = RESOURCE_NAMES.indexOf(resourceName);
			this.resourceUnit = RESOURCE_UNITS.get(resourceID);
		}
		else {
			this.resourceID = 0;
			this.resourceName = RESOURCE_NAMES.get(0);
			this.resourceUnit = RESOURCE_UNITS.get(0);
		}
	}
	
	public int getResourceID() {
		return resourceID;
	}

	public String getResourceName() {
		return resourceName;
	}

	public static List<String> getResourceNames() {
		return RESOURCE_NAMES;
	}

	public static List<String> getResourceUnits() {
		return RESOURCE_UNITS;
	}
	
	public String getUnitAmount(float amount) {
		if(amount < 1000) {
			return Integer.toString((int) (amount)) + " " + resourceUnit;
		}
		if(amount < 1000000) {
			return Float.toString(amount/1000) + " k" + resourceUnit;
		}
		if(amount < 1000000000) {
			return Float.toString(amount/1000000) + " M" + resourceUnit;
		}
		return Float.toString(amount/1000000000) + " G" + resourceUnit;
	}

}
