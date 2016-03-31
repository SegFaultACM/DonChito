package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class LivermorioEscape implements Screen {
    private OrthographicCamera camera,cameraHUD;
    private final DonChito game;
    private Viewport view;

    //TODO Refactor to interface
    private SimpleAsset fondo,deathByRobot;
    private SpriteBatch batch;
    private Music musicaFondo;

    Array<SimpleAsset> platforms;

    DonChitoLivermorio player;
    public static final float DEATH_MOVE_SPEED = 200;

    public LivermorioEscape(DonChito game) {
        this.game = game;
    }

    @Override
    public void show() {

        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();

        view = new FitViewport(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO,camera);
        batch = new SpriteBatch();

        platforms = new Array<SimpleAsset>();
        player = new DonChitoLivermorio();

        cargarAudio();
        cargarRecursos();

        //Death by Sandd
        deathByRobot = new SimpleAsset(Constants.PLATFORM,-4000,0);
        deathByRobot.getSprite().setRotation(90);
        deathByRobot.getSprite().setScale(3, 10);

        platforms.add(new SimpleAsset(Constants.PLATFORM, 20, 20));
        platforms.add(new SimpleAsset(Constants.PLATFORM, 300, 200));

        fondo = new SimpleAsset(Constants.LIVERMORIO_FONDO_PNG,0,0);

    }

    private void cargarRecursos() {

        AssetManager assetManager = DonChito.getAssetManager();
        assetManager.load(Constants.PLATFORM, Texture.class);
        assetManager.load(Constants.LIVERMORIO_FONDO_PNG, Texture.class);
        assetManager.finishLoading();

    }

    private void cargarAudio() {

    }


    @Override
    public void render(float delta) {

        //Limpiar pantalla
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        view.apply();
        actualizarCamara();

        batch.setProjectionMatrix(camera.combined);

        player.update(delta, platforms);

        batch.begin();

        fondo.render(batch);
        for (SimpleAsset platform : platforms) {
            platform.render(batch);
        }
        //actualizar posición
        deathByRobot.setPosition(deathByRobot.getSprite().getX() + (delta * DEATH_MOVE_SPEED), deathByRobot.getSprite().getY());
        player.render(batch);
        deathByRobot.render(batch);

        batch.end();
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

    }
    private void actualizarCamara() {
        float posX = player.getX();
        if (posX>=game.ANCHO_MUNDO/2 && posX<=1280) {
            camera.position.set((int)posX, camera.position.y, 0);
        } else if (posX<1280-game.ANCHO_MUNDO/2) {
            camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        }
        camera.update();
    }
    enum State{
        DEAD,
        NOTDEAD
    }
}
