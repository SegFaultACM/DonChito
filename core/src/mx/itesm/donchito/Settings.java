package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Settings implements Screen {
    private OrthographicCamera camera;
    private final DonChito game;
    private Viewport view;

    private SpriteBatch batch;

    BitmapFont title = new BitmapFont();
    BitmapFont musicEnable = new BitmapFont();
    BitmapFont soundEnable = new BitmapFont();


    private SimpleAsset background;

    public Settings(DonChito game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Initializations
        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();
        view = new FitViewport(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO,camera);
        batch = new SpriteBatch();

        // Assets and text declaration
        background = new SimpleAsset(Constants.PANTALLA_CONFIG_PNG,new Vector2(0,0));
    }

    private void cargarAudio() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.render(batch);
        title.draw(batch, Constants.TITULO_OPCIONES, 350, 600);
        soundEnable.draw(batch, Constants.TEXTO_SONIDO_OPCIONES,DonChito.ANCHO_MUNDO / 3 ,400);
        musicEnable.draw(batch, Constants.TEXTO_MUSICA_OPCIONES, DonChito.ANCHO_MUNDO / 3, 300);
        title.getData().setScale(2.5f);
        soundEnable.getData().setScale(1.7f);
        musicEnable.getData().setScale(1.7f);
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
}
