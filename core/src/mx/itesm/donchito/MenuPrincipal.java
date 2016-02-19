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
    public static final int MULTIPLICADOR_TIEMPO = 100;
    public static final int MAX_MOVIMIENTO_ANIMACION = 3;
    public static final int TIEMPO_ROTACION = 7;
    private OrthographicCamera camera;
    private final DonChito game;
    private Viewport view;


    //TODO Refactor to interface
    private SpriteBatch batch;

    private Music musicaFondo;

    private SimpleAsset btnCargarPartida, btnDonChito,fondo,btnNuevaPartida,btnNuevaPartidaFondo,btnExtra,btnPala;


    //TODO better code pls
    boolean clickOnButton;
    float tiempoRecorrido;
    int direccion;
    int movimientosActual;
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

        //TODO Refactor next code into an Asset Manager
        fondo = new SimpleAsset(Constants.MENUPRINCIPAL_FONDO_JPG,new Vector2(0,0));
        btnCargarPartida = new SimpleAsset(Constants.MENUPRINCIPAL_CARGARPARTIDA_PNG,new Vector2(870,290));
        btnNuevaPartida = new SimpleAsset(Constants.MENUPRINCIPAL_NUEVAPARTIDA_PNG,new Vector2(480,600));
        btnDonChito = new SimpleAsset(Constants.MENUPRINCIPAL_CARTELDONCHITO_PNG,new Vector2(420,230));
        btnExtra = new SimpleAsset(Constants.MENUPRINCIPAL_EXTRA_PNG,new Vector2(800,100));
        btnPala = new SimpleAsset(Constants.MENUPRINCIPAL_PALA_PNG,new Vector2(830,100));
        btnPala.getSprite().setRotation(-10);

        //TODO Integrar a menu principal
        btnNuevaPartidaFondo = new SimpleAsset(Constants.MENUPRINCIPAL_NUEVAPARTIDAFONDO_PNG,new Vector2(420,550));
        leerEntrada();
        cargarAudio();
        init();
    }

    private void leerEntrada() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int x, int y, int pointexr, int button) {
                return false;
            }

            @Override
            public boolean touchUp (int x, int y, int pointer, int button) {
                // your touch up code here
                if(btnNuevaPartida.isTouched(x,y,camera)){
                    clickOnButton = true;
                    pantallaSiguiente = 'G';
                    return true;
                }
                if(btnExtra.isTouched(x,y,camera)){
                    //TODO Load screen
                    clickOnButton = true;
                    pantallaSiguiente = 'A';
                    return true;
                }
                if(btnDonChito.isTouched(x,y,camera)){
                    clickOnButton = true;
                    pantallaSiguiente = 'D';
                    return true;
                }
                return false;
            }
        });

    }

    private void cargarAudio() {
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal(Constants.MENU_PRINCIPAL_MP3));
        musicaFondo.setLooping(true);
        musicaFondo.play();
    }


    public void init(){
        clickOnButton = false;
        tiempoRecorrido = 0;
        direccion = 1;
        movimientosActual =  0;
        pantallaSiguiente = '\0';
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
            if(movimientosActual == MAX_MOVIMIENTO_ANIMACION){
                if(pantallaSiguiente == 'D'){
                    init();
                }else{
                    if(musicaFondo.isPlaying()){
                        musicaFondo.stop();
                    }
                    switch (pantallaSiguiente){
                        case 'G': game.setScreen(new FlevorioSays(game));break;
                        case 'A':  game.setScreen(new AcercaDe(game));break;
                        default:break;
                    }
                }
            }else{
                if(tiempoRecorrido >= TIEMPO_ROTACION || tiempoRecorrido <=-TIEMPO_ROTACION){
                    direccion *=-1;
                    movimientosActual++;
                }
                switch (pantallaSiguiente){
                    case 'G': btnNuevaPartida.setRotation(tiempoRecorrido);break;
                    case 'A':  btnPala.setRotation(tiempoRecorrido);break;
                    case 'D': btnDonChito.setRotation(tiempoRecorrido);break;
                    default:break;
                }
                if(direccion <0){
                    tiempoRecorrido -= delta* MULTIPLICADOR_TIEMPO;
                }else{
                    tiempoRecorrido += delta* MULTIPLICADOR_TIEMPO;
                }
            }
        }
        fondo.render(batch);
        btnCargarPartida.render(batch);
        btnDonChito.render(batch);
        btnNuevaPartidaFondo.render(batch);
        btnNuevaPartida.render(batch);
        btnExtra.render(batch);
        btnPala.render(batch);
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
        btnDonChito.dispose();
        btnNuevaPartidaFondo.dispose();
        btnNuevaPartida.dispose();
    }
}