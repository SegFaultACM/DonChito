package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.badlogic.gdx.utils.viewport.Viewport;


public class AcercaDe implements Screen {
    private OrthographicCamera camera;
    private final DonChito game;
    private Viewport view;

    private SpriteBatch batch;
    private SimpleAsset background,
                        imgJoel,
                        imgSteve,
                        imgLicho,
                        imgKarla,
                        imgSada;

    public AcercaDe(DonChito game) {
        this.game = game;
    }
    @Override
    public void show() {

        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();
        view = new FitViewport(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO,camera);
        batch = new SpriteBatch();
        cargarRecursos();
        // Assets and text declaration
        crearElementos();

        leerEntrada();
    }

    private void crearElementos(){
        background = new SimpleAsset(Constants.ACERCA_FONDO_JPG,0,0);
        imgJoel = new SimpleAsset(Constants.ACERCA_JOEL_PNG,805,275);
        imgKarla = new SimpleAsset(Constants.ACERCA_KARLA_PNG,230,275);
        imgLicho = new SimpleAsset(Constants.ACERCA_LICHO_PNG,0,275);
        imgSada = new SimpleAsset(Constants.ACERCA_SADA_PNG,1035,275);
        imgSteve = new SimpleAsset(Constants.ACERCA_STEVE_PNG,575,275);
    }

    private void cargarAudio() {

    }
    private void cargarRecursos() {
        AssetManager assetManager = DonChito.getAssetManager();
        assetManager.load(Constants.ACERCA_FONDO_JPG,Texture.class);
        assetManager.load(Constants.ACERCA_JOEL_PNG,Texture.class);
        assetManager.load(Constants.ACERCA_KARLA_PNG,Texture.class);
        assetManager.load(Constants.ACERCA_LICHO_PNG,Texture.class);
        assetManager.load(Constants.ACERCA_SADA_PNG,Texture.class);
        assetManager.load(Constants.ACERCA_STEVE_PNG,Texture.class);
        assetManager.finishLoading();
    }
    private void leerEntrada() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int x, int y, int pointexr, int button) {
                return false;
            }

            @Override
            public boolean touchUp (int x, int y, int pointer, int button) {
                // your touch up code here
                if(background.isTouched(x,y,camera)){
                    game.setScreen(new MenuPrincipal(game));
                    return true;
                }
                return false;
            }
        });

    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        view.apply();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        background.render(batch);
        imgJoel.render(batch);
        imgSteve.render(batch);
        imgSada.render(batch);
        imgLicho.render(batch);
        imgKarla.render(batch);
        batch.end();

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
}
