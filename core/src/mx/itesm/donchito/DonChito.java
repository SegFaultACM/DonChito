package mx.itesm.donchito;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class DonChito extends Game {
    public static final float ANCHO_MUNDO = 1280;
	public static final float ALTO_MUNDO = 720;

    public  static final AssetManager assetManager = new AssetManager();
	public static Preferences preferences;
	@Override
	public void create() {
		DonChito.assetManager.setLoader(TiledMap.class,
                new TmxMapLoader(new InternalFileHandleResolver()));
		preferences = Gdx.app.getPreferences(Constants.GLOBAL_PREFERENCES);
		this.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.MENU, this));
	}

	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);
	}
    @Override
    public void dispose() {
        super.dispose();
        DonChito.assetManager.clear();
    }
	public void initPref(){
		DonChito.preferences.putBoolean("RomanStruggle",false);
		DonChito.preferences.putBoolean("Livermorio",false);

	}
}
