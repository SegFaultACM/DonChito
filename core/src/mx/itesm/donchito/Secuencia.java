package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Secuencia implements Screen {

    private final DonChito game;
    private SimpleAsset botonPlay;
    private SimpleAsset historieta;
    private AssetManager assetManager;
    private Camera camera;
    private Camera cameraHUD;
    private Viewport view;

    private float tiempoEsperar = 5f;
    private int indicePosiciones = 0;
    private int maxPosiciones = 7;
    private boolean cueva;

    private float currx = 300;
    private float curry = 1270;

    private boolean acabar = false;

    private float[] posicionesX = new float[]{300,900,300,850,300,600,900};
    private float[] posicionesY = new float[]{1270,1270,800,800,300,300,300};

    private SpriteBatch batch;

    public Secuencia(DonChito game, boolean cueva) {
        this.game = game;
        this.cueva = cueva;
    }
    @Override
    public void show() {
        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO/2, DonChito.ALTO_MUNDO/2);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();

        cameraHUD = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        cameraHUD.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        cameraHUD.update();

        view = new FitViewport(DonChito.ANCHO_MUNDO, DonChito.ALTO_MUNDO, camera);
        view.apply();
        batch = new SpriteBatch();
        historieta = new SimpleAsset(Constants.SECUENCIAS_HISTORIETA,0,0);
        botonPlay =  new SimpleAsset(Constants.GLOBAL_BOTON_PLAY_PNG,1100,0);
        leerEntrada();
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        view.apply();
        batch.begin();

        historieta.render(batch);
        if(indicePosiciones == 2){
            camera = new OrthographicCamera(DonChito.ANCHO_MUNDO/1.5f, DonChito.ALTO_MUNDO/1.5f);
        }
        else if(indicePosiciones == 4){
            camera = new OrthographicCamera(DonChito.ANCHO_MUNDO/1.2f, DonChito.ALTO_MUNDO/1.2f);
        }
        if(acabar){
            dispose();
            if(cueva){
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.CUEVA,game));
            }else{
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.ACERCA,game));
            }

        }
        if(esperar(delta)){
            if (indicePosiciones < maxPosiciones - 1) {
                indicePosiciones++;
            } else {
                acabar = true;
            }
        }
        else{
            hacerTransicion();
        }
        camera.update();
        batch.end();

        batch.setProjectionMatrix(cameraHUD.combined);
        batch.begin();
        botonPlay.render(batch);
        batch.end();
    }

    private void hacerTransicion() {
        switch(indicePosiciones) {
            case 1:
                if(currx<900) {
                    currx += 5f;
                }
                else{
                    currx = 900;
                }
                camera.position.set(currx, curry, 0);
                break;
            case 2:
                if(currx >300) {
                    currx -= 8f;
                }
                else{
                    currx = 300;
                }
                if(curry>800) {
                    curry -= 6f;
                }
                else{
                    curry = 800;
                }
                camera.position.set(currx, curry, 0);
                break;
            case 3:
                if(currx<850) {
                    currx += 5f;
                }
                else{
                    currx = 850;
                }
                camera.position.set(currx, curry, 0);
                break;
            case 4:
                if(currx >300) {
                    currx -= 8f;
                }
                else{
                    currx = 300;
                }
                if(curry>300) {
                    curry -= 6f;
                }
                else{
                    curry = 300;
                }
                camera.position.set(currx, curry, 0);
                break;
            case 5:case 6:
                if(currx<900) {
                    currx += 2f;
                }
                else{
                    currx = 900;
                }
                camera.position.set(currx, curry, 0);
                break;
            case 0: default:
                camera.position.set(posicionesX[indicePosiciones], posicionesY[indicePosiciones], 0);
                break;
        }
    }

    private boolean esperar(float delta){
        if(tiempoEsperar <=0){
            tiempoEsperar = 5f;
            return true;
        }
        tiempoEsperar -= delta;
        return false;
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

    private void leerEntrada() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchDown(int x, int y, int pointer, int button) {
                if(botonPlay.isTouched(x,y,cameraHUD)){
                    acabar = true;
                }
                return true;
            }
        });
    }
    private void cargarAudio() {

    }
}

