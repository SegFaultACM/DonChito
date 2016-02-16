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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Esteban on 2/16/2016.
 */
public class FlevorioSays implements Screen{
    private OrthographicCamera camera;
    private final DonChito game;
    private static final float INCREMENTO = 0.6f;
    private Viewport view;


    //TODO Refactor to interface
    private Texture texturaFondo;
    private Texture texturaRoca;
    private Sprite rocaP;
    private SpriteBatch batch;
    private Music musicaFondo;
    private Sprite fondo;
    private Texture texturaRocaLvl1;
    private Texture texturaRocaLvl2;
    private Texture texturaRocaLvl3;
    //private Array<Rocas> rocas;

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
        texturaFondo = new Texture(Gdx.files.internal("Imagenes/Simon/base simons.png"));
        fondo = new Sprite(texturaFondo);
        fondo.scale(1);
        fondo.setScale(0.1f);

        crearRoca();
        cargarAudio();
    }

    private void crearRoca() {
        texturaRoca = new Texture(Gdx.files.internal("Imagenes/Simon/Simonst.png"));
        rocaP = new Sprite(texturaRoca);
        rocaP.setPosition(DonChito.ALTO_MUNDO / 2, DonChito.ANCHO_MUNDO / 2);

        texturaRocaLvl1 = new Texture(Gdx.files.internal("Imagenes/Simon/Botongrande.jpg"));
        texturaRocaLvl2 = new Texture(Gdx.files.internal("Imagenes/Simon/Botonchico.jpg"));
        texturaRocaLvl3 = new Texture(Gdx.files.internal("Imagenes/Simon/diamante_nivel3.png"));

        //rocas = new Array<Rocas>(24);

        /*
        hoyos = new Array<Hoyo>(9);
        for(int i=0;i<9;i++){
            Hoyo nuevo = new Hoyo(texturaHoyo);
            nuevo.setPosicion(150+(i%3)*350,100+200*(i/3));
            hoyos.add(nuevo);
        }
        */
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

        fondo.setScale(MathUtils.clamp(delta*INCREMENTO+fondo.getScaleX(),.1f,1f));

        batch.begin();
        fondo.draw(batch);
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