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
import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * Created on 06-Apr-16.
 *
 * @author Joel Lara
 * @project DonChito.
 * @package mx.itesm.donchito.
 */
public class LoadingScreen implements Screen {

    private DonChito game;
    private ScreenSel screenSel,comingFrom;
    private boolean winStat; //winOrNot
    private SimpleAsset loading;
    private AssetManager assetManager;
    private Camera camera;
    private GameText loadingTxt;
    private SpriteBatch batch;
    private static final int LOADING_SIZE = 195;

    public LoadingScreen(ScreenSel screenSel,DonChito game) {
        this.game = game;
        this.screenSel = screenSel;
        this.assetManager = DonChito.assetManager;
        this.comingFrom = ScreenSel.NONE;
    }
    public LoadingScreen(ScreenSel screenSel,DonChito game,boolean victory, ScreenSel comingFrom) {
        this.game = game;
        this.screenSel = screenSel;
        this.assetManager = DonChito.assetManager;
        this.winStat = victory;
        this.comingFrom = comingFrom;
    }

    @Override
    public void show() {
        if(comingFrom != ScreenSel.NONE){
            dispose();
            game.setScreen(new PostGameStatusScreen(screenSel, game, winStat, comingFrom));
        }
        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO, DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();
        assetManager.load(Constants.ROMAN_PIEDRA, Texture.class);
        assetManager.finishLoading();
        loading = new SimpleAsset(Constants.ROMAN_PIEDRA,DonChito.ANCHO_MUNDO / 2 - LOADING_SIZE /2,DonChito.ALTO_MUNDO/2-LOADING_SIZE/2);
        loadingTxt = new GameText(DonChito.ANCHO_MUNDO / 2 - LOADING_SIZE /2+50,DonChito.ALTO_MUNDO/2-LOADING_SIZE/2 - 75);
        loadingTxt.setColor(.323f,.4823f,.649f,1);
        batch = new SpriteBatch();

        loadResources();
    }

