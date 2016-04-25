package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.TimeUtils;


import org.omg.CORBA.BAD_TYPECODE;

import java.sql.Time;

import static mx.itesm.donchito.LoadingScreen.*;
/**
 * Created on 25-Apr-16.
 *
 * @author Joel Lara
 * @project DonChito.
 * @package mx.itesm.donchito.
 */
public class PostGameStatusScreen implements Screen {

    private final DonChito game;
    private ScreenSel screenSel,comingFrom;
    private boolean winStat; //winOrNot
    private Camera camera;
    private SpriteBatch batch;
    private Texture fondo;
    private SimpleAsset item;
    private long startTime;

    public PostGameStatusScreen(ScreenSel screenSel,DonChito game,boolean victory, ScreenSel comingFrom){
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
        batch = new SpriteBatch();
        DonChito.assetManager.load(Constants.LIVERMORIO_ITEM,Texture.class);
        DonChito.assetManager.load(Constants.FLEVORIO_BOTONCENTRAL_PNG,Texture.class);
        DonChito.assetManager.finishLoading();
        if(this.winStat){
            //TODO CAMBIAR FONDO
            fondo = new Texture(Constants.FINAL_BOSS_WIN);
            if(this.comingFrom == ScreenSel.FINALBOSS){
                fondo = new Texture(Constants.FINAL_BOSS_WIN);
            }
            else{
                switch (this.comingFrom){
                    case LIVERMORIO:
                        item = new SimpleAsset(Constants.LIVERMORIO_ITEM,0,0);
                        break;
                    case FLEVORIO:
                        item = new SimpleAsset(Constants.FLEVORIO_BOTONCENTRAL_PNG,0,0);
                        break;
                    case ROMANSTRUGGLE:
                        item = new SimpleAsset(Constants.LIVERMORIO_ITEM,0,0);
                        break;
                    default:
                        item = new SimpleAsset(Constants.LIVERMORIO_ITEM,0,0);
                        break;
                }
                item.setPosition(DonChito.ANCHO_MUNDO/2 - item.getSprite().getWidth()/2,DonChito.ALTO_MUNDO/2-item.getSprite().getHeight()/2);
            }

        }else{
            fondo = new Texture(Constants.CTHULHU);
        }
        startTime = TimeUtils.millis();
    }

    @Override
    public void render(float delta) {
        if(((TimeUtils.millis()-startTime)/1000)<3){
            batch.begin();
            batch.draw(fondo,0,0);
            if(this.winStat && this.comingFrom != ScreenSel.FINALBOSS){
                item.render(batch);
            }
            batch.end();
        }else{
            dispose();
            game.setScreen(new LoadingScreen(this.screenSel,game));
        }

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
