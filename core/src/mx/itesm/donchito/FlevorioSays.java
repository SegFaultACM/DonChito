package mx.itesm.donchito;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Esteban on 2/16/2016.
 */
public class FlevorioSays implements Screen{
    private OrthographicCamera camera;
    private final DonChito game;
    private static final float INCREMENTO = 0.6f;
    private Viewport view;

    private boolean rocasCreadas = false;
    private boolean brillando = true;
    private boolean perdio = false;

    private SimpleAsset botonPausa;

    private State estado = State.PLAY;

    private float tiempoEsperar = 1f;

    private int nivel = 0;
    private int indiceSecuencia = 0;

    private SpriteBatch batch;

    private SimpleAsset fondo;

    private Array<SimpleAsset> rocas;
    private SimpleAsset fondoPantalla;

    private int[] combinaciones = new int[]{0,0,0,0,0,0,0,0,0,0};
    private boolean[] combinacionesPR = new boolean[]{false,false,false,false,false,false,false,false,false,false};

    private Music efectoBoton;
    private Music efectoGanar;
    private Music efectoPerder;
    private Music musicaFondo;
    private Music musicaIntro;


    public FlevorioSays(DonChito game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();
        view = new FitViewport(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO,camera);

        leerEntrada();
        cargarRecursos();

        musicaFondo.setLooping(true);
        musicaIntro.play();

        //TODO Refactor next code into an Asset Manager
        batch = new SpriteBatch();
        fondoPantalla = new SimpleAsset(Constants.FLEVORIO_FONDOPANTALLA_PNG,new Vector2(0,0));
        fondo = new SimpleAsset(Constants.FLEVORIO_FONDO_PNG,new Vector2(0,0));
        botonPausa = new SimpleAsset(Constants.FLEVORIO_BOTON_PAUSA_PNG,new Vector2(1050,10));
        fondo.getSprite().scale(1);
        fondo.getSprite().setScale(0.1f);
    }

    private void cargarRecursos() {
        AssetManager assetManager = DonChito.getAssetManager();

        assetManager.load(Constants.FLEVORIO_FONDOPANTALLA_PNG,Texture.class);
        assetManager.load(Constants.FLEVORIO_FONDO_PNG,Texture.class);
        assetManager.load(Constants.FLEVORIO_BOTONCENTRAL_PNG, Texture.class);
        assetManager.load(Constants.FLEVORIO_BOTON2_PNG, Texture.class);
        assetManager.load(Constants.FLEVORIO_BOTON3_PNG, Texture.class);

        assetManager.load(Constants.FLEVORIO_BOTON_PAUSA_PNG, Texture.class);

        assetManager.load(Constants.FLEVORIO_SONIDOBOTON_WAV, Music.class);
        assetManager.load(Constants.FLEVORIO_SONIDOFAIL_WAV,Music.class);
        assetManager.load(Constants.FLEVORIO_SONIDOVICTORY_WAV, Music.class);
        assetManager.load(Constants.FLEVORIO_MUSICAINTRO_WAV, Music.class);
        assetManager.load(Constants.FLEVORIO_MUSICAFONDO_WAV,Music.class);
        assetManager.finishLoading();

        efectoBoton = assetManager.get(Constants.FLEVORIO_SONIDOBOTON_WAV);
        efectoGanar = assetManager.get(Constants.FLEVORIO_SONIDOVICTORY_WAV);
        efectoPerder = assetManager.get(Constants.FLEVORIO_SONIDOFAIL_WAV);

        musicaFondo = assetManager.get(Constants.FLEVORIO_MUSICAFONDO_WAV);
        musicaIntro = assetManager.get(Constants.FLEVORIO_MUSICAINTRO_WAV);

    }

    private void crearRoca() {
        rocasCreadas = true;
        SimpleAsset nuevo;

        rocas = new Array<SimpleAsset>(3);

        nuevo = new SimpleAsset(Constants.FLEVORIO_BOTONCENTRAL_PNG,new Vector2(505,250));
        nuevo.getSprite().setScale(1.1f);
        rocas.add(nuevo);

        nuevo = new SimpleAsset(Constants.FLEVORIO_BOTON2_PNG,new Vector2(370,365));
        rocas.add(nuevo);
        nuevo = new SimpleAsset(Constants.FLEVORIO_BOTON2_PNG,new Vector2(370,135));
        nuevo.setRotation(180f);
        rocas.add(nuevo);

        nuevo = new SimpleAsset(Constants.FLEVORIO_BOTON3_PNG,new Vector2(253,370));
        nuevo.getSprite().setScale(0.98f);
        rocas.add(nuevo);
        nuevo = new SimpleAsset(Constants.FLEVORIO_BOTON3_PNG,new Vector2(253,18));
        nuevo.setRotation(180f);
        nuevo.getSprite().setScale(0.98f);
        rocas.add(nuevo);
    }

