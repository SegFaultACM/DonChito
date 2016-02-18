package mx.itesm.donchito;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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

    private boolean rocasCreadas = false;
    private boolean nivelCompleto = true;
    private boolean brillando = false;

    private int nivel = 1;
    //TODO Refactor to interface
    private Texture texturaRoca;

    private SpriteBatch batch;
    private Music musicaFondo;

    private SimpleAsset rocaP;
    private SimpleAsset fondo;

    private Array<SimpleAsset> rocas;
    private SimpleAsset fondoPantalla;

    private int[] combinaciones = new int[]{0,0,0,0,0,0,0,0,0,0};

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
        fondoPantalla = new SimpleAsset(Constants.FLEVORIO_FONDOPANTALLA,new Vector2(0,0));
        fondo = new SimpleAsset(Constants.FLEVORIO_FONDO,new Vector2(0,0));
        fondo.getSprite().scale(1);
        fondo.getSprite().setScale(0.1f);
        cargarAudio();
    }

    private void crearRoca() {
        rocasCreadas = true;
        SimpleAsset nuevo;

        rocas = new Array<SimpleAsset>(3);

        nuevo = new SimpleAsset(Constants.FLEVORIO_BOTONCENTRAL,new Vector2(555,250));
        nuevo.getSprite().setScale(1.1f);
        rocas.add(nuevo);

        Gdx.app.log("Creando rocas", "Se crean rocas");
    }

    private void crearCoordenadas() {
        /*
        rocas.get(15).setPosition(new Vector2(572, 340));
        rocas.get(15).setRotation(225f);
        */

    }

    private void cargarAudio() {
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal(Constants.MUSICA_FLAVIO_SAYS_MP3));
        musicaFondo.setLooping(true);
        //musicaFondo.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(nivelCompleto){
            crearCombinacion(nivel);
            nivelCompleto = false;
        }
        batch.begin();

        fondoPantalla.render(batch);
        fondo.render(batch);

        if(fondo.getSprite().getScaleX()>=1f){
            if(!rocasCreadas){
                crearRoca();
            }
            else{
                for(SimpleAsset roca: rocas){
                    roca.render(batch);
                }
                rocas.get(0).getSprite().setColor(103,128,150,1);
            }
        }
        else{
            fondo.getSprite().setScale(MathUtils.clamp(delta*INCREMENTO+fondo.getSprite().getScaleX(),.1f,1f));
        }
        batch.end();
    }

    private void crearCombinacion(int nivel) {
        for(int i=0;i<combinaciones.length;i++){
            combinaciones[i] = 0;
        }
        for(int i=0;i<nivel*2;i++){
            combinaciones[i] = MathUtils.random(1,4);
        }
        for(int i=0;i<combinaciones.length;i++){
            Gdx.app.log(i+"",combinaciones[i]+"");
        }

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