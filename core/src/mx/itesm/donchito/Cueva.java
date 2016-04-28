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
    private OrthographicCamera cameraHUD;
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

    private boolean positivoX=true;
    private boolean positivoY=true;

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

    private static final float velocidad = 6.20f;


    private State estado = State.PLAY;
    public enum State
    {
        PAUSA,
        PLAY
    }

    private float CamX = 1100;
    private float CamY = 700;
    private float CamSpeed = 2.4f;

    public Cueva(DonChito game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.input.setCatchBackKey(true);

        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        camera.position.set(0, 0, 0);
        camera.zoom = 1.2f;
        camera.update();

        cameraHUD = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        cameraHUD.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        cameraHUD.update();

        view = new FitViewport(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO,cameraHUD);
        cargarElementos();
        cargarAudio();
        batch = new SpriteBatch();

        fondoPausa = new SimpleAsset(Constants.GLOBAL_MENU_PAUSA_PNG,0,0);
        botonPlay = new SimpleAsset(Constants.GLOBAL_BOTON_PLAY_PNG,1050,500);
        botonConfiguracion = new SimpleAsset(Constants.GLOBAL_BOTON_CONFIGURACION_PNG,405,175);
        botonSalirMenu = new SimpleAsset(Constants.GLOBAL_BOTON_SALIRMENU_PNG,425,425);
        botonPlay.setAlpha(0.5f);

        botonPausa = new SimpleAsset(Constants.GLOBAL_BOTON_PAUSA_PNG,1050,500);
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
        donchito = new SimpleAsset(Constants.CUEVA_DON_CHITO_PNG,DonChito.ANCHO_MUNDO-270,DonChito.ALTO_MUNDO-150);
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            dispose();
            game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.MENU, game));
        }
        if(Gdx.input.justTouched()){
            if(botonPausa.isTouched(Gdx.input.getX(),Gdx.input.getY(),cameraHUD)||botonPlay.isTouched(Gdx.input.getX(),Gdx.input.getY(),cameraHUD)){
                if(estado == State.PAUSA){
                    estado = State.PLAY;
                }
                else if(estado == State.PLAY){
                    estado = State.PAUSA;
                }
            }
            if(estado == State.PAUSA && botonSalirMenu.isTouched(Gdx.input.getX(),Gdx.input.getY(),cameraHUD)){
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
        camera.position.set(CamX, CamY, 0);
        camera.update();
        batch.end();
        batch.setProjectionMatrix(cameraHUD.combined);
        batch.begin();

        if(estado == State.PLAY) {
            float x = donchito.getSprite().getX();
            float y = donchito.getSprite().getY();
            int CellX = xtoCell(x);
            int CellY = ytoCell(y);
            TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(1);
            TiledMapTileLayer.Cell curr = capa.getCell(CellX, CellY);

            if (curr == null) {
                donchito.getSprite().setX(donchito.getSprite().getX() + touchpad.getKnobPercentX() * velocidad);
                donchito.getSprite().setY(donchito.getSprite().getY() + touchpad.getKnobPercentY() * velocidad);
                float kPx = touchpad.getKnobPercentX();
                float kPy = touchpad.getKnobPercentY();
                if (kPx > 0) {
                    positivoX = true;
                } else {
                    positivoX = false;
                }

                if (kPy > 0) {
                    positivoY = true;
                } else {
                    positivoY = false;
                }
            } else {
                if (positivoX)
                    donchito.getSprite().setX(donchito.getSprite().getX() + ((-1 * Math.abs(touchpad.getKnobPercentX())) * velocidad));
                else
                    donchito.getSprite().setX(donchito.getSprite().getX() + ((Math.abs(touchpad.getKnobPercentX())) * velocidad));

                if (positivoY)
                    donchito.getSprite().setY(donchito.getSprite().getY() + ((-1 * Math.abs(touchpad.getKnobPercentY())) * velocidad));
                else
                    donchito.getSprite().setY(donchito.getSprite().getY() + ((1 * Math.abs(touchpad.getKnobPercentY())) * velocidad));
            }

            if (donchito.getSprite().getY() > 1050) {
                dispose();
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.FLEVORIO, game));
            }
            if (donchito.getSprite().getX() < 50) {
                dispose();
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.LIVERMORIO, game));
            }
            if (donchito.getSprite().getX() > 1850) {
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