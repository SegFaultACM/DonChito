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


    private SpriteBatch batch;

    private Music musicaFondo;

    private SimpleAsset btnCargarPartida,
                        btnDonChito,
                        fondo,
                        btnNuevaPartida,
                        btnExtra,
                        btnPala,
                        btnAjustes;


    //TODO better code pls
    boolean clickOnButton;
    float tiempoRecorrido;
    int direccion;
    int movimientosActual;
    char pantallaSiguiente;
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

        cargarRecursos();

        fondo = new SimpleAsset(Constants.MENUPRINCIPAL_FONDO_JPG,new Vector2(0,0));
        btnCargarPartida = new SimpleAsset(Constants.MENUPRINCIPAL_CARGARPARTIDA_PNG,new Vector2(870,290));
        btnNuevaPartida = new SimpleAsset(Constants.MENUPRINCIPAL_NUEVAPARTIDA_PNG,new Vector2(480,575));
        btnDonChito = new SimpleAsset(Constants.MENUPRINCIPAL_CARTELDONCHITO_PNG,new Vector2(420,230));
        btnExtra = new SimpleAsset(Constants.MENUPRINCIPAL_EXTRA_PNG,new Vector2(800,100));
        btnPala = new SimpleAsset(Constants.MENUPRINCIPAL_PALA_PNG,new Vector2(820,125));
        btnAjustes = new SimpleAsset(Constants.AJUSTES_BOTON_PNG,new Vector2(50,475));

        leerEntrada();
        cargarAudio();
        init();
    }
    private void cargarRecursos() {
        AssetManager assetManager = DonChito.getAssetManager();
        assetManager.load(Constants.MENUPRINCIPAL_FONDO_JPG,Texture.class);
        assetManager.load(Constants.MENUPRINCIPAL_CARGARPARTIDA_PNG,Texture.class);
        assetManager.load(Constants.MENUPRINCIPAL_NUEVAPARTIDA_PNG,Texture.class);
        assetManager.load(Constants.MENUPRINCIPAL_CARTELDONCHITO_PNG,Texture.class);
        assetManager.load(Constants.MENUPRINCIPAL_EXTRA_PNG,Texture.class);
        assetManager.load(Constants.MENUPRINCIPAL_PALA_PNG,Texture.class);
        assetManager.load(Constants.AJUSTES_BOTON_PNG,Texture.class);
        assetManager.load(Constants.MENU_PRINCIPAL_MP3,Music.class);
        assetManager.finishLoading();
    }
    private void leerEntrada() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int x, int y, int pointexr, int button) {
                return false;
            }

            @Override
            public boolean touchUp (int x, int y, int pointer, int button) {
                if(!isClick){
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
            }
        });

    }

    private void cargarAudio() {
        musicaFondo = DonChito.getAssetManager().get(Constants.MENU_PRINCIPAL_MP3);
        musicaFondo.setLooping(true);
        musicaFondo.play();
    }


    public void init(){
        clickOnButton = false;
        tiempoRecorrido = 0;
        direccion = 1;
        movimientosActual =  0;
        pantallaSiguiente = '\0';
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
        if(clickOnButton){
            if(movimientosActual == MAX_MOVIMIENTO_ANIMACION){
                if(pantallaSiguiente == 'D'){
                    init();
                }else{
                    if(musicaFondo.isPlaying()){
                        musicaFondo.stop();
                    }
                    switch (pantallaSiguiente){
                        case 'G': dispose();game.setScreen(new RomanStruggle(game));break;
                        case 'A':  dispose();game.setScreen(new AcercaDe(game));break;
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
        btnNuevaPartida.render(batch);
        btnExtra.render(batch);
        btnPala.render(batch);
        btnAjustes.render(batch);
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
        DonChito.getAssetManager().clear();
    }
}