package mx.itesm.donchito;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;


/**
 * Created on 22-Apr-16.
 *
 * @author Joel Lara
 * @project DonChito.
 * @package mx.itesm.donchito.
 */
public class SplashScreen implements Screen {
    private long startTime;
    private Texture logoTec;
    private DonChito game;
    private Batch batch;

    public SplashScreen(DonChito game){
        this.game = game;
    }
    @Override
    public void show() {
        batch = new SpriteBatch();
        logoTec = DonChito.assetManager.get(Constants.LOGO_TEC);
        startTime = TimeUtils.millis();

    }

    @Override
    public void render(float delta) {
        batch.begin();
        if(((TimeUtils.millis()-startTime)/1000)>5){
            game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.MENU, game));
        }else{
            batch.draw(logoTec,0,0);
        }
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
        DonChito.assetManager.clear();
    }
}
