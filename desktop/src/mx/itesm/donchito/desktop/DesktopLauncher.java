package mx.itesm.donchito.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import mx.itesm.donchito.DonChito;

public class DesktopLauncher {
    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT =720;
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new DonChito(), config);
		config.width = SCREEN_WIDTH;
        config.height = SCREEN_HEIGHT;
	}
}