    @Override
    public void render(float delta) {
        camera.update();
        batch.begin();
        if(estado == State.PAUSA){

            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.setColor(0,0,0,0);
            batch.end();
            return;
        }
        else{
            batch.setColor(0,0,0,0);
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(indiceSecuencia == nivel*2){
            //correr animacion de SUCCEES!! NEXT LVL...
            nivel++;
            if(nivel == 4){
                musicaFondo.setLooping(false);
                if(musicaFondo.isPlaying()) {
                    musicaFondo.stop();
                }
                game.setScreen(new MenuPrincipal(game));
            }
            crearCombinacion(nivel);
            indiceSecuencia = 0;
            if(nivel != 1 && !perdio){
                efectoBoton.stop();
                efectoGanar.play();
                perdio = false;
            }
        }
        view.apply();
        fondoPantalla.render(batch);
        fondo.render(batch);
        botonPausa.render(batch);
        if(nivel != 4) {
            if (fondo.getSprite().getScaleX() >= 1f) {
                if (!rocasCreadas) {
                    // Sonido que se reproduce al abrir el proyector
                    efectoBoton.play();

                    crearRoca();
                } else {
                    if (!musicaIntro.isPlaying()) {
                        if (!musicaFondo.isPlaying()) {
                            Gdx.app.log("Musica", "Render");
                            musicaFondo.play();
                        }
                        for (SimpleAsset roca : rocas) {
                            roca.render(batch);
                        }
                        if (!efectoGanar.isPlaying() && !efectoPerder.isPlaying()) {
                            for (int i = 0; i < nivel * 2; i++) {
                                if (!combinacionesPR[i]) {
                                    if (!efectoBoton.isPlaying()) {
                                        efectoBoton.play();
                                    }
                                    rocas.get(combinaciones[i] - 1).getSprite().setColor(103, 128, 150, 1);
                                    if (esperar(delta)) {
                                        rocas.get(combinaciones[i] - 1).getSprite().setColor(Color.WHITE);
                                        combinacionesPR[i] = true;
                                        efectoBoton.stop();
                                    }
                                    break;
                                }
                            }
                            brillando = false;
                            for (int i = 0; i < nivel * 2; i++) {
                                if (!combinacionesPR[i]) {
                                    brillando = true;
                                    break;
                                }
                            }
                        }
                    }
                }

            } else {
                fondo.getSprite().setScale(MathUtils.clamp(delta * INCREMENTO + fondo.getSprite().getScaleX(), .1f, 1f));
            }
        }
        batch.end();
    }

    private boolean esperar(float delta){
        if(tiempoEsperar <=0){
            tiempoEsperar = 1f;
            return true;
        }
        tiempoEsperar -= delta;
        return false;
    }

    private void crearCombinacion(int nivel) {
        for(int i=0;i<combinaciones.length;i++){
            combinaciones[i] = 0;
            combinacionesPR[i] = false;
        }
        for(int i=0;i<nivel*2;i++){
            combinaciones[i] = MathUtils.random(1,5);
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
            @Override
            public boolean touchDown (int x, int y, int pointexr, int button) {
                if(!brillando){
                    int roca = 0;
                    float distancia = (float) Math.sqrt(Math.pow(625-x,2)+Math.pow(348-y,2));
                    if(distancia>=0 && distancia<= 164){
                        roca = 1;
                    }
                    if(distancia>=165 && distancia<= 269){
                        if(y<358){
                            roca = 2;
                        }
                        else{
                            roca = 3;
                        }
                    }
                    if(distancia>=270 && distancia<= 396){
                        if(y<358){
                            roca = 4;
                        }
                        else{
                            roca = 5;
                        }
                    }
                    if(roca !=0){
                        efectoBoton.stop();
                        efectoBoton.play();
                        if(roca == combinaciones[indiceSecuencia]){
                            indiceSecuencia++;
                            perdio = false;
                        }
                        else{
                            nivel --;
                            perdio = true;
                            indiceSecuencia = nivel*2;
                            brillando = true;
                            efectoBoton.stop();
                            efectoPerder.play();
                        }
                    }
                    else{
                        //    VER SI SE APRETO EL BOTON DE MENU
                        Gdx.app.log(""+x,""+y);
                        if(x>1050 && y >540){
                            estado = State.PAUSA;
                            Gdx.app.log("PAUSA","PAUSA");
                        }

                    }

                }
                return true; // return true to indicate the event was handled
            }

            @Override
            public boolean touchUp (int x, int y, int pointer, int button) {
                // your touch up code here
                return true; // return true to indicate the event was handled
            }
        });
    }
    public enum State
    {
        PAUSA,
        PLAY
    }
}