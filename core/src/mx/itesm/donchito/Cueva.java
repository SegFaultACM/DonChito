package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class Cueva implements Screen{
    private OrthographicCamera camera;
    private OrthographicCamera cameraHUD;
    private final DonChito game;
    private Viewport view;

    private SpriteBatch batch;
    private Music musicaFondo;

    private SimpleAsset fondoPantalla;
    private SimpleAsset donchito;


    private SimpleAsset flechaArriba;
    private SimpleAsset flechaAbajo;
    private SimpleAsset flechaDer;
    private SimpleAsset flechaIzq;

    private static final float velocidad = 4.20f; // Blaze it


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

        // Camara y viewport
        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        camera.position.set(0, 0, 0);
        camera.update();

        cameraHUD = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        cameraHUD.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        cameraHUD.update();

        view = new FitViewport(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO,camera);
        cargarElementos();
        leerEntrada();
        cargarAudio();
        batch = new SpriteBatch();


    }

    private void cargarAudio() {
        //TODO: Add correct music for this screen
        //musicaFondo = Gdx.audio.newMusic(Gdx.files.internal(Constants.MUSICA_FLAVIO_SAYS_MP3));
        //musicaFondo.setLooping(true);
        //musicaFondo.play();
    }


    private void cargarElementos(){
        fondoPantalla = new SimpleAsset(Constants.CUEVA_FONDO_JPG,0,0);
        donchito = new SimpleAsset(Constants.CUEVA_DON_CHITO_PNG,DonChito.ANCHO_MUNDO-270,DonChito.ALTO_MUNDO-150);
        flechaArriba = new SimpleAsset(Constants.CUEVA_ARROW_UP, 100,300);
        flechaAbajo = new SimpleAsset(Constants.CUEVA_ARROW_DOWN, 100,100);
        flechaDer = new SimpleAsset(Constants.CUEVA_ARROW_RIGHT, 200,200);
        flechaIzq = new SimpleAsset(Constants.CUEVA_ARROW_LEFT, 0,200);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();


        fondoPantalla.render(batch);
        donchito.render(batch);
        camera.position.set(CamX, CamY, 0);
        camera.update();
        ejecutarInputs();
        batch.end();
        renderHUD();
    }
    public void renderHUD(){
        batch.setProjectionMatrix(cameraHUD.combined);
        batch.begin();
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
        DonChito.getAssetManager().clear();
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
                        CamX -= CamSpeed;
                    /*camera.position.set(new Vector2(camera.position.x-100,camera.position.y),0);
                    camera.update();
                    batch.setProjectionMatrix(camera.combined);*/
                    break;
                case 2:
                    donchito.setPosition(donchito.getSprite().getX() + velocidad, donchito.getSprite().getY());
                    if(CamX < 1361)
                        CamX += CamSpeed;
                    break;
                case 3:
                    donchito.setPosition(donchito.getSprite().getX(), donchito.getSprite().getY() - velocidad);
                    if(CamY > 376)
                        CamY -= CamSpeed;
                    break;
                case 4:
                    donchito.setPosition(donchito.getSprite().getX(), donchito.getSprite().getY() + velocidad);
                    if(CamY < 845.0)
                        CamY += CamSpeed;
                    break;
                default:
                    break;
            }
        }
    }
}