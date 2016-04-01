package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;


public class LivermorioEscape implements Screen {
    private OrthographicCamera camera,cameraHUD;
    private final DonChito game;
    private Viewport view;

    //TODO Refactor to interface
    private SimpleAsset fondo,
                        fondo2,
                        fondoPausa,
                        botonPausa,
                        botonPlay,
                        botonSalirMenu,
                        botonConfiguracion;
    private SpriteBatch batch;
    private Music musicaFondo;

    Array<SimpleAsset> platforms;

    DonChitoLivermorio player;

    private static final float DEATH_MOVE_SPEED = 200;
    private float deathVelocity = 1;
    private Vector2 DeathPosition;
    private Animation animationDeath;
    private float deathStartTime,gameStartTime;
    private int nFondos = 2,posFondos = 10240;
    private TextureRegion regionDeath;

    private PlayerState playerState = PlayerState.NOTDEAD;
    private GameState gameState = GameState.PLAY;
    public LivermorioEscape(DonChito game) {
        this.game = game;
    }

    @Override
    public void show() {

        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();

        cameraHUD = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        cameraHUD.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        cameraHUD.update();

        view = new FitViewport(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO,camera);
        batch = new SpriteBatch();
        platforms = new Array<SimpleAsset>();
        player = new DonChitoLivermorio();
        gameStartTime = TimeUtils.nanoTime();
        cargarAudio();
        cargarRecursos();
        leerEntrada();
        crearElementos();
}
    private void crearElementos(){
        fondoPausa = new SimpleAsset(Constants.GLOBAL_MENU_PAUSA_PNG,0,0);
        botonPlay = new SimpleAsset(Constants.GLOBAL_BOTON_PLAY_PNG,1050,10);
        botonConfiguracion = new SimpleAsset(Constants.GLOBAL_BOTON_CONFIGURACION_PNG,405,175);
        botonSalirMenu = new SimpleAsset(Constants.GLOBAL_BOTON_SALIRMENU_PNG,405,425);
        botonPausa = new SimpleAsset(Constants.GLOBAL_BOTON_PAUSA_PNG,1050,10);
        botonPausa.setAlpha(0.5f);
        botonPlay.setAlpha(0.5f);
        fondo = new SimpleAsset(Constants.LIVERMORIO_FONDO_PNG,0,0);
        fondo2 = new SimpleAsset(Constants.LIVERMORIO_FONDO_PNG,posFondos/2,0);

    }
    private void cargarRecursos() {

        AssetManager assetManager = DonChito.getAssetManager();
        for (String platform:Constants.PLATFORMS
             ) {
            assetManager.load(platform,Texture.class);
        }
        assetManager.load(Constants.LIVERMORIO_FONDO_PNG, Texture.class);
        assetManager.load(Constants.DEATHBYROBOT,Texture.class);
        assetManager.load(Constants.CTHULHU,Texture.class);

        assetManager.load(Constants.GLOBAL_MENU_PAUSA_PNG, Texture.class);
        assetManager.load(Constants.GLOBAL_BOTON_PAUSA_PNG, Texture.class);
        assetManager.load(Constants.GLOBAL_BOTON_PLAY_PNG, Texture.class);
        assetManager.load(Constants.GLOBAL_BOTON_CONFIGURACION_PNG, Texture.class);
        assetManager.load(Constants.GLOBAL_BOTON_SALIRMENU_PNG, Texture.class);
        assetManager.finishLoading();
        //Death by Sandd
        TextureRegion texturaCompleta = new TextureRegion(new Texture(Constants.DEATHBYROBOT));
        TextureRegion[][] texturaDeath = texturaCompleta.split(1280,720);
        animationDeath = new Animation(.15f,texturaDeath[0][0],
                texturaDeath[1][0], texturaDeath[2][0],texturaDeath[3][0]);
        animationDeath.setPlayMode(Animation.PlayMode.LOOP);
        DeathPosition = new Vector2(-2000,0);
        deathStartTime = TimeUtils.nanoTime();
        regionDeath = animationDeath.getKeyFrame(deathStartTime);

    }
    private void leerEntrada() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchUp(int x, int y, int pointer, int button) {
                if(playerState == PlayerState.NOTDEAD){
                    if (gameState == GameState.PAUSE) {
                        if (botonSalirMenu.isTouched(x, y, cameraHUD)) {
                            game.setScreen(new MenuPrincipal(game));
                        }
                        if (botonPlay.isTouched(x, y, cameraHUD)) {
                            gameStartTime = TimeUtils.nanoTime();
                            gameState = GameState.PLAY;
                        }
                    } else {
                        if (botonPausa.isTouched(x, y, cameraHUD)) {
                            gameState = GameState.PAUSE;
                        }
                    }
                }else{
                    DonChito.getAssetManager().clear();
                    game.setScreen(new MenuPrincipal(game));
                }
                return true;
            }

