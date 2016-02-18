package mx.itesm.donchito;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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
    private boolean nivelCompleto = true;
    private boolean brillando = true;

    private float tiempoEsperar = 1f;

    private int nivel = 5; //corregir que pasa desde lvl 1
    private Texture texturaRoca;

    private SpriteBatch batch;
    private Music musicaFondo;

    private SimpleAsset rocaP;
    private SimpleAsset fondo;

    private Array<SimpleAsset> rocas;
    private SimpleAsset fondoPantalla;

    private int[] combinaciones = new int[]{0,0,0,0,0,0,0,0,0,0};
    private boolean[] combinacionesPR = new boolean[]{false,false,false,false,false,false,false,false,false,false};
    private Sound efecto;

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

        //TODO Refactor next code into an Asset Manager
        batch = new SpriteBatch();
        fondoPantalla = new SimpleAsset(Constants.FLEVORIO_FONDOPANTALLA_PNG,new Vector2(0,0));
        fondo = new SimpleAsset(Constants.FLEVORIO_FONDO_PNG,new Vector2(0,0));
        fondo.getSprite().scale(1);
        fondo.getSprite().setScale(0.1f);
        cargarAudio();
    }

    private void crearRoca() {
        rocasCreadas = true;
        SimpleAsset nuevo;

        rocas = new Array<SimpleAsset>(3);

        nuevo = new SimpleAsset(Constants.FLEVORIO_BOTONCENTRAL_PNG,new Vector2(555,250));
        nuevo.getSprite().setScale(1.1f);
        rocas.add(nuevo);

        nuevo = new SimpleAsset(Constants.FLEVORIO_BOTON2_PNG,new Vector2(410,360));
        rocas.add(nuevo);
        nuevo = new SimpleAsset(Constants.FLEVORIO_BOTON2_PNG,new Vector2(410,130));
        nuevo.setRotation(180f);
        rocas.add(nuevo);

        nuevo = new SimpleAsset(Constants.FLEVORIO_BOTON3_PNG,new Vector2(278,360));
        nuevo.getSprite().setScale(0.98f);
        rocas.add(nuevo);
        nuevo = new SimpleAsset(Constants.FLEVORIO_BOTON3_PNG,new Vector2(278,25));
        nuevo.setRotation(180f);
        nuevo.getSprite().setScale(0.98f);
        rocas.add(nuevo);

        Gdx.app.log("Creando rocas", "Se crean rocas");
    }

    private void cargarAudio() {
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal(Constants.MUSICA_FLAVIO_SAYS_MP3));
        musicaFondo.setLooping(true);
        //musicaFondo.play();

        efecto = Gdx.audio.newSound(Gdx.files.internal(Constants.FLEVORIO_SONIDOBOTON_WAV));

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(nivelCompleto){
            //correr animacion de SUCCEES!! NEXT LVL...
            crearCombinacion(nivel);
            nivelCompleto = false;
        }

        batch.begin();

        fondoPantalla.render(batch);
        fondo.render(batch);

        if(fondo.getSprite().getScaleX()>=1f){
            if(!rocasCreadas){
                efecto.play();
                crearRoca();
            }
            else {
                for (SimpleAsset roca : rocas) {
                    roca.render(batch);
                }
                for (int i = 0; i < nivel * 2; i++) {
                    if (!combinacionesPR[i]) {
                        rocas.get(combinaciones[i]-1).getSprite().setColor(103, 128, 150, 1);
                        if(esperar(delta)){
                            efecto.play();
                            rocas.get(combinaciones[i]-1).getSprite().setColor(Color.WHITE);
                            combinacionesPR[i] = true;
                        }
                        break;
                    }
                }
            }

        } else {
            fondo.getSprite().setScale(MathUtils.clamp(delta * INCREMENTO + fondo.getSprite().getScaleX(), .1f, 1f));
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
    private void leerEntrada() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int x, int y, int pointexr, int button) {
                if(!brillando){
                    Gdx.app.log("Touch","lol");
                    //if(btnInicio.isTouched(x,y,camera)){
                    //    Gdx.app.log("Juego","botonIncio");
                    //
                    //}
                }
                //donChitoBtn.isTouched(x,y);
                return true; // return true to indicate the event was handled
            }

            @Override
            public boolean touchUp (int x, int y, int pointer, int button) {
                // your touch up code here
                return true; // return true to indicate the event was handled
            }
        });

    }
}