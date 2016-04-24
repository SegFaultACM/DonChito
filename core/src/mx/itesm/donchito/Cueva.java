package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;

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


    // Touchpad
    private Stage stage;
    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private Texture blockTexture;
    private Sprite blockSprite;
    private float blockSpeed;

    private boolean positivoX=true;
    private boolean positivoY=true;

    private SpriteBatch batch;
    private Music musicaFondo;

    private SimpleAsset fondoPantalla;
    private SimpleAsset donchito;


    private SimpleAsset flechaArriba;
    private SimpleAsset flechaAbajo;
    private SimpleAsset flechaDer;
    private SimpleAsset flechaIzq;

    private static final float velocidad = 6.20f; // Blaze it


    private State estado = State.PLAY;
    private State estadoBoton = State.NOPRESIONADO;
    private int botonPresionado = 0;
    public enum State
    {
        PAUSA,
        PLAY,
        PRESIONADO,
        NOPRESIONADO
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

        // Camara y viewport
        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        camera.position.set(0, 0, 0);
        camera.zoom = 1f;
        camera.update();

        // Cargar frames

        cameraHUD = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        cameraHUD.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        cameraHUD.update();

        view = new FitViewport(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO,cameraHUD);
        cargarElementos();
        leerEntrada();
        cargarAudio();
        batch = new SpriteBatch();

        rendererMapa = new OrthogonalTiledMapRenderer(mapa,batch);
        rendererMapa.setView(camera);


        //Create a touchpad skin
        touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("Imagenes/Cueva/touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("Imagenes/Cueva/touchKnob.png"));
        //Create TouchPad Style
        touchpadStyle = new Touchpad.TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        //Create new TouchPad with the created style
        touchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds(15, 15, 200, 200);
        //Create a Stage and add TouchPad
        stage = new Stage(view);
        stage.addActor(touchpad);
        Gdx.input.setInputProcessor(stage);



    }

    private void cargarAudio() {
        //TODO: Add correct music for this screen
        //musicaFondo = Gdx.audio.newMusic(Gdx.files.internal(Constants.MUSICA_FLAVIO_SAYS_MP3));
        //musicaFondo.setLooping(true);
        //musicaFondo.play();
    }


    private void cargarElementos(){


        AssetManager assetManager = DonChito.assetManager;   // Referencia al assetManager
        // Carga el mapa en memoria
        mapa = assetManager.get(Constants.CUEVA_TILES);
        fondoPantalla = new SimpleAsset(Constants.CUEVA_FONDO_JPG,0,0);
        donchito = new SimpleAsset(Constants.CUEVA_DON_CHITO_PNG,DonChito.ANCHO_MUNDO-270,DonChito.ALTO_MUNDO-150);
        flechaArriba = new SimpleAsset(Constants.CUEVA_ARROW_UP, 100,300);
        flechaAbajo = new SimpleAsset(Constants.CUEVA_ARROW_DOWN, 100,100);
        flechaDer = new SimpleAsset(Constants.CUEVA_ARROW_RIGHT, 200,200);
        flechaIzq = new SimpleAsset(Constants.CUEVA_ARROW_LEFT, 0,200);
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            dispose();
            game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.MENU, game));
        }
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        rendererMapa.setView(camera);
        rendererMapa.render();
        batch.begin();
        //fondoPantalla.render(batch);
        donchito.render(batch);
        camera.position.set(CamX, CamY, 0);
        camera.update();


        ejecutarInputs();
        batch.end();
        batch.setProjectionMatrix(cameraHUD.combined);
        batch.begin();
        /*flechaAbajo.render(batch);
        flechaArriba.render(batch);
        flechaDer.render(batch);
        flechaIzq.render(batch);*/

        float x = donchito.getSprite().getX();
        float y = donchito.getSprite().getY();
        int CellX = xtoCell(x);
        int CellY = ytoCell(y);
        TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(1);
        TiledMapTileLayer.Cell curr = capa.getCell(CellX, CellY);
        //Gdx.app.log("CellX,Y", CellX + " " + CellY);
        if (curr != null) {
            //Gdx.app.log("Tile: ", curr.getTile().toString());


        }

        if (curr == null) {
            //Gdx.app.log("pos ", positivoX + " " + positivoY);
            //Gdx.app.log("knob ", touchpad.getKnobPercentX() + " " + touchpad.getKnobPercentY());
                     //Gdx.app.log("KNOB ", touchpad.getKnobPercentX() + " " + touchpad.getKnobPercentY());
            donchito.getSprite().setX(donchito.getSprite().getX() + touchpad.getKnobPercentX() * velocidad);
            donchito.getSprite().setY(donchito.getSprite().getY() + touchpad.getKnobPercentY() * velocidad);
            float kPx = touchpad.getKnobPercentX();
            float kPy = touchpad.getKnobPercentY();
            if (kPx > 0) {
                positivoX = true;
            }else {
                positivoX = false;
            }

            if(kPy > 0) {
                positivoY = true;
            }else {
                positivoY = false;
            }
        } else {
            // dentro
            if(positivoX)
                donchito.getSprite().setX(donchito.getSprite().getX() + ((-1*Math.abs(touchpad.getKnobPercentX())) * velocidad));
            else
                donchito.getSprite().setX(donchito.getSprite().getX() + ((Math.abs(touchpad.getKnobPercentX())) * velocidad));

            if(positivoY)
                donchito.getSprite().setY(donchito.getSprite().getY() + ((-1*Math.abs(touchpad.getKnobPercentY())) * velocidad));
            else
                donchito.getSprite().setY(donchito.getSprite().getY() + ((1*Math.abs(touchpad.getKnobPercentY())) * velocidad));

           /* if(touchpad.getKnobX() > 0){
                 else {
                donchito.getSprite().setX(donchito.getSprite().getX() + (touchpad.getKnobPercentX() * velocidad));
            }
            if(touchpad.getKnobY() > 0){
                donchito.getSprite().setY(donchito.getSprite().getY() - (touchpad.getKnobPercentY() * velocidad));

            } else {
                donchito.getSprite().setY(donchito.getSprite().getY() + (touchpad.getKnobPercentY() * velocidad));
            }*/
            //donchito.getSprite().setX(donchito.getSprite().getX() - (touchpad.getKnobPercentX() * velocidad));
            //donchito.getSprite().setY(donchito.getSprite().getY() - (touchpad.getKnobPercentY() * velocidad));
        }
        if(donchito.getSprite().getY() > 1050)
            game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.FLEVORIO,game));
        if(donchito.getSprite().getX() < 300)
            game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.LIVERMORIO,game));
        if(donchito.getSprite().getX() > 1850)
            game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.ROMANSTRUGGLE,game));

        batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        view.update(width,height);
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
    private void leerEntrada() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchUp (int x, int y, int pointer, int button) {
                estadoBoton = State.NOPRESIONADO;
                return true; // return true to indicate the event was handled
            }
            public boolean touchDown (int x, int y, int pointexr, int button) {
                estadoBoton = State.PRESIONADO;

                return true; // return true to indicate the event was handled
            }
        });

    }
    private void ejecutarInputs(){
        if(estadoBoton == State.PRESIONADO){
            int x = Gdx.app.getInput().getX();
            int y = Gdx.app.getInput().getY();
            int CellX = xtoCell(x);
            int CellY = ytoCell(74-y);
            TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(1);
            TiledMapTileLayer.Cell curr = capa.getCell(CellX, CellY);
            Gdx.app.log("Tile: ", curr.getTile().toString());
            //Gdx.app.log(""+x,""+y);
            //Gdx.app.log("CamX ",Float.toString(CamX));
            //Gdx.app.log("CamY ",Float.toString(CamY));
            if(x>0 && x<100 && y>427 && y<525) {
                botonPresionado = 1;
            }
            else if(x>200 && x<300 && y>427 && y<525){
                botonPresionado = 2;
            }
            else if(x>100 && x<200 && y>520 && y<620){
                botonPresionado = 3;
            }
            else if(x>100 && x<200 && y>325 && y<425){
                botonPresionado = 4;

            }
            else {
                botonPresionado = 0;
            }
            //Gdx.app.log(Float.toString(donchito.getSprite().getX()),Float.toString(donchito.getSprite().getY()));
            if(donchito.getSprite().getY() > 1050)
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.FLEVORIO,game));
            if(donchito.getSprite().getX() < 50)
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.LIVERMORIO,game));
            if(donchito.getSprite().getX() > 1850)
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.ROMANSTRUGGLE,game));
            switch(botonPresionado){
                case 1:
                    donchito.setPosition(donchito.getSprite().getX() - velocidad,donchito.getSprite().getY());
                    if(CamX > 653)
                        //CamX -= CamSpeed;
                        CamX = donchito.getSprite().getX();
                    /*camera.position.set(new Vector2(camera.position.x-100,camera.position.y),0);
                    camera.update();
                    batch.setProjectionMatrix(camera.combined);*/
                    break;
                case 2:
                    donchito.setPosition(donchito.getSprite().getX() + velocidad, donchito.getSprite().getY());
                    if(CamX < 1361)
                        //CamX += CamSpeed;
                        CamX = donchito.getSprite().getX();
                    break;
                case 3:
                    donchito.setPosition(donchito.getSprite().getX(), donchito.getSprite().getY() - velocidad);
                    if(CamY > 376)
                        //CamY -= CamSpeed;s
                        CamY = donchito.getSprite().getY();
                    break;
                case 4:
                    donchito.setPosition(donchito.getSprite().getX(), donchito.getSprite().getY() + velocidad);
                    if(CamY < 845.0)
                        //CamY += CamSpeed;
                        CamY = donchito.getSprite().getY();
                    break;
                default:
                    break;
            }
        }
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