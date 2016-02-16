package mx.itesm.donchito;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import sun.java2d.pipe.SpanShapeRenderer;

/**
 * Created by Esteban on 2/16/2016.
 */
public class FlevorioSays implements Screen{
    private OrthographicCamera camera;
    private final DonChito game;
    private static final float INCREMENTO = 0.6f;
    private Viewport view;

    //TODO Refactor to interface
    private Texture texturaRoca;

    private SpriteBatch batch;
    private Music musicaFondo;

    private SimpleAsset rocaP;
    private SimpleAsset fondo;

    private Array<SimpleAsset> rocas;

    public FlevorioSays(DonChito game) {
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
        fondo = new SimpleAsset("Imagenes/Simon/base simons.png",new Vector2(0,0));
        fondo.getSprite().scale(1);
        fondo.getSprite().setScale(0.1f);

        crearRoca();
        cargarAudio();
    }

    private void crearRoca() {
        rocaP = new SimpleAsset("Imagenes/Simon/Simonst.png",new Vector2(0,0));
        rocaP.setPosition(new Vector2(DonChito.ALTO_MUNDO / 2, DonChito.ANCHO_MUNDO / 2));
        SimpleAsset nuevo;

        rocas = new Array<SimpleAsset>(24);
        for(int i=0;i<8;i++){
            nuevo = new SimpleAsset("Imagenes/Simon/Botongrande.jpg",new Vector2(0,0));
            rocas.add(nuevo);
        }
        for(int i=0;i<8;i++){
            nuevo = new SimpleAsset("Imagenes/Simon/Botonchico.jpg",new Vector2(0,0));
            rocas.add(nuevo);
        }
        for(int i=0;i<8;i++){
            nuevo = new SimpleAsset("Imagenes/Simon/diamante_nivel3.png",new Vector2(0,0));
            rocas.add(nuevo);
        }
    }

    private void cargarAudio() {
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("Musica/FlavioSays.mp3"));
        musicaFondo.setLooping(true);
        musicaFondo.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //quiere decir que va a borrar con las caracteristicas de arriba

        fondo.getSprite().setScale(MathUtils.clamp(delta*INCREMENTO+fondo.getSprite().getScaleX(),.1f,1f));

        batch.begin();
        fondo.render(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

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