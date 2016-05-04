package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

import static mx.itesm.donchito.LoadingScreen.ScreenSel;

/**
 * Created on 25-Apr-16.
 *
 * @author Joel Lara
 * @project DonChito.
 * @package mx.itesm.donchito.
 */
public class PostGameStatusScreen implements Screen {

    private final DonChito game;
    private ScreenSel screenSel, comingFrom;
    private boolean winStat; //winOrNot
    private Camera camera;
    private SpriteBatch batch;
    private Texture fondo;
    private SimpleAsset item;
    private Viewport view;
    private long startTime;
    Random randomIntGenerator = new Random();
    private Music dialogo;

    public PostGameStatusScreen(ScreenSel screenSel, DonChito game, boolean victory, ScreenSel comingFrom) {
        this.game = game;
        this.screenSel = screenSel;
        this.winStat = victory;
        this.comingFrom = comingFrom;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO, DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();
        view = new FitViewport(DonChito.ANCHO_MUNDO, DonChito.ALTO_MUNDO, camera);
        view.apply();
        batch = new SpriteBatch();

        AssetManager assetManager;
        if (this.winStat) {
            fondo = new Texture(Constants.WIN_SCREEN);
            if (this.comingFrom == ScreenSel.FINALBOSS) {
                fondo = new Texture(Constants.FINAL_BOSS_WIN);
            } else {
                switch (this.comingFrom) {
                    case LIVERMORIO:
                        DonChito.assetManager.load(Constants.LIVERMORIO_ITEM, Texture.class);
                        DonChito.assetManager.load(Constants.DIALOGO_BOTAS, Music.class);
                        DonChito.assetManager.finishLoading();
                        item = new SimpleAsset(Constants.LIVERMORIO_ITEM, 0, 0);

                        assetManager = DonChito.assetManager;
                        dialogo = assetManager.get(Constants.DIALOGO_BOTAS);
                        break;
                    case FLEVORIO:
                        DonChito.assetManager.load(Constants.FLEVORIO_BOTONCENTRAL_PNG, Texture.class);
                        DonChito.assetManager.load(Constants.DIALOGO_PIEDRA, Music.class);
                        DonChito.assetManager.finishLoading();
                        item = new SimpleAsset(Constants.FLEVORIO_BOTONCENTRAL_PNG, 0, 0);

                        assetManager = DonChito.assetManager;
                        dialogo = assetManager.get(Constants.DIALOGO_PIEDRA);
                        break;
                    case ROMANSTRUGGLE:
                        DonChito.assetManager.load(Constants.ROMAN_HONDA, Texture.class);
                        DonChito.assetManager.load(Constants.DIALOGO_RESORTERA, Music.class);
                        DonChito.assetManager.finishLoading();
                        item = new SimpleAsset(Constants.ROMAN_HONDA, 0, 0);
                        item.getSprite().setScale(0.25f);

                        assetManager = DonChito.assetManager;
                        dialogo = assetManager.get(Constants.DIALOGO_RESORTERA);
                        break;
                    default:
                        break;
                }
                reproducirMusica(dialogo);
                if (this.comingFrom == ScreenSel.ROMANSTRUGGLE) {
                    item.setPosition(DonChito.ANCHO_MUNDO / 2 - item.getSprite().getWidth() / 2, DonChito.ALTO_MUNDO / 2 - item.getSprite().getHeight() / 1.75f);
                } else if (this.comingFrom == ScreenSel.FLEVORIO) {
                    item.setPosition(DonChito.ANCHO_MUNDO / 2 - item.getSprite().getWidth() / 2, DonChito.ALTO_MUNDO / 2 - item.getSprite().getHeight() / 1.5f);
                } else {
                    item.setPosition(DonChito.ANCHO_MUNDO / 2 - item.getSprite().getWidth() / 2, DonChito.ALTO_MUNDO / 2 - item.getSprite().getHeight() / 2);
                }
            }

        } else {
            if (randomIntGenerator.nextInt(1) == 1) {
                DonChito.assetManager.load(Constants.DIALOGO_PERDER1, Music.class);
                DonChito.assetManager.finishLoading();
                assetManager = DonChito.assetManager;
                dialogo = assetManager.get(Constants.DIALOGO_PERDER1);
            } else {
                DonChito.assetManager.load(Constants.DIALOGO_PERDER2, Music.class);
                DonChito.assetManager.finishLoading();
                assetManager = DonChito.assetManager;
                dialogo = assetManager.get(Constants.DIALOGO_PERDER2);
            }
            fondo = new Texture(Constants.CTHULHU);
            reproducirMusica(dialogo);
        }
        startTime = TimeUtils.millis();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        if (((TimeUtils.millis() - startTime) / 1000) < 5) {
            batch.begin();
            batch.draw(fondo, 0, 0);
            if (this.winStat && this.comingFrom != ScreenSel.FINALBOSS) {
                item.render(batch);
            }
            batch.end();
        } else {
            dispose();
            game.setScreen(new LoadingScreen(this.screenSel, game));
        }

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

    private void reproducirMusica(Music musica) {
        musica.setVolume(1F);
        if (!DonChito.preferences.getBoolean(Constants.MENUPRINCIPAL_SOUND_PREF, true)) {
            musica.setVolume(0f);
        }
        musica.play();
    }

    @Override
    public void dispose() {
        DonChito.assetManager.clear();
    }
}
