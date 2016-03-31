package mx.itesm.donchito;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

public class DonChito extends Game {
	public static final float ALTO_MUNDO = 720;
	public static final float ANCHO_MUNDO = 1280;
	private static final AssetManager assetManager = new AssetManager();
	@Override
	public void create() {
		this.setScreen(new LivermorioEscape(this));
	}

	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);
	}
	public static AssetManager getAssetManager() {
		return assetManager;
	}
}