            public boolean touchDown(int x, int y, int pointexr, int button) {

                return true;
            }
        });
    }
    private void cargarAudio() {

    }


    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        view.apply();
        renderCamera();
        checkCollisions();
        if(playerState == PlayerState.NOTDEAD){
            if(gameState == GameState.PLAY){
                actualizarCamara();
                update(delta);
                if(MathUtils.nanoToSec * (TimeUtils.nanoTime() - gameStartTime) >=2){
                    gameStartTime = TimeUtils.nanoTime();
                    platforms.add(new SimpleAsset(Constants.PLATFORMS[(new Random()).nextInt(Constants.PLATFORMS.length)],player.getX()+100,0 + (int)(Math.random() * ((500 - 50) + 1))));
                }
                if((posFondos-1280) < player.getX() ){
                   switch ((nFondos+1)%2){
                       case 1:
                           fondo.setPosition(fondo2.getSprite().getX()+fondo2.getSprite().getWidth(),0);
                           break;
                       case 0:
                           fondo2.setPosition(fondo.getSprite().getX()+fondo.getSprite().getWidth(),0);
                           break;
                   }
                    nFondos++;
                    posFondos += 5120;
                }
            }
            renderHUD();
        }else{
            renderDeath();
        }
    }
    public void checkCollisions(){
        if(DeathPosition.x + regionDeath.getRegionWidth() -200> player.getX()){
            playerState = PlayerState.DEAD;
        }
    }

    public void renderDeath(){
        batch.setProjectionMatrix(cameraHUD.combined);
        batch.begin();
        SimpleAsset fondoDeath = new SimpleAsset(Constants.CTHULHU,0,0);
        fondoDeath.render(batch);
        batch.end();

    }

    public void renderCamera(){
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        fondo.render(batch);
        fondo2.render(batch);
        for (SimpleAsset platform : platforms) {
            platform.render(batch);
        }
        player.render(batch);
        batch.draw(regionDeath, DeathPosition.x, DeathPosition.y);
        batch.end();
    }

    public void renderHUD(){
        batch.setProjectionMatrix(cameraHUD.combined);
        batch.begin();
        if(gameState == GameState.PAUSE){
            fondoPausa.render(batch);
            botonPlay.render(batch);
            botonConfiguracion.render(batch);
            botonSalirMenu.render(batch);
        }else{
            botonPausa.render(batch);
        }
        batch.end();
    }

    public void update(float delta){
        player.update(delta, platforms,gameState);
        regionDeath = animationDeath.getKeyFrame(MathUtils.nanoToSec * (TimeUtils.nanoTime() - deathStartTime));
        deathVelocity += delta*.01;
        DeathPosition.x += delta * DEATH_MOVE_SPEED * deathVelocity;
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
        if (posX>=DonChito.ANCHO_MUNDO/2) {
            camera.position.set((int) posX, camera.position.y, 0);
        } else if (posX<DonChito.ANCHO_MUNDO-DonChito.ANCHO_MUNDO/2) {
            camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        }
        camera.update();
    }
    enum PlayerState{
        DEAD,
        NOTDEAD
    }
    enum GameState{
        PLAY,
        PAUSE
    }
}
