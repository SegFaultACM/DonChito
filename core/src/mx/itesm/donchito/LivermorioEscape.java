package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

import static mx.itesm.donchito.LoadingScreen.ScreenSel.CUEVA;
import static mx.itesm.donchito.LoadingScreen.ScreenSel.LIVERMORIO;
import static mx.itesm.donchito.LoadingScreen.ScreenSel.MENU;


public class LivermorioEscape implements Screen {
    private static final float DEATH_MOVE_SPEED = 200;
    public static final int PLATFORM_WIDTH = 3512;
    private static final int BACKGROUND_SIZE = PLATFORM_WIDTH;
    private final int[] CARRETAS_X = new int[]{114, 600, 1460, 1952, 2837, 3024, 3429, 3994, 4706, 5107, 5800, 5952, 7018, 7016, 7412, 8560, 8960, 9336, 8861, 9528, 9685, 10036, 10394};
    private final int[] CARRETAS_Y = new int[]{283, 90, 254, 12, 18, 419, 17, 173, 293, 294, 10, 486, 3, 2, 2, 6, 18, 17, 474, 478, 172, 314, 452};

    private final int[] MADERAS_X = new int[]{810, 1217, 1965, 2206, 3600, 4029, 4460, 5064, 6414, 6339, 6643, 7119, 7613, 7963, 8219, 11001, 10922, 11404};
    private final int[] MADERAS_Y = new int[]{431, 42, 569, 281, 523, 520, 20, 129, 555, 192, 402, 412, 598, 172, 479, 516, 54, 71};

    private OrthographicCamera camera, cameraHUD;
    private final DonChito game;
    private Viewport view;

    //TODO Refactor to interface
    private SimpleAsset fondo,
            fondo2,
            fondoPausa,
            botonPausa,
            botonPlay,
            botonSalirMenu,
            botonConfiguracion,
            arrowLeft,
            arrowRight,
            arrowUp,
            powerUpAs,
            btnBacktoCave;
    private SpriteBatch batch;

    DonChitoLivermorio player;
    private int leftPointer;

    private float deathVelocity = 1;
    private Vector2 DeathPosition;
    private Animation animationDeath;
    private float deathStartTime;
    private int nFondos = 2, posFondos = BACKGROUND_SIZE * 2;
    private TextureRegion regionDeath;


    Array<SimpleAsset> platformsCarretas;
    Array<SimpleAsset> platformsMadera;
    private int posPlatformsCarretas = 9;
    private int posPlatformsMadera = 9;
    private boolean powerUp = false;
    private boolean isAvailable = false;

    private PlayerState playerState = PlayerState.NOTDEAD;
    private GameState gameState = GameState.PLAY;
    private MoveState moveState = MoveState.NONE;

    private Music music;


    private int randomAs;
    private SimpleAsset temp;
    private Random random;

