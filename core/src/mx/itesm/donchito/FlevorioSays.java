package mx.itesm.donchito;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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

    //TODO Refactor to interface
    private Texture texturaRoca;

    private SpriteBatch batch;
    private Music musicaFondo;

    private SimpleAsset rocaP;
    private SimpleAsset fondo;

    private Array<SimpleAsset> rocas;

    public FlevorioSays(DonChito game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();
        view = new FitViewport(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO,camera);

        //TODO Refactor next code into an Asset Manager
        batch = new SpriteBatch();
        fondo = new SimpleAsset("Imagenes/Simon/base simons.png",new Vector2(0,0));
        fondo.getSprite().scale(1);
        fondo.getSprite().setScale(0.1f);
        cargarAudio();
    }

    private void crearRoca() {
        //rocaP = new SimpleAsset("Imagenes/Simon/Simonst.png",new Vector2(0,0));
        //rocaP.setPosition(new Vector2(DonChito.ALTO_MUNDO / 2, DonChito.ANCHO_MUNDO / 2));
        rocasCreadas = true;
        SimpleAsset nuevo;

        rocas = new Array<SimpleAsset>(24);
        for(int i=0;i<8;i++){
            nuevo = new SimpleAsset("Imagenes/Simon/nivel1.png",new Vector2(0,0));
            rocas.add(nuevo);
        }
        for(int i=0;i<8;i++){
            nuevo = new SimpleAsset("Imagenes/Simon/nivel2.png",new Vector2(0,0));
            rocas.add(nuevo);
        }
        for(int i=0;i<8;i++){
            nuevo = new SimpleAsset("Imagenes/Simon/nivel3.png",new Vector2(0,0));
            rocas.add(nuevo);
        }
        crearCoordenadas();
        Gdx.app.log("Creando rocas", "Se crean rocas");
    }

    private void crearCoordenadas() {
        rocas.get(0).setPosition(new Vector2(625,360));
        rocas.get(1).setPosition(new Vector2(630,210));
        rocas.get(1).setRotation(180f);
        rocas.get(2).setPosition(new Vector2(540, 280));
        rocas.get(2).setRotation(90f);
        rocas.get(3).setPosition(new Vector2(713, 288));
        rocas.get(3).setRotation(270f);
        rocas.get(4).setPosition(new Vector2(687, 336));
        rocas.get(4).setRotation(138f);
        rocas.get(5).setPosition(new Vector2(683, 230));
        rocas.get(5).setRotation(45f);
        rocas.get(6).setPosition(new Vector2(570, 235));
        rocas.get(6).setRotation(318f);
        rocas.get(7).setPosition(new Vector2(572, 340));
        rocas.get(7).setRotation(225f);

        /*
        rocas.get(9).setPosition(new Vector2(630,210));
        rocas.get(9).setRotation(180f);

        rocas.get(10).setPosition(new Vector2(540, 280));
        rocas.get(10).setRotation(90f);

        rocas.get(11).setPosition(new Vector2(713, 288));
        rocas.get(11).setRotation(270f);

        rocas.get(12).setPosition(new Vector2(687, 336));
        rocas.get(12).setRotation(138f);

        rocas.get(13).setPosition(new Vector2(683, 230));
        rocas.get(13).setRotation(45f);

        rocas.get(14).setPosition(new Vector2(570, 235));
        rocas.get(14).setRotation(318f);

        rocas.get(15).setPosition(new Vector2(572, 340));
        rocas.get(15).setRotation(225f);
        */

    }

    private void cargarAudio() {
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("Musica/FlavioSays.mp3"));
        musicaFondo.setLooping(true);
        //musicaFondo.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        if(fondo.getSprite().getScaleX()>=1f){
            if(!rocasCreadas){
                crearRoca();
            }
            else{
                for(SimpleAsset roca: rocas){
                    roca.render(batch);
                }
            }
        }
        else{
            fondo.getSprite().setScale(MathUtils.clamp(delta*INCREMENTO+fondo.getSprite().getScaleX(),.1f,1f));
        }
        fondo.render(batch);
        batch.end();
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
}