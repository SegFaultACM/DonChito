package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created on 06-Apr-16.
 *
 * @author Joel Lara
 * @project DonChito.
 * @package mx.itesm.donchito.
 */
public class LoadingScreen implements Screen {

    private final DonChito game;
    private ScreenSel screenSel;
    private SimpleAsset loading;
    private AssetManager assetManager;
    private Camera camera;
    private Viewport viewport;
    private GameText loadingTxt;
    private SpriteBatch batch;
    private static final int LOADING_SIZE = 195;

    public LoadingScreen(ScreenSel screenSel,DonChito game) {
        this.game = game;
        this.screenSel = screenSel;
        this.assetManager = DonChito.assetManager;
    }
    @Override
    public void show() {
        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO, DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();
        assetManager.load(Constants.ROMAN_PIEDRA, Texture.class);
        assetManager.finishLoading();
        loading = new SimpleAsset(Constants.ROMAN_PIEDRA,DonChito.ANCHO_MUNDO / 2 - LOADING_SIZE /2,DonChito.ALTO_MUNDO/2-LOADING_SIZE/2);
        viewport = new FitViewport(DonChito.ANCHO_MUNDO, DonChito.ALTO_MUNDO, camera);
        loadingTxt = new GameText(DonChito.ANCHO_MUNDO / 2 - LOADING_SIZE /2+50,DonChito.ALTO_MUNDO/2-LOADING_SIZE/2 - 75);
        loadingTxt.setColor(.323f,.4823f,.649f,1);
        batch = new SpriteBatch();

        loadResources();
    }

    public void loadResources(){
        switch (screenSel){
            case CUEVA:

                assetManager.load(Constants.CUEVA_FONDO_JPG,Texture.class);
                assetManager.load(Constants.CUEVA_DON_CHITO_PNG,Texture.class);
                assetManager.load(Constants.CUEVA_ARROW_UP,Texture.class);
                assetManager.load(Constants.CUEVA_ARROW_DOWN,Texture.class);
                assetManager.load(Constants.CUEVA_ARROW_LEFT,Texture.class);
                assetManager.load(Constants.CUEVA_ARROW_RIGHT,Texture.class);
                //assetManager.load(Constants.CUEVA_TILES, Texture.class);

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
                for (String platform:Constants.PLATFORMS
                        ) {
                    assetManager.load(platform,Texture.class);
                }
                assetManager.load(Constants.LIVERMORIO_FONDO_PNG, Texture.class);
                assetManager.load(Constants.DEATHBYROBOT,Texture.class);
                assetManager.load(Constants.CTHULHU,Texture.class);


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
                //ROMAN_SPRITES_DONCHITO
                assetManager.load(Constants.CTHULHU, Texture.class);

                break;
            case RADCLIFF:
                break;
            case MENU: default:
                assetManager.load(Constants.MENUPRINCIPAL_FONDO_JPG,Texture.class);
                assetManager.load(Constants.MENUPRINCIPAL_CARGARPARTIDA_PNG,Texture.class);
                assetManager.load(Constants.MENUPRINCIPAL_NUEVAPARTIDA_PNG,Texture.class);
                assetManager.load(Constants.MENUPRINCIPAL_CARTELDONCHITO_PNG,Texture.class);
                assetManager.load(Constants.MENUPRINCIPAL_EXTRA_PNG,Texture.class);
                assetManager.load(Constants.MENUPRINCIPAL_PALA_PNG,Texture.class);
                assetManager.load(Constants.AJUSTES_BOTON_PNG,Texture.class);
                assetManager.load(Constants.MENU_PRINCIPAL_MP3,Music.class);
                assetManager.load(Constants.GLOBAL_MENU_PAUSA_PNG, Texture.class);
                assetManager.load(Constants.GLOBAL_BOTON_SALIRMENU_PNG, Texture.class);
                break;
        }
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);    // r, g, b, alpha  //color NEGRO
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateScreen();
        if (assetManager.update()) {// Terminó la carga, cambiar de pantalla
            switch(this.screenSel){//dependiendo que se cargo, cambiamos la pantalla...
                case MENU:
                    game.setScreen(new MenuPrincipal(game));
                    break;
                case ACERCA:
                    game.setScreen(new AcercaDe(game));
                    break;
                case LIVERMORIO:
                    game.setScreen(new LivermorioEscape(game));
                    break;
                case RADCLIFF:
                    game.setScreen(new RaddcliffFight(game));
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
                default:
                    game.setScreen(new MenuPrincipal(game));
                    break;
            }
        }
        else {
            float avance = assetManager.getProgress()*100;
            Gdx.app.log("Loading",avance+" %");
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
        RADCLIFF,
        CUEVA,
        ACERCA
    }
}
