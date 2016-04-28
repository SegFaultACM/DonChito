package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static mx.itesm.donchito.LoadingScreen.ScreenSel.CUEVA;
import static mx.itesm.donchito.LoadingScreen.ScreenSel.ROMANSTRUGGLE;

public class RomanStruggle implements Screen {

    private OrthographicCamera camera;
    private final DonChito game;
    private Viewport view;

    private int nivel = 1;
    private int indiceRocas = 0;

    private int puntos = 0;

    private float velocidadBala = 5f;
    private float tiempoEsperar = 5f;

    private SimpleAsset botonIzquierda;
    private SimpleAsset botonDerecha;
    private SimpleAsset botonDisparo;
    private SimpleAsset botonPausa;
    private SimpleAsset botonPlay;
    private SimpleAsset botonSalirMenu;
    private SimpleAsset botonConfiguracion;
    private SimpleAsset botonSalirCueva;
    private SimpleAsset proyectil;

    private DonChitoLivermorio player;
    private MoveState moveState;
    private int leftPointer;
    private GameText levelTxt;
    private GameText pointsTxt;


    private SimpleAsset fondoPantalla;
    private SimpleAsset fondoPausa;

    private DelayedRemovalArray<RomanRock> rocas;
    RomanRock rocaP;

    private boolean disparado = false;


    private State estado = State.PLAY;

    private SpriteBatch batch;

    private Music efectoRomper;
    private Music efectoGanar;
    private Music efectoPerder;
    private Music musicaFondo;


    public RomanStruggle(DonChito game) {
        this.game = game;
    }
    private void cargarRecursos() {
        AssetManager assetManager = DonChito.assetManager;

        musicaFondo = assetManager.get(Constants.ROMAN_MUSICA_FONDO_MP3);
        musicaFondo.setLooping(true);
        reproducirMusica(musicaFondo);
        efectoRomper = assetManager.get(Constants.ROMAN_EFECTO_ROCA);
        efectoGanar = assetManager.get(Constants.FLEVORIO_SONIDOVICTORY_WAV);
        efectoPerder = assetManager.get(Constants.FLEVORIO_SONIDOFAIL_WAV);

    }
    @Override
    public void show() {
        if(!(DonChito.preferences.getBoolean(Constants.PREF_ROMAN_STRUGGLE,false))){
            DonChito.preferences.putBoolean(Constants.PREF_ROMAN_STRUGGLE,false);
            DonChito.preferences.flush();
        }
        player = new DonChitoLivermorio(640f,70f);
        player.setJumpState();
        moveState = MoveState.NONE;
        cargarRecursos();
        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();
        view = new FitViewport(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO,camera);
        view.apply();
        leerEntrada();

        rocas = new DelayedRemovalArray<RomanRock>();
        batch = new SpriteBatch();
        createFirstRocks(nivel);

        fondoPantalla = new SimpleAsset(Constants.ROMAN_FONDO,0,0);
        botonIzquierda = new SimpleAsset(Constants.ROMAN_BOTON_IZQUIERDA,20,50);
        botonDerecha = new SimpleAsset(Constants.ROMAN_BOTON_IZQUIERDA,270,50);
        botonDisparo = new SimpleAsset(Constants.ROMAN_BOTON_IZQUIERDA,1100,50);

        botonDerecha.setRotation(180);
        botonDisparo.setRotation(90);

        botonIzquierda.setAlpha(0.5f);
        botonDerecha.setAlpha(0.5f);
        botonDisparo.setAlpha(0.5f);

        fondoPausa = new SimpleAsset(Constants.GLOBAL_MENU_PAUSA_PNG,0,0);
        botonPlay = new SimpleAsset(Constants.GLOBAL_BOTON_PLAY_PNG,1050,500);
        botonConfiguracion = new SimpleAsset(Constants.GLOBAL_BOTON_CONFIGURACION_PNG,405,175);
        botonSalirMenu = new SimpleAsset(Constants.GLOBAL_BOTON_SALIRMENU_PNG,425,425);
        botonSalirCueva = new SimpleAsset(Constants.GLOBAL_BOTON_BACK_CAVE,685,195);
        botonPlay.setAlpha(0.5f);

        botonPausa = new SimpleAsset(Constants.GLOBAL_BOTON_PAUSA_PNG,1050,500);
        botonPausa.setAlpha(0.5f);

        levelTxt = new GameText(150,700);
        pointsTxt = new GameText(900,700);

        proyectil = new SimpleAsset(Constants.ROMAN_PIEDRA,-1000f,-0912384f);
        proyectil.getSprite().setColor(Color.BLACK);
        proyectil.getSprite().setScale(0.3f);
    }

