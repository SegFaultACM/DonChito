package mx.itesm.donchito;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.Rectangle;

public class Cueva implements Screen{
    private OrthographicCamera camera;
    private final DonChito game;
    private static final float INCREMENTO = 0.6f;
    private Viewport view;

    private boolean rocasCreadas = false;
    private SpriteBatch batch;
    private Music musicaFondo;

    private SimpleAsset fondoPantalla;
    //private SimpleAsset donchito;
    private Texture donchitoImage
    private SimpleAsset flechaArriba;
    private SimpleAsset flechaAbajo;
    private SimpleAsset flechaDer;
    private SimpleAsset flechaIzq;
    private Rectangle donchito;

    public Cueva(DonChito game) {
        this.game = game;
    }

    @Override
    public void show() {

        // Camara y viewport
                camera = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();
        view = new FitViewport(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO,camera);

        cargarRecursos();
        cargarElementos();
        leerEntrada();
        cargarAudio();
        donchitoImage = new Texture(Gdx.files.internal(Constants.CUEVA_DON_CHITO_PNG));
        donchito = new Rectangle();
        donchito.x = 800 / 2 - 64 / 2;
        donchito.y = 20;
        donchito.width = 64;
        donchito.height = 64;
        batch = new SpriteBatch();

    }

    private void cargarAudio() {
        //TODO: Add correct music for this screen
        //musicaFondo = Gdx.audio.newMusic(Gdx.files.internal(Constants.MUSICA_FLAVIO_SAYS_MP3));
        //musicaFondo.setLooping(true);
        //musicaFondo.play();
    }

    private void cargarRecursos(){
        AssetManager assetManager = DonChito.getAssetManager();
        assetManager.load(Constants.CUEVA_FONDO_JPG,Texture.class);
        assetManager.load(Constants.CUEVA_DON_CHITO_PNG,Texture.class);
        assetManager.load(Constants.CUEVA_ARROW_UP,Texture.class);
        assetManager.load(Constants.CUEVA_ARROW_DOWN,Texture.class);
        assetManager.load(Constants.CUEVA_ARROW_LEFT,Texture.class);
        assetManager.load(Constants.CUEVA_ARROW_RIGHT,Texture.class);
        assetManager.finishLoading();
    }

    private void cargarElementos(){
        fondoPantalla = new SimpleAsset(Constants.CUEVA_FONDO_JPG,new Vector2(0,0));
        // donchito = new SimpleAsset(Constants.CUEVA_DON_CHITO_PNG,new Vector2(DonChito.ANCHO_MUNDO/2-75,DonChito.ALTO_MUNDO/2-75));
        flechaArriba = new SimpleAsset(Constants.CUEVA_ARROW_UP, new Vector2(100,300));
        flechaAbajo = new SimpleAsset(Constants.CUEVA_ARROW_DOWN, new Vector2(100,100));
        flechaDer = new SimpleAsset(Constants.CUEVA_ARROW_RIGHT, new Vector2(200,200));
        flechaIzq = new SimpleAsset(Constants.CUEVA_ARROW_LEFT, new Vector2(0,200));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        fondoPantalla.render(batch);
        donchito.render(batch);
        flechaAbajo.render(batch);
        flechaArriba.render(batch);
        flechaDer.render(batch);
        flechaIzq.render(batch);

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
    private void leerEntrada() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int x, int y, int pointexr, int button) {
                if(flechaArriba.isTouched(x,y,camera)){
                    donchito.getSprite()
                }






                return false;
            }

            @Override
            public boolean touchUp (int x, int y, int pointer, int button) {


                /*if(!isClick){
                    if(btnNuevaPartida.isTouched(x,y,camera)){
                        clickOnButton = true;
                        isClick = true;
                        pantallaSiguiente = 'G';
                        return true;
                    }
                    if(btnExtra.isTouched(x,y,camera)){
                        //TODO Load screen for extras
                        clickOnButton = true;
                        isClick = true;
                        pantallaSiguiente = 'A';
                        return true;
                    }
                    if(btnDonChito.isTouched(x,y,camera)){
                        clickOnButton = true;
                        isClick = true;
                        pantallaSiguiente = 'D';
                        return true;
                    }
                }
                return false;
            }*/
        });


    }
}