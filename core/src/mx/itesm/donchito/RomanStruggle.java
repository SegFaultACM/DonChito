package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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

    private SimpleAsset botonIzquierda;
    private SimpleAsset botonDerecha;
    private SimpleAsset botonDisparo;
    private SimpleAsset botonPausa;
    private SimpleAsset botonPlay;
    private SimpleAsset botonSalirMenu;
    private SimpleAsset botonConfiguracion;
    private SimpleAsset proyectil;

    //FALTA HACERLO PERSONAJE
    private SimpleAsset donChito;




    private SimpleAsset fondoPantalla;
    private SimpleAsset fondoPausa;

    private DelayedRemovalArray<RomanRock> rocas;
    RomanRock rocaP;

    private boolean disparado = false;


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
        //init();
        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();
        view = new FitViewport(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO,camera);

        leerEntrada();
        cargarRecursos();

        //musicaFondo.setLooping(true);
        //musicaIntro.play();
        rocas = new DelayedRemovalArray<RomanRock>();
        //TODO Refactor next code into an Asset Manager
        batch = new SpriteBatch();
        rocaP = new RomanRock(Constants.ROMAN_PERSONAJE_DONCHITO,550,700,1,1,1,0);
        rocas.add(rocaP);

        fondoPantalla = new SimpleAsset(Constants.ROMAN_FONDO,0,0);
        botonIzquierda = new SimpleAsset(Constants.ROMAN_BOTON_IZQUIERDA,20,10);
        botonDerecha = new SimpleAsset(Constants.ROMAN_BOTON_DERECHA,170,10);
        botonDisparo = new SimpleAsset(Constants.ROMAN_BOTON_DISPARA,320,10);

        //FALTA HACER A DON CHITO COMO UN PERSONAJE
        donChito = new SimpleAsset(Constants.ROMAN_PERSONAJE_DONCHITO,550,10);
    }

    /* inicializar niveles, vidas, etc.
    private void init() {
        nivel = 0;
        indiceSecuencia = 0;
    }
    */

    private void cargarRecursos() {
        AssetManager assetManager = DonChito.getAssetManager();

        assetManager.load(Constants.ROMAN_BOTON_IZQUIERDA,Texture.class);
        assetManager.load(Constants.ROMAN_BOTON_DERECHA,Texture.class);
        assetManager.load(Constants.ROMAN_BOTON_DISPARA, Texture.class);
        assetManager.load(Constants.ROMAN_PERSONAJE_DONCHITO, Texture.class);
        assetManager.load(Constants.ROMAN_FONDO, Texture.class);

        assetManager.load(Constants.FLEVORIO_MENU_PAUSA_PNG, Texture.class);
        assetManager.load(Constants.FLEVORIO_BOTON_PAUSA_PNG, Texture.class);
        assetManager.load(Constants.FLEVORIO_BOTON_PLAY_PNG, Texture.class);
        assetManager.load(Constants.FLEVORIO_BOTON_CONFIGURACION_PNG, Texture.class);
        assetManager.load(Constants.FLEVORIO_BOTON_SALIRMENU_PNG, Texture.class);

        /*
        assetManager.load(Constants.FLEVORIO_SONIDOBOTON_WAV, Music.class);
        assetManager.load(Constants.FLEVORIO_SONIDOFAIL_WAV,Music.class);
        assetManager.load(Constants.FLEVORIO_SONIDOVICTORY_WAV, Music.class);
        assetManager.load(Constants.FLEVORIO_MUSICAINTRO_WAV, Music.class);
        assetManager.load(Constants.FLEVORIO_MUSICAFONDO_WAV,Music.class);
        */
        assetManager.finishLoading();
        /*
        efectoBoton = assetManager.get(Constants.FLEVORIO_SONIDOBOTON_WAV);
        efectoGanar = assetManager.get(Constants.FLEVORIO_SONIDOVICTORY_WAV);
        efectoPerder = assetManager.get(Constants.FLEVORIO_SONIDOFAIL_WAV);

        musicaFondo = assetManager.get(Constants.FLEVORIO_MUSICAFONDO_WAV);
        musicaIntro = assetManager.get(Constants.FLEVORIO_MUSICAINTRO_WAV);
        */
    }

    @Override
    public void render(float delta) {
        camera.update();
        batch.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        fondoPantalla.render(batch);

        ejecutarInputs();

        donChito.render(batch);
        botonDerecha.render(batch);
        botonIzquierda.render(batch);
        botonDisparo.render(batch);
        view.apply();

        if(estado == State.PAUSA){
            fondoPausa = new SimpleAsset(Constants.FLEVORIO_MENU_PAUSA_PNG,0,0);
            botonPlay = new SimpleAsset(Constants.FLEVORIO_BOTON_PLAY_PNG,1050,10);
            botonConfiguracion = new SimpleAsset(Constants.FLEVORIO_BOTON_CONFIGURACION_PNG,405,175);
            botonSalirMenu = new SimpleAsset(Constants.FLEVORIO_BOTON_SALIRMENU_PNG,405,425);

            botonPlay.setAlpha(0.5f);

            fondoPausa.render(batch);
            botonPlay.render(batch);
            botonConfiguracion.render(batch);
            botonSalirMenu.render(batch);
        }
        else{
            botonPausa = new SimpleAsset(Constants.FLEVORIO_BOTON_PAUSA_PNG,1050,10);
            botonPausa.setAlpha(0.5f);
            botonPausa.render(batch);
            for(RomanRock roca: rocas){
                roca.updateRock();
                roca.render(batch);
                revisarColisiones(roca);
                indiceRocas++;
            }
            indiceRocas = 0;
            if(disparado){
                if(proyectil.getSprite().getY() >600){
                    disparado = false;
                }
                else{
                    proyectil.setPosition(new Vector2(proyectil.getSprite().getX(),proyectil.getSprite().getY()+velocidadBala));
                    proyectil.render(batch);
                }
            }
        }
        batch.end();
    }

    private void revisarColisiones(RomanRock roca) {
        float rocaX = roca.getSprite().getX();
        float rocaY = roca.getSprite().getY();

        //revisar con personaje
        if(roca.getSprite().getY() <60){
            if(rocaX>donChito.getSprite().getX()-100 && rocaX<donChito.getSprite().getX()+100){
                //Gdx.app.log("CHOCO CON PERSONAJE"," LOL TONTO PERDISTEEEE");
            }
        }
        //revisar con bala
        if(disparado) {
            if (rocaX> proyectil.getSprite().getX() - 40 &&rocaX< proyectil.getSprite().getX() + 40 &&rocaY<proyectil.getSprite().getY()+20) {
                proyectil.setPosition(new Vector2(1000,1000));
                disparado = false;
                if(roca.getEscala()/2 >= 0.25) {
                    RomanRock nuevo = new RomanRock(Constants.ROMAN_PERSONAJE_DONCHITO,roca.getSprite().getX(), roca.getSprite().getY(), 1, roca.getDireccionV(), roca.getEscala()/2,roca.getVelocidad());
                    rocas.add(nuevo);
                    nuevo = new RomanRock(Constants.ROMAN_PERSONAJE_DONCHITO, roca.getSprite().getX(), roca.getSprite().getY(), 0, roca.getDireccionV(), roca.getEscala()/2,roca.getVelocidad());
                    rocas.add(nuevo);
                }
                rocas.removeIndex(indiceRocas);
                puntos += roca.getEscala()*10;
                Gdx.app.log("Puntos", ""+puntos);
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
        DonChito.getAssetManager().clear();
    }
    private void leerEntrada() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchUp(int x, int y, int pointer, int button) {
                estadoBoton = State.NOPRESIONADO;
                return true; // return true to indicate the event was handled
            }
            public boolean touchDown(int x, int y, int pointexr, int button) {
                estadoBoton = State.PRESIONADO;
                // x  15
                // x  1250
                if (estado == State.PAUSA) {
                    if (x < 828 && x > 414 && y < 284 && y > 213) {
                        //init();
                        //musicaFondo.setLooping(false);
                        //if (musicaFondo.isPlaying()) {
                        //    musicaFondo.stop();
                        //}
                        game.setScreen(new MenuPrincipal(game));
                    }
                }
                if(x>1050 && y >540){
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
        NOPRESIONADO
    }
    private void ejecutarInputs(){
        if(estadoBoton == State.PRESIONADO){
            int x = Gdx.app.getInput().getX();
            int y = Gdx.app.getInput().getY();
            if(x>20 && x<125 && y>610 && y<710) {
                botonPresionado = 1;
            }
            else if(x>170 && x<265 && y>610 && y<710){
                botonPresionado = 2;

            }
            else if(x>325 && x<425 && y>610 && y<710){
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