    private void createFirstRocks(int nivel) {
        for(int i=0;i<nivel;i++) {
            rocaP = new RomanRock(Constants.ROMAN_PIEDRA, (float)(Math.random()*700), 700, (int)(Math.random()*2), 1, 1, 0);
            rocas.add(rocaP);
        }
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            dispose();
            game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.MENU, game));
        }
        camera.update();
        view.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if(rocas.size == 0){
            if(nivel!= 0){
                efectoGanar.play();
            }
            tiempoEsperar = 5f;
            nivel++;
            if(nivel == 2){
                DonChito.preferences.putBoolean(Constants.PREF_ROMAN_STRUGGLE,true);
                DonChito.preferences.flush();
                dispose();
                game.setScreen(new LoadingScreen(CUEVA,game,true,ROMANSTRUGGLE));
            }
            createFirstRocks(nivel);
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        fondoPantalla.render(batch);
        levelTxt.showMessage(batch, "Level: " + nivel);
        pointsTxt.showMessage(batch,"Points: "+puntos);
        ejecutarInputs();

        if(player.getX()>=1130){
            player.position = new Vector2(1130,player.getY());
        }
        if(player.getX()<=42){
            player.position = new Vector2(42,player.getY());
        }
        player.render(batch);
        botonDerecha.render(batch);
        botonIzquierda.render(batch);
        botonDisparo.render(batch);
        view.apply();

        if(estado == State.PAUSA){
            fondoPausa.render(batch);
            botonPlay.render(batch);
            botonConfiguracion.render(batch);
            botonSalirMenu.render(batch);
            botonSalirCueva.render(batch);
        }
        else if(estado == State.DEATH) {
            efectoPerder.play();
        }
        else{
            botonPausa.render(batch);
            if(esperar(delta)) {
                for (RomanRock roca : rocas) {
                    roca.updateRock();
                    roca.render(batch);
                    revisarColisiones(roca);
                    indiceRocas++;
                }
            }
            indiceRocas = 0;
            if (disparado) {
                if (proyectil.getSprite().getY() > 700) {
                    disparado = false;
                } else {
                    proyectil.setPosition(proyectil.getSprite().getX(), proyectil.getSprite().getY() + velocidadBala);
                    proyectil.render(batch);
                }
            }

        }
        batch.end();
    }
    private boolean esperar(float delta){
        if(tiempoEsperar <=0){
            return true;
        }
        tiempoEsperar -= delta;
        return false;
    }

    private void revisarColisiones(RomanRock roca) {
        float rocaX = roca.getSprite().getX();
        float rocaY = roca.getSprite().getY();

        if(rocaX+roca.getRockWidth()-(50*roca.getEscala())>player.getX()&& rocaX-roca.getRockWidth()+(50*roca.getEscala())<player.getX() && rocaY<=player.getY()){
            estado = State.DEATH;
        }

        if(disparado) {
            if (rocaX+roca.getRockWidth()>proyectil.getSprite().getX()&& rocaX-roca.getRockWidth()<proyectil.getSprite().getX()&&rocaY+roca.getRockHeight()<proyectil.getSprite().getY()+proyectil.getSprite().getHeight()){
                proyectil.setPosition(1000,1000);
                disparado = false;
                reproducirMusica(efectoRomper);
                if(roca.getEscala()/2 >= 0.25) {
                    RomanRock nuevo = new RomanRock(Constants.ROMAN_PIEDRA,roca.getSprite().getX(), roca.getSprite().getY(), 1, 0, roca.getEscala()/2,roca.getVelocidad());
                    rocas.add(nuevo);
                    nuevo = new RomanRock(Constants.ROMAN_PIEDRA, roca.getSprite().getX(), roca.getSprite().getY(), 0, 0, roca.getEscala()/2,roca.getVelocidad());
                    rocas.add(nuevo);
                }
                rocas.removeIndex(indiceRocas);
                puntos += 10/roca.getEscala();
            }
        }
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
        stopMusic();
        DonChito.assetManager.clear();
    }

    private void leerEntrada() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchUp(int x, int y, int pointer, int button) {
                if (estado == State.PLAY) {
                    if(leftPointer == pointer){
                        moveState = MoveState.NONE;
                    }
                }
                if(botonPlay.isTouched(x,y,camera,view) || botonPausa.isTouched(x,y,camera,view)){
                    if(estado == State.PLAY){
                        estado = State.PAUSA;
                        player.stand();
                        leftPointer = -1;
                    }
                    else{
                        estado = State.PLAY;
                    }
                }
                if (botonSalirMenu.isTouched(x,y,camera,view) && estado == State.PAUSA) {
                    dispose();
                    game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.MENU,game));
                }
                if (botonSalirCueva.isTouched(x,y,camera,view) && estado == State.PAUSA) {
                    dispose();
                    game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.CUEVA,game));
                }
                return true;
            }

            @Override
            public boolean touchDragged(int x, int y, int pointer) {
                if(leftPointer == pointer){
                    if(player.getMoveState() == DonChitoLivermorio.WalkState.WALKING){
                        if (!botonIzquierda.isTouched(x, y,camera,view) && !botonDerecha.isTouched(x, y,camera,view) ) {
                            moveState = MoveState.NONE;
                        }
                    }
                    if(player.getMoveState() == DonChitoLivermorio.WalkState.STANDING){
                        if(botonIzquierda.isTouched(x,y,camera,view)){
                            moveState = MoveState.LEFT;
                            leftPointer = pointer;
                        }else if(botonDerecha.isTouched(x,y,camera,view)){
                            moveState = MoveState.RIGHT;
                            leftPointer = pointer;
                        }
                    }
                }
            return true;
            }

            public boolean touchDown(int x, int y, int pointer, int button) {
                if (estado != State.PAUSA) {
                    if(botonIzquierda.isTouched(x,y,camera,view)){
                        moveState = MoveState.LEFT;
                        leftPointer = pointer;
                    }else if(botonDerecha.isTouched(x,y,camera,view)){
                        moveState = MoveState.RIGHT;
                        leftPointer = pointer;
                    }
                    if(botonDisparo.isTouched(x,y,camera,view)){
                        if(!disparado){
                            batch.begin();
                            proyectil.setPosition(player.getX(),player.getY()+30);
                            proyectil.render(batch);
                            disparado = true;
                            batch.end();
                        }
                    }
                }

                return true;
            }
        });
    }
    private void stopMusic(){
        if(efectoRomper.isPlaying()){
            efectoRomper.stop();
        }
        if(efectoPerder.isPlaying()){
            efectoPerder.stop();
        }
        if(efectoGanar.isPlaying()){
            efectoGanar.stop();
        }
        if(musicaFondo.isPlaying()){
            musicaFondo.stop();
        }
    }
    private void reproducirMusica(Music musica){
        musica.setVolume(1F);
        if(!DonChito.preferences.getBoolean(Constants.MENUPRINCIPAL_SOUND_PREF,true)){
            musica.setVolume(0f);
        }
        musica.play();
    }
    public enum State
    {
        PAUSA,
        PLAY,
        DEATH
    }
    public enum MoveState {
        LEFT,
        RIGHT,
        NONE
    }
    private void ejecutarInputs(){
        if(estado == State.DEATH){
            dispose();
            game.setScreen(new LoadingScreen(CUEVA,game,false,ROMANSTRUGGLE));
            return;
        }
        float delta = Gdx.graphics.getDeltaTime();
        if(moveState == MoveState.LEFT){
            player.moveLeft(delta);
        }else if(moveState == MoveState.RIGHT){
            player.moveRight(delta);
        }else{
            player.stand();
        }

    }
}