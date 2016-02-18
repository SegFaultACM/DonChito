package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by joell on 2/12/2016.
 */
public class MenuPrincipal implements Screen {
    private OrthographicCamera camera;
    private final DonChito game;
    private Viewport view;


    //TODO Refactor to interface
    private SpriteBatch batch;

    private Music musicaFondo;

    private SimpleAsset btnInicio;
    private SimpleAsset donChitoBtn;
    private SimpleAsset fondo;



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
        fondo = new SimpleAsset("Imagenes/Menuprincipal/menuPrincipal.jpg",new Vector2(0,0));
        btnInicio = new SimpleAsset("Imagenes/Menuprincipal/cargarpartida.png",new Vector2(870,290));
        donChitoBtn = new SimpleAsset("Imagenes/Menuprincipal/carteldonchito.jpg",new Vector2(420,230));
        leerEntrada();
        //cargarAudio();
    }

    private void leerEntrada() {

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int x, int y, int pointexr, int button) {
                Gdx.app.log("Touch","lol");
                if(btnInicio.isTouched(x,y,camera)){
                    Gdx.app.log("Juego","botonIncio");
                    game.setScreen(new FlevorioSays(game));
                }
                //donChitoBtn.isTouched(x,y);
                return true; // return true to indicate the event was handled
            }

            @Override
            public boolean touchUp (int x, int y, int pointer, int button) {
                // your touch up code here
                return true; // return true to indicate the event was handled
            }
        });

    }

    private void cargarAudio() {
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("Musica/menuPrincipal.mp3"));
        musicaFondo.setLooping(true);
        musicaFondo.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        view.apply();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        fondo.render(batch);
        btnInicio.render(batch);
        donChitoBtn.render(batch);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        view.update(width, height);
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
        fondo.dispose();
        musicaFondo.dispose();
    }

}