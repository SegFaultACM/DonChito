package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.DelayedRemovalArray;
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

    //FALTA HACERLO PERSONAJE
    private SimpleAsset donChito;

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

    @Override
    public void show() {
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
        donChito = new SimpleAsset(Constants.ROMAN_PERSONAJE_DONCHITO,550,10);
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
            //Gdx.app.log("SUBIR LVL","ALGO ASI");
            //HACER UN ESTILO DE SLEEP
            tiempoEsperar = 5f;
            nivel++;
            if(nivel == 4){
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.CUEVA,game));
            }
            createFirstRocks(nivel);
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        fondoPantalla.render(batch);
        levelTxt.showMessage(batch, "Level: " + nivel);
        pointsTxt.showMessage(batch,"Points: "+puntos);
        ejecutarInputs();

        donChito.render(batch);
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

        //revisar con personaje
        if(roca.getSprite().getY() <100){
            if(rocaX+roca.getRockWidth()>donChito.getSprite().getX()&& rocaX-roca.getRockWidth()<donChito.getSprite().getX()){
                if(rocaY<=donChito.getSprite().getY()+donChito.getSprite().getHeight()) {
                    estado = State.DEATH;
                    estadoBoton = State.NOPRESIONADO;
                }
            }
        }
        //revisar con bala
        if(disparado) {
            if (rocaX> proyectil.getSprite().getX()-proyectil.getSprite().getWidth()&&rocaX< proyectil.getSprite().getX()+proyectil.getSprite().getWidth()&&rocaY<proyectil.getSprite().getY()+proyectil.getSprite().getHeight()){
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
                return true; // return true to indicate the event was handled
            }
            public boolean touchDown(int x, int y, int pointexr, int button) {
                estadoBoton = State.PRESIONADO;
                if (estado == State.PAUSA) {
                    if (botonSalirMenu.isTouched(x,y,camera,view)) {
                        //init();
                        //musicaFondo.setLooping(false);
                        //if (musicaFondo.isPlaying()) {
                        //    musicaFondo.stop();
                        //}
                        game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.MENU,game));
                    }
                }
                if(botonPlay.isTouched(x,y,camera,view) || botonPausa.isTouched(x,y,camera,view)){
                    if(estado == State.PLAY){
                        estado = State.PAUSA;
                    }
                    else{
                        estado = State.PLAY;
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
    private void ejecutarInputs(){
        if(estadoBoton == State.PRESIONADO){
            if(estado == State.DEATH){
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.MENU,game));
                return;
            }
            int x = Gdx.app.getInput().getX();
            int y = Gdx.app.getInput().getY();
            if(botonIzquierda.isTouched(x,y,camera,view)){
                botonPresionado = 1;
            }
            else if(botonDerecha.isTouched(x,y,camera,view)){
                botonPresionado = 2;
            }
            else if(botonDisparo.isTouched(x,y,camera,view)){
                botonPresionado = 3;
            }
            else {
                botonPresionado = 0;
            }
            switch(botonPresionado){
                case 1:
                    if(donChito.getSprite().getX()>20){
                        donChito.setPosition(donChito.getSprite().getX() - velocidad,donChito.getSprite().getY());
                    }
                    break;
                case 2:
                    if(donChito.getSprite().getX()<1100){
                        donChito.setPosition(donChito.getSprite().getX() + velocidad, donChito.getSprite().getY());
                    }
                    break;
                case 3:
                    if(!disparado){
                        proyectil = new SimpleAsset(Constants.ROMAN_PERSONAJE_DONCHITO,donChito.getSprite().getX(),donChito.getSprite().getY()+30);
                        proyectil.getSprite().setScale(0.5f);
                        proyectil.render(batch);
                        disparado = true;
                    }
                    break;
                default:
                    break;
            }
        }
    }
}