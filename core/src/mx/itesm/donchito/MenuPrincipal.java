package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class MenuPrincipal implements Screen {
    private OrthographicCamera camera;
    private final DonChito game;
    private Viewport view;


    //TODO Refactor to interface
    private SpriteBatch batch;

    private Music musicaFondo;

    private SimpleAsset btnCargarPartida, donChitoBtn,fondo,btnNuevaPartida,btnNuevaPartidaFondo;


    //TODO better code pls
    boolean clickOnButton;
    float tiempoRecorrido;
    int direccion = 1;
    int numeroDeMovimiento = 0;
    char pantallaSiguiente;

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

        clickOnButton = false;
        tiempoRecorrido = 0;

        //TODO Refactor next code into an Asset Manager
        fondo = new SimpleAsset(Constants.MENUPRINCIPAL_FONDO_JPG,new Vector2(0,0));
        btnCargarPartida = new SimpleAsset(Constants.MENUPRINCIPAL_CARGARPARTIDA_PNG,new Vector2(870,290));
        btnNuevaPartida = new SimpleAsset(Constants.MENUPRINCIPAL_NUEVAPARTIDA_PNG,new Vector2(480,600));
        btnNuevaPartidaFondo = new SimpleAsset(Constants.MENUPRINCIPAL_NUEVAPARTIDAFONDO_PNG,new Vector2(420,550));
        donChitoBtn = new SimpleAsset(Constants.MENUPRINCIPAL_CARTELDONCHITO_PNG,new Vector2(420,230));

        leerEntrada();
        cargarAudio();
    }

    private void leerEntrada() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int x, int y, int pointexr, int button) {
                if(btnNuevaPartida.isTouched(x,y,camera)){
                    clickOnButton = true;
                    pantallaSiguiente = 'G';
                    return true;
                }
                if(btnCargarPartida.isTouched(x,y,camera)){
                    //TODO Load screen
                    clickOnButton = true;
                    pantallaSiguiente = 'A';
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchUp (int x, int y, int pointer, int button) {
                // your touch up code here
                return true;
            }
        });

    }

    private void cargarAudio() {
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal(Constants.MENU_PRINCIPAL_MP3));
        musicaFondo.setLooping(true);
        musicaFondo.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        view.apply();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if(clickOnButton){
            if(tiempoRecorrido >=10 || tiempoRecorrido <=-10){
                //tiempoRecorrido = 0;
                direccion *=-1;
                numeroDeMovimiento++;
            }
            if(direccion <0){
                tiempoRecorrido -= delta*100;
            }else{
                tiempoRecorrido += delta*100;
            }
            switch (pantallaSiguiente){
                case 'G': btnNuevaPartida.setRotation(tiempoRecorrido);break;
                case 'A':  btnCargarPartida.setRotation(tiempoRecorrido);break;
                default:break;
            }

            if(numeroDeMovimiento == 5){
                direccion = 1;
                switch (pantallaSiguiente){
                    case 'G': game.setScreen(new FlevorioSays(game));break;
                    case 'A':  game.setScreen(new AcercaDe(game));break;
                    default:break;
                }

            }
        }
        fondo.render(batch);
        btnCargarPartida.render(batch);
        donChitoBtn.render(batch);
        btnNuevaPartidaFondo.render(batch);
        btnNuevaPartida.render(batch);
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
        btnCargarPartida.dispose();
        donChitoBtn.dispose();
        btnNuevaPartidaFondo.dispose();
        btnNuevaPartida.dispose();
    }
}