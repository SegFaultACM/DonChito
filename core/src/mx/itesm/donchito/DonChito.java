package mx.itesm.donchito;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class DonChito extends Game {
	public static final float ALTO_MUNDO = 720;
	public static final float ANCHO_MUNDO = 1280;
	@Override
	public void create() {
		this.setScreen(new MenuPrincipal(this));
	}

	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);
	}
}
