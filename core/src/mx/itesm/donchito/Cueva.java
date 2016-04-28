package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Cueva implements Screen{
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer rendererMapa;
    private final DonChito game;
    private Viewport view;

    private TiledMap mapa;

    public static final float ANCHO_MAPA = 1280;   // Ancho del mapa en pixeles
    public static final int TAM_CELDA = 16;

    private Stage stage;
    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;

    private SpriteBatch batch;
    private Music musicaFondo;
    private Music efectoFondo;

    //TODO aplicar ANIMACIONES
    private SimpleAsset donchito;

    private SimpleAsset fondoPausa;
    private SimpleAsset botonPausa;
    private SimpleAsset botonPlay;
    private SimpleAsset botonSalirMenu;
    private SimpleAsset botonConfiguracion;

    private static final float velocidad = 3.20f;


    private State estado = State.PLAY;

    private Direccion positivoX = Direccion.NONE;
    private Direccion positivoY = Direccion.NONE;

    private Direccion cancelarX = Direccion.NONE;
    private Direccion cancelarY = Direccion.NONE;

    private float ultX;
    private float ultY;

    public enum State{
        PAUSA,
        PLAY
    }

    public enum Direccion{
        LEFT,
        RIGHT,
        UP,
        DOWN,
        NONE
    }

    public Cueva(DonChito game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.input.setCatchBackKey(true);

        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();

        view = new FitViewport(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO,camera);
        view.apply();
        cargarElementos();
        cargarAudio();
        batch = new SpriteBatch();

        fondoPausa = new SimpleAsset(Constants.GLOBAL_MENU_PAUSA_PNG,0,0);
        botonPlay = new SimpleAsset(Constants.GLOBAL_BOTON_PLAY_PNG,1110,530);
        botonConfiguracion = new SimpleAsset(Constants.GLOBAL_BOTON_CONFIGURACION_PNG,405,175);
        botonConfiguracion.getSprite().setColor(.1f,.1f,.1f,.5f);
        botonSalirMenu = new SimpleAsset(Constants.GLOBAL_BOTON_SALIRMENU_PNG,405,425);
        botonPlay.setAlpha(0.5f);

        botonPausa = new SimpleAsset(Constants.GLOBAL_BOTON_PAUSA_PNG,1110,530);
        botonPausa.setAlpha(0.5f);

        rendererMapa = new OrthogonalTiledMapRenderer(mapa,batch);
        rendererMapa.setView(camera);

        touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("Imagenes/Cueva/touchBackground.png"));
        touchpadSkin.add("touchKnob", new Texture("Imagenes/Cueva/touchKnob.png"));
        touchpadStyle = new Touchpad.TouchpadStyle();
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        touchpad = new Touchpad(10, touchpadStyle);
        touchpad.setBounds(15, 15, 200, 200);
        stage = new Stage(view);
        stage.addActor(touchpad);
        Gdx.input.setInputProcessor(stage);
    }

    private void cargarAudio() {
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal(Constants.CUEVA_MUSICA_FONDO));
        musicaFondo.setLooping(true);
        reproducirMusica(musicaFondo);
        efectoFondo = Gdx.audio.newMusic(Gdx.files.internal(Constants.CUEVA_EFECTOS_FONDO));
    }
    private void reproducirMusica(Music musica){
        musica.setVolume(1F);
        if(!DonChito.preferences.getBoolean(Constants.MENUPRINCIPAL_SOUND_PREF,true)){
            musica.setVolume(0f);
        }
        musica.play();
    }


    private void cargarElementos(){
        AssetManager assetManager = DonChito.assetManager;
        mapa = assetManager.get(Constants.CUEVA_TILES);
        donchito = new SimpleAsset(Constants.CUEVA_DON_CHITO_PNG,DonChito.ANCHO_MUNDO/2,DonChito.ALTO_MUNDO/2);
    }

    @Override
    public void render(float delta) {
        view.apply();
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            dispose();
            game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.MENU, game));
        }
        if(Gdx.input.justTouched()){
            if(botonPausa.isTouched(Gdx.input.getX(),Gdx.input.getY(),camera,view)||botonPlay.isTouched(Gdx.input.getX(),Gdx.input.getY(),camera,view)){
                if(estado == State.PAUSA){
                    estado = State.PLAY;
                }
                else if(estado == State.PLAY){
                    estado = State.PAUSA;
                }
            }
            if(estado == State.PAUSA && botonSalirMenu.isTouched(Gdx.input.getX(),Gdx.input.getY(),camera,view)){
                dispose();
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.MENU, game));
            }
        }
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        rendererMapa.setView(camera);
        rendererMapa.render();
        batch.begin();
        donchito.render(batch);
        camera.update();

        if(estado == State.PLAY) {
            float x = donchito.getSprite().getX();
            float y = donchito.getSprite().getY();
            int CellX = xtoCell(x);
            int CellY = ytoCell(y);
            TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(1);
            TiledMapTileLayer.Cell curr = capa.getCell(CellX, CellY);

            if (curr == null) {
                ultX = donchito.getSprite().getX();
                ultY = donchito.getSprite().getY();

                if(cancelarX == Direccion.RIGHT){
                    if(touchpad.getKnobPercentX()<0){
                        donchito.getSprite().setX(donchito.getSprite().getX() + touchpad.getKnobPercentX() * velocidad);
                        cancelarX = Direccion.NONE;
                        cancelarY = Direccion.NONE;
                    }
                }
                else if(cancelarX == Direccion.LEFT){
                    if(touchpad.getKnobPercentX()>0){
                        donchito.getSprite().setX(donchito.getSprite().getX() + touchpad.getKnobPercentX() * velocidad);
                        cancelarX = Direccion.NONE;
                        cancelarY = Direccion.NONE;
                    }
                }
                else {
                    donchito.getSprite().setX(donchito.getSprite().getX() + touchpad.getKnobPercentX() * velocidad);
                }

                if(cancelarY == Direccion.UP){
                    if(touchpad.getKnobPercentY()<0){
                        donchito.getSprite().setY(donchito.getSprite().getY() + touchpad.getKnobPercentY() * velocidad);
                        cancelarX = Direccion.NONE;
                        cancelarY = Direccion.NONE;
                    }
                }
                else if(cancelarY == Direccion.DOWN){
                    if(touchpad.getKnobPercentY()>0){
                        donchito.getSprite().setY(donchito.getSprite().getY() + touchpad.getKnobPercentY() * velocidad);
                        cancelarX = Direccion.NONE;
                        cancelarY = Direccion.NONE;
                    }
                }
                else {
                    donchito.getSprite().setY(donchito.getSprite().getY() + touchpad.getKnobPercentY() * velocidad);
                }
                float kPx = touchpad.getKnobPercentX();
                float kPy = touchpad.getKnobPercentY();
                if (kPx > 0) {
                    positivoX = Direccion.RIGHT;
                } else if(kPx<0){
                    positivoX = Direccion.LEFT;
                }
                else {
                    positivoY  = Direccion.NONE;
                }
                if (kPy > 0) {
                    positivoY = Direccion.UP;
                } else if(kPy<0){
                    positivoY = Direccion.DOWN;
                }
                else {
                    positivoY  =Direccion.NONE;
                }
            } else {

                cancelarX = positivoX;
                cancelarY = positivoY;

                donchito.getSprite().setPosition(ultX,ultY);
            }
            if (donchito.getSprite().getY() > 700 ) {
                dispose();
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.FLEVORIO, game));
            }
            if (donchito.getSprite().getX() < 20) {
                dispose();
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.LIVERMORIO, game));
            }
            if (donchito.getSprite().getX() > 1240) {
                dispose();
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.ROMANSTRUGGLE, game));
            }
            botonPausa.render(batch);
        }
        if(estado == State.PAUSA){
            fondoPausa.render(batch);
            botonConfiguracion.render(batch);
            botonSalirMenu.render(batch);
            botonPlay.render(batch);
        }

        batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        view.update(width,height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
    private void stopMusic(){
        if(musicaFondo.isPlaying()){
            musicaFondo.stop();
        }
        if(efectoFondo.isPlaying()){
            efectoFondo.stop();
        }
    }

    @Override
    public void dispose() {
        stopMusic();
        DonChito.assetManager.clear();
    }
    private int xtoCell(float x){
        return Math.round(x/16);
    }
    private int ytoCell(float y){
        return Math.round(y/16);
    }

    private int Celltox(int x){
        return x*16;
    }
    private int Celltoy(int y){
        return y*16;
    }
}