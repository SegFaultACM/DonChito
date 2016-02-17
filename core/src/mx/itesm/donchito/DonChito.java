package mx.itesm.donchito;


import com.badlogic.gdx.Game;

public class DonChito extends Game {
	public static final float ALTO_MUNDO = 720;
	public static final float ANCHO_MUNDO = 1280;
	@Override
	public void create() {
		this.setScreen(new FlevorioSays(this));
	}
}
