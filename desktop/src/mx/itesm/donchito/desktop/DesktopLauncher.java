package mx.itesm.donchito.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import mx.itesm.donchito.DonChito;

public class DesktopLauncher {
    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT =720;
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Don Chito";
		cfg.addIcon("Imagenes/iconos/ic_launcher.png", Files.FileType.Internal);
		cfg.width = SCREEN_WIDTH;
		cfg.height = SCREEN_HEIGHT;
		new LwjglApplication(new DonChito(), cfg);

	}
}