    public LivermorioEscape(DonChito game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.input.setCatchBackKey(true);
        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO, DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();

        cameraHUD = new OrthographicCamera(DonChito.ANCHO_MUNDO, DonChito.ALTO_MUNDO);
        cameraHUD.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        cameraHUD.update();
        random = new Random();
        view = new FitViewport(DonChito.ANCHO_MUNDO, DonChito.ALTO_MUNDO, camera);
        batch = new SpriteBatch();
        platformsCarretas = new Array<SimpleAsset>();
        platformsMadera = new Array<SimpleAsset>();
        player = new DonChitoLivermorio(300, 600);
        cargaPosiciones();
        cargarAudio();
        cargarRecursos();
        leerEntrada();
        crearElementos();
        createPlatforms();
    }

    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        view.apply();
        checkCollisions();
        updateCamera();
        renderCamera();
        checkPlatforms();
        if (playerState == PlayerState.NOTDEAD) {
            if (gameState == GameState.PLAY) {
                updateCamera();
                update(delta);
                realInput();
                if ((posFondos - 1280) < player.getX()) {
                    switch ((nFondos + 1) % 2) {
                        case 1:
                            fondo.setPosition(fondo2.getSprite().getX() + fondo2.getSprite().getWidth(), 0);
                            break;
                        case 0:
                            fondo2.setPosition(fondo.getSprite().getX() + fondo.getSprite().getWidth(), 0);
                            break;
                    }
                    nFondos++;
                    posFondos += PLATFORM_WIDTH;
                }
            }
            renderHUD();
        } else {
            renderDeath();
        }
    }

    private void checkPlatforms() {
        if (platformsCarretas.get(0).getSprite().getX() + platformsCarretas.get(0).getSprite().getWidth() < player.getX() - 1000) {
            platformsCarretas.removeIndex(0);
            randomAs = random.nextInt(Constants.PLATFORMS_CARRETAS.length);
            temp = new SimpleAsset(Constants.PLATFORMS_CARRETAS[randomAs], CARRETAS_X[posPlatformsCarretas], CARRETAS_Y[posPlatformsCarretas]);
            if (!isAvailable && posPlatformsCarretas > 15) {
                if (15 >= random.nextInt(CARRETAS_X.length)) {
                    isAvailable = true;
                    powerUpAs.setPosition(CARRETAS_X[posPlatformsCarretas] + temp.getSprite().getWidth() / 2, MADERAS_Y[posPlatformsCarretas] + temp.getSprite().getHeight());
                }
            }
            posPlatformsCarretas++;
            posPlatformsCarretas = posPlatformsCarretas % CARRETAS_X.length;
            platformsCarretas.add(temp);
        }
        if (platformsMadera.get(0).getSprite().getX() < player.getX() - 1280) {
            platformsMadera.removeIndex(0);
            randomAs = random.nextInt(Constants.PLATFORMS_MADERA.length);
            temp = new SimpleAsset(Constants.PLATFORMS_MADERA[randomAs], MADERAS_X[posPlatformsMadera], MADERAS_Y[posPlatformsMadera]);
            if (!isAvailable && posPlatformsCarretas > 15) {
                if (15 >= random.nextInt(MADERAS_X.length)) {
                    isAvailable = true;
                    powerUpAs.setPosition(MADERAS_X[posPlatformsMadera] + temp.getSprite().getWidth() / 2, MADERAS_Y[posPlatformsMadera] + temp.getSprite().getHeight());
                }
            }
            posPlatformsMadera++;
            posPlatformsMadera = posPlatformsMadera % MADERAS_X.length;
            platformsMadera.add(temp);
        }
    }

    private void crearElementos() {
        fondoPausa = new SimpleAsset(Constants.GLOBAL_MENU_PAUSA_PNG, 0, 0);
        botonPlay = new SimpleAsset(Constants.GLOBAL_BOTON_PLAY_PNG, 1110, 530);
        botonConfiguracion = new SimpleAsset(Constants.GLOBAL_BOTON_CONFIGURACION_PNG, 405, 175);
        botonConfiguracion.getSprite().setColor(.1f, .1f, .1f, .5f);
        botonSalirMenu = new SimpleAsset(Constants.GLOBAL_BOTON_SALIRMENU_PNG, 405, 425);
        botonPausa = new SimpleAsset(Constants.GLOBAL_BOTON_PAUSA_PNG, 1110, 530);
        botonPausa.setAlpha(0.5f);
        botonPlay.setAlpha(0.5f);
        fondo = new SimpleAsset(Constants.LIVERMORIO_FONDO_PNG, 0, 0);
        fondo2 = new SimpleAsset(Constants.LIVERMORIO_FONDO_PNG, posFondos / 2, 0);
        powerUpAs = new SimpleAsset(Constants.LIVERMORIO_ITEM, -1233, 23);
        powerUpAs.getSprite().setScale(.50f);
        btnBacktoCave = new SimpleAsset(Constants.GLOBAL_BOTON_BACK_CAVE, 705, 195);

        //Change locations,when asset is available
        arrowUp = new SimpleAsset(Constants.CUEVA_ARROW_LEFT, 1110, 50);
        arrowUp.setRotation(-90);
        arrowRight = new SimpleAsset(Constants.CUEVA_ARROW_LEFT, 200, 50);
        arrowRight.setRotation(-180);
        arrowLeft = new SimpleAsset(Constants.CUEVA_ARROW_LEFT, 10, 50);

    }

    private void cargaPosiciones() {

    }

    private void createPlatforms() {
        batch.setProjectionMatrix(camera.projection);
        batch.begin();
        SimpleAsset temp;
        int randomAs;
        for (int i = 0; i < 9; i++) {
            randomAs = (new Random()).nextInt(Constants.PLATFORMS_CARRETAS.length);
            temp = new SimpleAsset(Constants.PLATFORMS_CARRETAS[randomAs], CARRETAS_X[i], CARRETAS_Y[i]);
            platformsCarretas.add(temp);
        }
        for (int i = 0; i < 9; i++) {
            randomAs = (new Random()).nextInt(Constants.PLATFORMS_MADERA.length);
            temp = new SimpleAsset(Constants.PLATFORMS_MADERA[randomAs], MADERAS_X[i], MADERAS_Y[i]);
            platformsMadera.add(temp);
        }
        batch.end();
    }

    private void cargarRecursos() {

        TextureRegion texturaCompleta = new TextureRegion(new Texture(Constants.DEATHBYROBOT));
        TextureRegion[][] texturaDeath = texturaCompleta.split(1280, 720);
        animationDeath = new Animation(.15f, texturaDeath[0][0],
                texturaDeath[1][0], texturaDeath[2][0], texturaDeath[3][0]);
        animationDeath.setPlayMode(Animation.PlayMode.LOOP);
        DeathPosition = new Vector2(-1270, 0);
        deathStartTime = TimeUtils.nanoTime();
        regionDeath = animationDeath.getKeyFrame(deathStartTime);

    }

    private void leerEntrada() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchUp(int x, int y, int pointer, int button) {
                if (playerState == PlayerState.NOTDEAD) {
                    if (gameState == GameState.PAUSE) {
                        if (botonSalirMenu.isTouched(x, y, cameraHUD, view)) {
                            dispose();
                            game.setScreen(new LoadingScreen(MENU, game));
                        }
                        if (botonPlay.isTouched(x, y, cameraHUD, view)) {
                            gameState = GameState.PLAY;
                        }
                        if (btnBacktoCave.isTouched(x, y, cameraHUD, view)) {
                            dispose();
                            game.setScreen(new LoadingScreen(CUEVA, game));
                        }
                    } else {
                        if (botonPausa.isTouched(x, y, cameraHUD, view)) {
                            gameState = GameState.PAUSE;
                            moveState = MoveState.NONE;
                            player.stand();
                            leftPointer = -1;
                        }
                        if (leftPointer == pointer) {
                            moveState = MoveState.NONE;
                        }
                    }
                } else {
                    dispose();
                    game.setScreen(new LoadingScreen(MENU, game));
                }
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    dispose();
                    game.setScreen(new LoadingScreen(CUEVA, game));
                }
                return super.keyUp(keycode);
            }

            public boolean touchDown(int x, int y, int pointer, int button) {
                if (gameState == GameState.PLAY) {
                    if (arrowLeft.isTouched(x, y, cameraHUD, view)) {
                        moveState = MoveState.LEFT;
                        leftPointer = pointer;
                    } else if (arrowRight.isTouched(x, y, cameraHUD, view)) {
                        moveState = MoveState.RIGHT;
                        leftPointer = pointer;
                    }
                    if (arrowUp.isTouched(x, y, cameraHUD, view)) {
                        switch (player.getJumpState()) {
                            case GROUND:
                                player.startJump();
                                break;
                            case JUMPING:
                                player.continueJump();
                        }
                    } else {
                        player.endJump();
                    }
                }
                return true;
            }

            @Override
            public boolean touchDragged(int x, int y, int pointer) {
                if (leftPointer == pointer) {
                    if (player.getMoveState() == DonChitoLivermorio.WalkState.WALKING) {
                        if (!arrowLeft.isTouched(x, y, cameraHUD, view) && !arrowRight.isTouched(x, y, cameraHUD, view)) {
                            moveState = MoveState.NONE;
                        }
                    }
                    if (player.getMoveState() == DonChitoLivermorio.WalkState.STANDING) {
                        if (arrowLeft.isTouched(x, y, cameraHUD, view)) {
                            moveState = MoveState.LEFT;
                            leftPointer = pointer;
                        } else if (arrowRight.isTouched(x, y, cameraHUD, view)) {
                            moveState = MoveState.RIGHT;
                            leftPointer = pointer;
                        }
                    }
                }
                return true;
            }

        });
    }

    private void cargarAudio() {

        if (DonChito.preferences.getBoolean(Constants.MENUPRINCIPAL_SOUND_PREF, true)) {
            music = DonChito.assetManager.get(Constants.LIVERMORIO_MUSIC);
            music.setLooping(true);
            music.play();
        } else {
            if (music != null && music.isPlaying()) {
                music.stop();
            }
        }
    }


    private void realInput() {
        float delta = Gdx.graphics.getDeltaTime();

        if (gameState == GameState.PLAY) {
            if (moveState == MoveState.LEFT) {
                player.moveLeft(delta);
            } else if (moveState == MoveState.RIGHT) {
                player.moveRight(delta);
            } else {
                player.stand();
            }
        }
        /*
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.moveLeft(delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.moveRight(delta);
        } else {
            player.stand();
        }
        */
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            switch (player.getJumpState()) {
                case GROUND:
                    player.startJump();
                    break;
                case JUMPING:
                    player.continueJump();
            }
        } else {
            player.endJump();
        }
    }


    public void checkCollisions() {
        if (DeathPosition.x + regionDeath.getRegionWidth() - 200 > player.getX()) {
            playerState = PlayerState.DEAD;
        }
        if (powerUpAs.getSprite().getBoundingRectangle().overlaps(new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight()))) {
            powerUp = true;
            DonChito.preferences.putBoolean(Constants.PREF_LIVERMORIO, true);
            DonChito.preferences.flush();
            dispose();
            if (DonChito.preferences.getBoolean(Constants.PREF_LIVERMORIO, false)) {
                game.setScreen(new LoadingScreen(CUEVA, game, true, LIVERMORIO));
            } else {
                game.setScreen(new LoadingScreen(CUEVA, game));
            }

        }
        if (player.getY() + player.getHeight() / 2 < 0) {
            dispose();
            game.setScreen(new LoadingScreen(CUEVA, game, false, LIVERMORIO));
        }
    }

    public void renderDeath() {
        dispose();
        game.setScreen(new LoadingScreen(CUEVA, game, false, LIVERMORIO));
    }

    public void renderCamera() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        fondo.render(batch);
        fondo2.render(batch);
        for (SimpleAsset platform : platformsCarretas) {
            platform.render(batch);
        }
        for (SimpleAsset platform : platformsMadera) {
            platform.render(batch);
        }
        if (!powerUp) powerUpAs.render(batch);
        player.render(batch);
        batch.draw(regionDeath, DeathPosition.x, DeathPosition.y);
        batch.end();
    }

    public void renderHUD() {
        batch.setProjectionMatrix(cameraHUD.combined);
        batch.begin();
        if (gameState == GameState.PAUSE) {
            fondoPausa.render(batch);
            botonPlay.render(batch);
            botonConfiguracion.render(batch);
            botonSalirMenu.render(batch);
            btnBacktoCave.render(batch);
        } else {
            botonPausa.render(batch);
            arrowRight.render(batch);
            arrowLeft.render(batch);
            arrowUp.render(batch);
        }
        batch.end();
    }

    public void update(float delta) {
        player.update(delta, platformsCarretas, platformsMadera, gameState);
        regionDeath = animationDeath.getKeyFrame(MathUtils.nanoToSec * (TimeUtils.nanoTime() - deathStartTime));
        deathVelocity += delta * .025;
        DeathPosition.x += delta * DEATH_MOVE_SPEED * deathVelocity;
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

    private void updateCamera() {
        float posX = player.getX();
        if (posX >= DonChito.ANCHO_MUNDO / 2) {
            camera.position.set((int) posX, camera.position.y, 0);
        } else if (posX < DonChito.ANCHO_MUNDO - DonChito.ANCHO_MUNDO / 2) {
            camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        }
        camera.update();
    }

    enum PlayerState {
        DEAD,
        NOTDEAD
    }

    enum GameState {
        PLAY,
        PAUSE
    }

    enum MoveState {
        LEFT,
        RIGHT,
        NONE
    }
}
