package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by joell on 2/12/2016.
 */
public class MenuPrincipal implements Screen {
    private OrthographicCamera camera;
    private final DonChito game;
    private Viewport view;


    //TODO Refactor to interface
    private Texture texturaFondo;
    private Sprite fondo;
    private SpriteBatch batch;
    private Music musicaFondo;

    public MenuPrincipal(DonChito game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();
        view = new FitViewport(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO,camera);

        //TODO Refactor next code into an Asset Manager
        batch = new SpriteBatch();
        texturaFondo = new Texture(Gdx.files.internal("Imagenes/Menuprincipal/MenuPrincipal.jpg"));

        fondo = new Sprite(texturaFondo);
        cargarAudio();
    }

    private void cargarAudio() {
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("Musica/menuPrincipal.mp3"));
        musicaFondo.setLooping(true);
        musicaFondo.play();
    }

    @Override
    public void render(float delta) {

        batch.begin();
        fondo.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        view.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
