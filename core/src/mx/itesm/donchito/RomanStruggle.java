package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
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

    //TODO FALTA HACERLO PERSONAJE
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
    private State estadoBoton = State.NOPRESIONADO;

    private SpriteBatch batch;

    private Music efectoBoton;
    private Music efectoGanar;
    private Music efectoPerder;
    private Music musicaFondo;
    private Music musicaIntro;


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
        if(!(DonChito.preferences.getBoolean("RomanStruggle",false))){
            DonChito.preferences.putBoolean("RomanStruggle",false);
            DonChito.preferences.flush();
        }
        player = new DonChitoLivermorio(640f,10f);
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
        botonIzquierda = new SimpleAsset(Constants.ROMAN_BOTON_IZQUIERDA,20,10);
        botonDerecha = new SimpleAsset(Constants.ROMAN_BOTON_DERECHA,170,10);
        botonDisparo = new SimpleAsset(Constants.ROMAN_BOTON_DISPARA,900,10);

        fondoPausa = new SimpleAsset(Constants.GLOBAL_MENU_PAUSA_PNG,0,0);
        botonPlay = new SimpleAsset(Constants.GLOBAL_BOTON_PLAY_PNG,1050,10);
        botonConfiguracion = new SimpleAsset(Constants.GLOBAL_BOTON_CONFIGURACION_PNG,405,175);
        botonSalirMenu = new SimpleAsset(Constants.GLOBAL_BOTON_SALIRMENU_PNG,405,425);
        botonPlay.setAlpha(0.5f);

        botonPausa = new SimpleAsset(Constants.GLOBAL_BOTON_PAUSA_PNG,1050,10);
        botonPausa.setAlpha(0.5f);

        levelTxt = new GameText(150,700);
        pointsTxt = new GameText(1000,700);

        fondoDeath = new SimpleAsset(Constants.CTHULHU,0,0);


        //FALTA HACER A DON CHITO COMO UN PERSONAJE
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
            if(nivel == 2){
                DonChito.preferences.putBoolean("RomanStruggle",true);
                DonChito.preferences.flush();
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
                if (proyectil.getSprite().getY() > 600) {
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

        //if(roca.verificarColision(donChito)){
        if(roca.getSprite().getY() <100){
            if(rocaX+roca.getRockWidth()>player.getX()&& rocaX-roca.getRockWidth()<player.getX()){
                if(rocaY<=player.getY()+player.getHeight()) {
                    estado = State.DEATH;
                    estadoBoton = State.NOPRESIONADO;
                }
            }
        }
        //revisar con bala
        if(disparado) {
            //if(roca.verificarColision(proyectil)){
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
                estadoBoton = State.NOPRESIONADO;
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
                estadoBoton = State.PRESIONADO;
                if (estado == State.PAUSA) {
                    if(botonIzquierda.isTouched(x,y,camera,view)){
                        moveState = MoveState.LEFT;
                        leftPointer = pointer;
                    }else if(botonDerecha.isTouched(x,y,camera,view)){
                        moveState = MoveState.RIGHT;
                        leftPointer = pointer;
                    }
                    if(botonDisparo.isTouched(x,y,camera,view)){
                        if(!disparado){
                            proyectil.setPosition(player.getX(),player.getY()+30);
                            proyectil.getSprite().setScale(0.5f);
                            proyectil.render(batch);
                            disparado = true;
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
        PRESIONADO,
        NOPRESIONADO,
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
        }
    }
}