package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    private float tiempoEsperar = 3f;

    private int indicePosiciones = 0;
    private int maxPosiciones = 7;

    private Music musicaFondo;

    private boolean cueva;
    private boolean acabar = false;

    private float[] posicionesX = new float[]{671, 2000, 700, 1800, 671, 2000, 2000};
    private float[] posicionesY = new float[]{3047, 3058, 1900, 1800, 630, 630, 680};

    private float currx = posicionesX[0];
    private float curry = posicionesY[0];

    private float currCamaraX = DonChito.ANCHO_MUNDO;
    private float currCamaraY = DonChito.ALTO_MUNDO;

    private SpriteBatch batch;

    public Secuencia(DonChito game, boolean cueva) {
        this.game = game;
        this.cueva = cueva;
    }

    @Override
    public void show() {
        //camera = new OrthographicCamera(DonChito.ANCHO_MUNDO/2, DonChito.ALTO_MUNDO/2);
        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO, DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();

        cameraHUD = new OrthographicCamera(DonChito.ANCHO_MUNDO, DonChito.ALTO_MUNDO);
        cameraHUD.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        cameraHUD.update();

        view = new FitViewport(DonChito.ANCHO_MUNDO, DonChito.ALTO_MUNDO, camera);
        view.apply();
        batch = new SpriteBatch();
        historieta = new SimpleAsset(Constants.SECUENCIAS_HISTORIETA, 0, 0);
        botonPlay = new SimpleAsset(Constants.GLOBAL_BOTON_PLAY_PNG, 1100, 0);
        leerEntrada();
        cargarAudio();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        view.apply();
        batch.begin();

        historieta.render(batch);
        if (indicePosiciones == 2) {
            if (currCamaraX < DonChito.ANCHO_MUNDO * 1.5f && currCamaraY < DonChito.ALTO_MUNDO * 1.5f) {
                currCamaraY *= 1.005f;
                currCamaraX *= 1.005f;
            } else {
                currCamaraY = DonChito.ALTO_MUNDO * 1.5f;
                currCamaraX = DonChito.ANCHO_MUNDO * 1.5f;
            }
        } else if (indicePosiciones == 4) {
            if (currCamaraX < DonChito.ANCHO_MUNDO * 1.8f && currCamaraY < DonChito.ALTO_MUNDO * 1.8f) {
                currCamaraY *= 1.005f;
                currCamaraX *= 1.005f;
            } else {
                currCamaraY = DonChito.ALTO_MUNDO * 1.8f;
                currCamaraX = DonChito.ANCHO_MUNDO * 1.8f;
            }
        }
        camera = new OrthographicCamera(currCamaraX, currCamaraY);
        if (acabar) {
            dispose();
            if (cueva) {
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.CUEVA, game));
            } else {
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.ACERCA, game));
            }

        }
        if (esperar(delta)) {
            if (indicePosiciones < maxPosiciones - 1) {
                indicePosiciones++;
            } else {
                acabar = true;
            }
        } else {
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
        switch (indicePosiciones) {
            case 1:
                if (currx < posicionesX[indicePosiciones]) {
                    currx += 15f;
                } else {
                    currx = posicionesX[indicePosiciones];
                }
                camera.position.set(currx, curry, 0);
                break;
            case 2:
                if (currx > posicionesX[indicePosiciones]) {
                    currx -= 8f;
                } else {
                    currx = posicionesX[indicePosiciones];
                }
                if (curry > posicionesY[indicePosiciones]) {
                    curry -= 6f;
                } else {
                    curry = posicionesY[indicePosiciones];
                }
                camera.position.set(currx, curry, 0);
                break;
            case 3:
                if (currx < posicionesX[indicePosiciones]) {
                    currx += 5f;
                } else {
                    currx = posicionesX[indicePosiciones];
                }
                camera.position.set(currx, curry, 0);
                break;
            case 4:
                if (currx > posicionesX[indicePosiciones]) {
                    currx -= 8f;
                } else {
                    currx = posicionesX[indicePosiciones];
                }
                if (curry > posicionesY[indicePosiciones]) {
                    curry -= 6f;
                } else {
                    curry = posicionesY[indicePosiciones];
                }
                camera.position.set(currx, curry, 0);
                break;
            case 5:
                if (currx < posicionesX[indicePosiciones]) {
                    currx += 7f;
                } else {
                    currx = posicionesX[indicePosiciones];
                }
                if (curry < posicionesY[indicePosiciones + 1]) {
                    curry += 3f;
                } else {
                    curry = posicionesY[indicePosiciones + 1];
                }
                camera.position.set(currx, curry, 0);
                break;
            case 6:

                camera.position.set(currx, curry, 0);
                break;

            case 0:
            default:
                camera.position.set(posicionesX[indicePosiciones], posicionesY[indicePosiciones], 0);
                break;
        }
    }

    private boolean esperar(float delta) {
        if (tiempoEsperar <= 0) {
            tiempoEsperar = 4f;
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
        stopMusic();
        DonChito.assetManager.clear();
    }

    private void stopMusic() {
        if (musicaFondo.isPlaying()) {
            musicaFondo.stop();
        }
    }

    private void leerEntrada() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchDown(int x, int y, int pointer, int button) {
                if (botonPlay.isTouched(x, y, cameraHUD, view)) {
                    acabar = true;
                }
                return true;
            }
        });
    }

    private void cargarAudio() {
        AssetManager assetManager = DonChito.assetManager;

        musicaFondo = assetManager.get(Constants.MENU_PRINCIPAL_MP3);
        musicaFondo.setLooping(true);
        reproducirMusica(musicaFondo);
    }

    private void reproducirMusica(Music musica) {
        musica.setVolume(1F);
        if (!DonChito.preferences.getBoolean(Constants.MENUPRINCIPAL_SOUND_PREF, true)) {
            musica.setVolume(0f);
        }
        musica.play();
    }
}

