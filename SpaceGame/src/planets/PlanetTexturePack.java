package planets;

public class PlanetTexturePack {
	
	private PlanetTexture backgroundTexture;
	private PlanetTexture rTexture;
	private PlanetTexture gTexture;
	private PlanetTexture bTexture;
	public PlanetTexturePack(PlanetTexture backgroundTexture, PlanetTexture rTexture, PlanetTexture gTexture,PlanetTexture bTexture) {
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}
	public PlanetTexture getBackgroundTexture() {
		return backgroundTexture;
	}
	public PlanetTexture getrTexture() {
		return rTexture;
	}
	public PlanetTexture getgTexture() {
		return gTexture;
	}
	public PlanetTexture getbTexture() {
		return bTexture;
	}

}