    public void loadResources(){
        switch (screenSel){
            case SPLASH:
                assetManager.load(Constants.LOGO_TEC,Texture.class);
                break;
            case CUEVA:
                assetManager.load(Constants.CUEVA_FONDO_JPG,Texture.class);
                assetManager.load(Constants.CUEVA_DON_CHITO_PNG,Texture.class);
                assetManager.load(Constants.CUEVA_ARROW_UP,Texture.class);
                assetManager.load(Constants.CUEVA_ARROW_DOWN,Texture.class);
                assetManager.load(Constants.CUEVA_ARROW_LEFT,Texture.class);
                assetManager.load(Constants.CUEVA_ARROW_RIGHT,Texture.class);
                assetManager.load(Constants.CUEVA_TILES, TiledMap.class);
                break;
            case ACERCA:
                assetManager.load(Constants.ACERCA_FONDO_JPG,Texture.class);
                assetManager.load(Constants.ACERCA_JOEL_PNG,Texture.class);
                assetManager.load(Constants.ACERCA_KARLA_PNG,Texture.class);
                assetManager.load(Constants.ACERCA_LICHO_PNG,Texture.class);
                assetManager.load(Constants.ACERCA_SADA_PNG,Texture.class);
                assetManager.load(Constants.ACERCA_STEVE_PNG,Texture.class);
                assetManager.load(Constants.ACERCA_DESC_JOEL_PNG,Texture.class);
                assetManager.load(Constants.ACERCA_DESC_KARLA_PNG,Texture.class);
                assetManager.load(Constants.ACERCA_DESC_LICHO_PNG,Texture.class);
                assetManager.load(Constants.ACERCA_DESC_SADA_PNG,Texture.class);
                assetManager.load(Constants.ACERCA_DESC_STEVE_PNG,Texture.class);
                assetManager.load(Constants.ACERCA_REGRESAR,Texture.class);
                break;
            case FLEVORIO:
                assetManager.load(Constants.FLEVORIO_FONDOPANTALLA_PNG,Texture.class);
                assetManager.load(Constants.FLEVORIO_FONDO_PNG,Texture.class);
                assetManager.load(Constants.FLEVORIO_BOTONCENTRAL_PNG, Texture.class);
                assetManager.load(Constants.FLEVORIO_BOTON2_PNG, Texture.class);
                assetManager.load(Constants.FLEVORIO_BOTON3_PNG, Texture.class);

                assetManager.load(Constants.FLEVORIO_INSTRUCCION1, Texture.class);
                assetManager.load(Constants.FLEVORIO_INSTRUCCION2, Texture.class);
                assetManager.load(Constants.FLEVORIO_INSTRUCCION3, Texture.class);
                assetManager.load(Constants.FLEVORIO_INSTRUCCION4, Texture.class);
                assetManager.load(Constants.FLEVORIO_INSTRUCCION5, Texture.class);
                assetManager.load(Constants.FLEVORIO_INSTRUCCION6, Texture.class);
                assetManager.load(Constants.FLEVORIO_INSTRUCCION7, Texture.class);
                assetManager.load(Constants.FLEVORIO_INSTRUCCION8, Texture.class);
                assetManager.load(Constants.FLEVORIO_INSTRUCCION9, Texture.class);
                assetManager.load(Constants.FLEVORIO_INSTRUCCION10, Texture.class);
                assetManager.load(Constants.FLEVORIO_INSTRUCCION11, Texture.class);
                assetManager.load(Constants.FLEVORIO_INSTRUCCION12, Texture.class);
                assetManager.load(Constants.FLEVORIO_INSTRUCCION13, Texture.class);
                assetManager.load(Constants.FLEVORIO_INSTRUCCION14, Texture.class);
                assetManager.load(Constants.FLEVORIO_INSTRUCCION15, Texture.class);

                assetManager.load(Constants.GLOBAL_MENU_PAUSA_PNG, Texture.class);
                assetManager.load(Constants.GLOBAL_BOTON_PAUSA_PNG, Texture.class);
                assetManager.load(Constants.GLOBAL_BOTON_PLAY_PNG, Texture.class);
                assetManager.load(Constants.GLOBAL_BOTON_CONFIGURACION_PNG, Texture.class);
                assetManager.load(Constants.GLOBAL_BOTON_SALIRMENU_PNG, Texture.class);

                assetManager.load(Constants.FLEVORIO_SONIDOBOTON_WAV, Music.class);
                assetManager.load(Constants.FLEVORIO_SONIDOFAIL_WAV,Music.class);
                assetManager.load(Constants.FLEVORIO_SONIDOVICTORY_WAV, Music.class);
                assetManager.load(Constants.FLEVORIO_MUSICAINTRO_WAV, Music.class);
                assetManager.load(Constants.FLEVORIO_MUSICAFONDO_WAV,Music.class);
                break;
            case LIVERMORIO:
                for (String platform:Constants.PLATFORMS_CARRETAS
                        ) {
                    assetManager.load(platform,Texture.class);
                }
                for (String platform:Constants.PLATFORMS_MADERA
                        ) {
                    assetManager.load(platform,Texture.class);
                }
                assetManager.load(Constants.LIVERMORIO_FONDO_PNG, Texture.class);
                assetManager.load(Constants.DEATHBYROBOT,Texture.class);
                assetManager.load(Constants.CTHULHU,Texture.class);
                assetManager.load(Constants.LIVERMORIO_ITEM,Texture.class);

                assetManager.load(Constants.CUEVA_ARROW_UP,Texture.class);
                assetManager.load(Constants.CUEVA_ARROW_LEFT,Texture.class);
                assetManager.load(Constants.CUEVA_ARROW_RIGHT,Texture.class);

                assetManager.load(Constants.GLOBAL_MENU_PAUSA_PNG, Texture.class);
                assetManager.load(Constants.GLOBAL_BOTON_PAUSA_PNG, Texture.class);
                assetManager.load(Constants.GLOBAL_BOTON_PLAY_PNG, Texture.class);
                assetManager.load(Constants.GLOBAL_BOTON_CONFIGURACION_PNG, Texture.class);
                assetManager.load(Constants.GLOBAL_BOTON_SALIRMENU_PNG, Texture.class);
                break;
            case ROMANSTRUGGLE:

                assetManager.load(Constants.ROMAN_BOTON_IZQUIERDA,Texture.class);
                assetManager.load(Constants.ROMAN_BOTON_DERECHA, Texture.class);
                assetManager.load(Constants.ROMAN_BOTON_DISPARA, Texture.class);
                assetManager.load(Constants.ROMAN_PERSONAJE_DONCHITO, Texture.class);
                assetManager.load(Constants.ROMAN_FONDO, Texture.class);
                assetManager.load(Constants.ROMAN_PIEDRA, Texture.class);

                assetManager.load(Constants.GLOBAL_MENU_PAUSA_PNG, Texture.class);
                assetManager.load(Constants.GLOBAL_BOTON_PAUSA_PNG, Texture.class);
                assetManager.load(Constants.GLOBAL_BOTON_PLAY_PNG, Texture.class);
                assetManager.load(Constants.GLOBAL_BOTON_CONFIGURACION_PNG, Texture.class);
                assetManager.load(Constants.GLOBAL_BOTON_SALIRMENU_PNG, Texture.class);

                assetManager.load(Constants.FLEVORIO_SONIDOFAIL_WAV,Music.class);
                assetManager.load(Constants.FLEVORIO_SONIDOVICTORY_WAV, Music.class);
                //ROMAN_SPRITES_DONCHITO
                assetManager.load(Constants.CTHULHU, Texture.class);
                break;
            case FINALBOSS:
                assetManager.load(Constants.FINAL_BOSS_FONDO,Texture.class);
                assetManager.load(Constants.FINAL_BOSS_DONCHITO,Texture.class);
                assetManager.load(Constants.FINAL_BOSS_FLEVORIO,Texture.class);
                assetManager.load(Constants.FINAL_BOSS_BOTAS,Texture.class);
                assetManager.load(Constants.FINAL_BOSS_PIEDRA,Texture.class);
                assetManager.load(Constants.FINAL_BOSS_PICO,Texture.class);
                assetManager.load(Constants.FINAL_BOSS_RESORTERA,Texture.class);
                assetManager.load(Constants.FINAL_BOSS_HEALTHBAR,Texture.class);
                assetManager.load(Constants.FINAL_BOSS_HEALTHBARB,Texture.class);
                assetManager.load(Constants.GLOBAL_MENU_PAUSA_PNG, Texture.class);
                assetManager.load(Constants.GLOBAL_BOTON_PAUSA_PNG, Texture.class);
                assetManager.load(Constants.GLOBAL_BOTON_PLAY_PNG, Texture.class);
                assetManager.load(Constants.GLOBAL_BOTON_CONFIGURACION_PNG, Texture.class);
                assetManager.load(Constants.GLOBAL_BOTON_SALIRMENU_PNG, Texture.class);
                
                assetManager.load(Constants.FINAL_BOSS_MUSICA, Music.class);
                break;
            case MENU:
            default:
                assetManager.load(Constants.MENUPRINCIPAL_FONDO_JPG,Texture.class);
                assetManager.load(Constants.MENUPRINCIPAL_CARGARPARTIDA_PNG,Texture.class);
                assetManager.load(Constants.MENUPRINCIPAL_NUEVAPARTIDA_PNG,Texture.class);
                assetManager.load(Constants.MENUPRINCIPAL_CARTELDONCHITO_PNG,Texture.class);
                assetManager.load(Constants.MENUPRINCIPAL_EXTRA_PNG,Texture.class);
                assetManager.load(Constants.MENUPRINCIPAL_PALA_PNG,Texture.class);
                assetManager.load(Constants.AJUSTES_BOTON_PNG,Texture.class);
                assetManager.load(Constants.MENU_PRINCIPAL_MP3,Music.class);
                assetManager.load(Constants.PANTALLA_CONFIG_PNG, Texture.class);
                assetManager.load(Constants.GLOBAL_BOTON_SALIRMENU_PNG, Texture.class);
                assetManager.load(Constants.MENUPRINCIPAL_SOUND_ON, Texture.class);
                assetManager.load(Constants.MENUPRINCIPAL_SOUND_OFF, Texture.class);
                break;
        }
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateScreen();
        if (assetManager.update()) {
            switch(this.screenSel){
                case MENU:
                    game.setScreen(new MenuPrincipal(game));
                    break;
                case ACERCA:
                    game.setScreen(new AcercaDe(game));
                    break;
                case LIVERMORIO:
                    game.setScreen(new LivermorioEscape(game));
                    break;
                case ROMANSTRUGGLE:
                    game.setScreen(new RomanStruggle(game));
                    break;
                case CUEVA:
                    game.setScreen(new Cueva(game));
                    break;
                case FLEVORIO:
                    game.setScreen(new FlevorioSays(game));
                    break;
                case SPLASH:
                    game.setScreen(new SplashScreen(game));
                    break;
                case FINALBOSS:
                    game.setScreen(new FinalBoss(game));
                    break;
                default:
                    game.setScreen(new MenuPrincipal(game));
                    break;
            }
        }
        loading.setRotation(loading.getSprite().getRotation() + 5);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        loading.render(batch);
        loadingTxt.showMessage(batch,"Loading...");
        batch.end();
    }

    public void updateScreen(){

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

    }
    enum ScreenSel {
        MENU,
        FLEVORIO,
        LIVERMORIO,
        ROMANSTRUGGLE,
        CUEVA,
        SPLASH,
        FINALBOSS,
        ACERCA,
        NONE
    }
}
