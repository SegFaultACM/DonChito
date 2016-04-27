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
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.IntFloatMap;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class RomanStruggle implements Screen {
    //Ver transparencia en botones, que don chito pasa adelante de ellos
    private OrthographicCamera camera;
    private final DonChito game;
    private Viewport view;

    private int nivel = 1;
    private int botonPresionado = 0;
    private int indiceRocas = 0;

    private int puntos = 0;

    private float velocidad = 5f;
    private float velocidadBala = 5f;
    private float tiempoEsperar = 5f;

    private SimpleAsset botonIzquierda;
    private SimpleAsset botonDerecha;
    private SimpleAsset botonDisparo;
    private SimpleAsset botonPausa;
    private SimpleAsset botonPlay;
    private SimpleAsset botonSalirMenu;
    private SimpleAsset botonConfiguracion;
    private SimpleAsset proyectil;

    private SimpleAsset fondoDeath;

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
    private boolean pasarNivel = true;


    private State estado = State.PLAY;

    private SpriteBatch batch;

    private Music efectoBoton;
    private Music efectoGanar;
    private Music efectoPerder;
    private Music musicaFondo;
    private Music musicaIntro;

    private float widthProyectil;
    private float heigthProyectil;


    public RomanStruggle(DonChito game) {
        this.game = game;
    }
    private void cargarRecursos() {
        AssetManager assetManager = DonChito.assetManager;

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

        //musicaFondo.setLooping(true);
        //musicaIntro.play();
        rocas = new DelayedRemovalArray<RomanRock>();
        batch = new SpriteBatch();
        createFirstRocks(nivel);

        fondoPantalla = new SimpleAsset(Constants.ROMAN_FONDO,0,0);
        botonIzquierda = new SimpleAsset(Constants.ROMAN_BOTON_IZQUIERDA,20,50);
        botonDerecha = new SimpleAsset(Constants.ROMAN_BOTON_DERECHA,270,50);
        botonDisparo = new SimpleAsset(Constants.ROMAN_BOTON_DISPARA,1100,50);
        botonIzquierda.setAlpha(0.5f);
        botonDerecha.setAlpha(0.5f);
        botonDisparo.setAlpha(0.5f);
        //botonIzquierda.getSprite().setScale(1.2f);
        //botonDerecha.getSprite().setScale(1.2f);
        //botonDisparo.getSprite().setScale(1.2f);

        fondoPausa = new SimpleAsset(Constants.GLOBAL_MENU_PAUSA_PNG,0,0);
        botonPlay = new SimpleAsset(Constants.GLOBAL_BOTON_PLAY_PNG,1050,500);
        botonConfiguracion = new SimpleAsset(Constants.GLOBAL_BOTON_CONFIGURACION_PNG,405,175);
        botonSalirMenu = new SimpleAsset(Constants.GLOBAL_BOTON_SALIRMENU_PNG,405,425);
        botonPlay.setAlpha(0.5f);

        botonPausa = new SimpleAsset(Constants.GLOBAL_BOTON_PAUSA_PNG,1050,500);
        botonPausa.setAlpha(0.5f);

        levelTxt = new GameText(150,700);
        pointsTxt = new GameText(900,700);

        fondoDeath = new SimpleAsset(Constants.CTHULHU,0,0);

        proyectil = new SimpleAsset(Constants.ROMAN_PIEDRA,-1000f,-0912384f);
        proyectil.getSprite().setColor(Color.BLACK);
        proyectil.getSprite().setScale(0.3f);
        heigthProyectil = proyectil.getSprite().getHeight()/0.3f;
        widthProyectil = proyectil.getSprite().getWidth()/0.3f;
    }

    private void createFirstRocks(int nivel) {
        for(int i=0;i<nivel;i++) {
            rocaP = new RomanRock(Constants.ROMAN_PIEDRA, (float)(Math.random()*700), 700, (int)(Math.random()*2), 1, 1, 0);
            rocas.add(rocaP);
        }
    }

    @Override
    public void render(float delta) {
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
            if(nivel == 4){
                DonChito.preferences.putBoolean(Constants.PREF_ROMAN_STRUGGLE,true);
                DonChito.preferences.flush();
                dispose();
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.CUEVA,game));
            }
            createFirstRocks(nivel);
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        fondoPantalla.render(batch);
        levelTxt.showMessage(batch, "Level: " + nivel);
        pointsTxt.showMessage(batch,"Points: "+puntos);
        ejecutarInputs();

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
        }
        else if(estado == State.DEATH) {
            fondoDeath.render(batch);
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

        //if(roca.getSprite().getY() <100){
            if(rocaX+roca.getRockWidth()-50>player.getX()&& rocaX-roca.getRockWidth()+50<player.getX()){
                if(rocaY<=player.getY()) {
                    estado = State.DEATH;
                    //Gdx.app.log("Colisiono en ","y");
                }
            }
        //}
        if(disparado) {
            if (rocaX+roca.getRockWidth()>proyectil.getSprite().getX()&& rocaX-roca.getRockWidth()<proyectil.getSprite().getX()&&rocaY+roca.getRockHeight()<proyectil.getSprite().getY()+proyectil.getSprite().getHeight()){
                proyectil.setPosition(1000,1000);
                disparado = false;
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
        DonChito.assetManager.clear();
    }

    private void leerEntrada() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchUp(int x, int y, int pointer, int button) {
                if (estado == State.PLAY) {
                    if(leftPointer == pointer){
                        moveState = MoveState.NONE;
                    }
                }else{

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
                    //init();
                    //musicaFondo.setLooping(false);
                    //if (musicaFondo.isPlaying()) {
                    //    musicaFondo.stop();
                    //}
                    dispose();
                    game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.MENU,game));
                }
                return true; // return true to indicate the event was handled
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

                return true; // return true to indicate the event was handled
            }
        });
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
            game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.MENU,game));
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