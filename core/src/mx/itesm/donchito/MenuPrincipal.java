package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
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

    private Sprite botonInicio;
    private Texture texturabtnInicio;

    private Sprite botonDonChito;
    private Texture texturaDonChito;

    public MenuPrincipal(DonChito game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();
        view = new FitViewport(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO,camera);
        batch = new SpriteBatch();

        //TODO Refactor next code into an Asset Manager
        texturaFondo = new Texture(Gdx.files.internal("Imagenes/Menuprincipal/menuPrincipal.jpg"));
        texturabtnInicio = new Texture(Gdx.files.internal("Imagenes/Menuprincipal/cargarpartida.png"));
        texturaDonChito = new Texture(Gdx.files.internal("Imagenes/Menuprincipal/carteldonchito.png"));

        botonInicio = new Sprite(texturabtnInicio);
        botonInicio.setPosition(870,290);

        botonDonChito = new Sprite(texturaDonChito);
        botonDonChito.setPosition(430,230);
        botonDonChito.setRotation(23);


        fondo = new Sprite(texturaFondo);

        leerEntrada();
        cargarAudio();
    }

    private void leerEntrada() {

    }

    private void cargarAudio() {
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("menuPrincipal.mp3"));
        musicaFondo.setLooping(true);
        musicaFondo.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        fondo.draw(batch);
        botonInicio.draw(batch);
        botonDonChito.draw(batch);

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
