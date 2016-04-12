package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class MenuPrincipal implements Screen {
    public static final int MULTIPLICADOR_TIEMPO = 100;
    public static final int MAX_MOVIMIENTO_ANIMACION = 3;
    public static final int TIEMPO_ROTACION = 7;
    private OrthographicCamera camera;
    private final DonChito game;
    private Viewport view;


    private SpriteBatch batch;

    private State estado = State.MENU;

    private Music musicaFondo;

    private SimpleAsset btnCargarPartida,
                        btnDonChito,
                        fondo,
                        btnNuevaPartida,
                        btnExtra,
                        btnPala,
                        btnAjustes,
                        botonSalirMenu,
                        fondoPausa;


    //TODO better code pls
    boolean clickOnButton;
    float tiempoRecorrido;
    int direccion;
    int movimientosActual;
    Screen pantallaSiguiente;
    boolean isClick;

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

        fondo = new SimpleAsset(Constants.MENUPRINCIPAL_FONDO_JPG,0,0);
        btnCargarPartida = new SimpleAsset(Constants.MENUPRINCIPAL_CARGARPARTIDA_PNG,870,290);
        btnNuevaPartida = new SimpleAsset(Constants.MENUPRINCIPAL_NUEVAPARTIDA_PNG,480,575);
        btnDonChito = new SimpleAsset(Constants.MENUPRINCIPAL_CARTELDONCHITO_PNG,420,230);
        btnExtra = new SimpleAsset(Constants.MENUPRINCIPAL_EXTRA_PNG,800,100);
        btnPala = new SimpleAsset(Constants.MENUPRINCIPAL_PALA_PNG,820,125);
        btnAjustes = new SimpleAsset(Constants.AJUSTES_BOTON_PNG,50,475);
        fondoPausa = new SimpleAsset(Constants.GLOBAL_MENU_PAUSA_PNG,0,0);
        botonSalirMenu = new SimpleAsset(Constants.GLOBAL_BOTON_SALIRMENU_PNG,405,425);
        estado = State.MENU;
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
                if(!isClick && estado == State.MENU){
                    if(btnNuevaPartida.isTouched(x,y,camera,view)){
                        clickOnButton = true;
                        isClick = true;
                        pantallaSiguiente = Screen.CUEVA;
                        return true;
                    }
                    if(btnExtra.isTouched(x,y,camera,view)){
                        clickOnButton = true;
                        isClick = true;
                        pantallaSiguiente = Screen.ACERCA;
                        return true;
                    }
                    if(btnDonChito.isTouched(x,y,camera,view)){
                        clickOnButton = true;
                        isClick = true;
                        pantallaSiguiente = Screen.DONCHITO;
                        return true;
                    }
                    if(btnAjustes.isTouched(x,y,camera,view)){
                        clickOnButton = true;
                        isClick = true;
                        pantallaSiguiente = Screen.SETTINGS;
                        return true;
                    }
                }
                if(!isClick && estado == State.PAUSA) {
                    if(botonSalirMenu.isTouched(x,y,camera,view)) {
                        estado = State.MENU;
                        return true;
                    }
                }
                return false;
            }
        });

    }

    private void cargarAudio() {
        musicaFondo = DonChito.assetManager.get(Constants.MENU_PRINCIPAL_MP3);
        musicaFondo.setLooping(true);
        musicaFondo.play();
    }


    public void init(){
        clickOnButton = false;
        tiempoRecorrido = 0;
        direccion = 1;
        movimientosActual =  0;
        pantallaSiguiente = Screen.NONE;
        isClick = false;
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        view.apply();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        fondo.render(batch);
        if(clickOnButton && estado == State.MENU){
            if(movimientosActual == MAX_MOVIMIENTO_ANIMACION){
                if(pantallaSiguiente == Screen.DONCHITO){
                    init();
                }else if(pantallaSiguiente == Screen.SETTINGS){
                    init();
                    estado = State.PAUSA;
                }
                else{
                    if(musicaFondo.isPlaying()){
                        musicaFondo.stop();
                    }
                    switch (pantallaSiguiente){
                        case CUEVA:
                            dispose();
                            game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.CUEVA,game));
                            break;
                        case ACERCA:  dispose();
                            game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.ACERCA,game));
                            break;
                        default:
                            break;
                    }
                }
            }else{
                if(tiempoRecorrido >= TIEMPO_ROTACION || tiempoRecorrido <=-TIEMPO_ROTACION){
                    direccion *=-1;
                    movimientosActual++;
                }
                switch (pantallaSiguiente){
                    case CUEVA:
                        btnNuevaPartida.setRotation(tiempoRecorrido);
                        break;
                    case ACERCA:
                        btnPala.setRotation(tiempoRecorrido);
                        break;
                    case SETTINGS:
                        btnAjustes.setRotation(tiempoRecorrido);
                        break;
                    case DONCHITO:
                        btnDonChito.setRotation(tiempoRecorrido);
                        break;
                    default:break;
                }
                if(direccion <0){
                    tiempoRecorrido -= delta* MULTIPLICADOR_TIEMPO;
                }else{
                    tiempoRecorrido += delta* MULTIPLICADOR_TIEMPO;
                }
            }
        }
        btnCargarPartida.render(batch);
        btnDonChito.render(batch);
        btnNuevaPartida.render(batch);
        btnExtra.render(batch);
        btnPala.render(batch);
        btnAjustes.render(batch);
        if(estado == State.PAUSA){
            fondoPausa.render(batch);
            botonSalirMenu.render(batch);
        }
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
        DonChito.assetManager.clear();
    }

    enum State
    {
        PAUSA,
        MENU,
    }
    enum Screen{
        DONCHITO,
        NONE,
        FLEVORIO,
        LIVERMORIO,
        ROMANSTRUGGLE,
        RADCLIFF,
        CUEVA,
        ACERCA,
        SETTINGS
    }
}