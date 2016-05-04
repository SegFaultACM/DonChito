package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/**
 * Created on 22-Apr-16.
 *
 * @author Joel Lara
 * @project DonChito.
 * @package mx.itesm.donchito.
 */
public class SplashScreen implements Screen {
    private long startTime;
    private SimpleAsset logoTec;
    private DonChito game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport view;

    public SplashScreen(DonChito game) {
        this.game = game;
    }

    @Override
    public void show() {
        DonChito.assetManager.load(Constants.LOGO_TEC, Texture.class);
        DonChito.assetManager.finishLoading();
        batch = new SpriteBatch();
        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO, DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();
        view = new FitViewport(DonChito.ANCHO_MUNDO, DonChito.ALTO_MUNDO, camera);
        logoTec = new SimpleAsset(Constants.LOGO_TEC, 0, 0);
        startTime = TimeUtils.millis();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (((TimeUtils.millis() - startTime) / 1000) > 2.5) {
            dispose();
            game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.MENU, game));
        } else {
            logoTec.render(batch);
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
}
