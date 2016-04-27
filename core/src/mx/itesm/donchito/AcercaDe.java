package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
                        imgSada,
                        descJoel,
                        descKarla,
                        descLicho,
                        descSada,
                        descSteve,
                        regresarMenu,
                        btnHistorieta;
    private State screenState = State.INIT;
    public AcercaDe(DonChito game) {
        this.game = game;
    }
    @Override
    public void show() {
        Gdx.input.setCatchBackKey(true);
        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();
        view = new FitViewport(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO,camera);
        batch = new SpriteBatch();
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
        descJoel = new SimpleAsset(Constants.ACERCA_DESC_JOEL_PNG,0,0);
        descKarla = new SimpleAsset(Constants.ACERCA_DESC_KARLA_PNG,0,0);
        descLicho = new SimpleAsset(Constants.ACERCA_DESC_LICHO_PNG,0,0);
        descSada = new SimpleAsset(Constants.ACERCA_DESC_SADA_PNG,0,0);
        descSteve = new SimpleAsset(Constants.ACERCA_DESC_STEVE_PNG,0,0);
        regresarMenu = new SimpleAsset(Constants.ACERCA_REGRESAR,1000,10);
        btnHistorieta = new SimpleAsset(Constants.ACERCA_HISTORIETA,30,75);
    }

    private void cargarAudio() {

    }
    private void leerEntrada() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int x, int y, int pointexr, int button) {
                return false;
            }

            @Override
            public boolean touchUp (int x, int y, int pointer, int button) {
                if(screenState == State.INIT) {
                    if(imgJoel.isTouched(x,y,camera,view)){
                        screenState = State.JOEL;
                    }else if(imgKarla.isTouched(x,y,camera,view)){
                        screenState = State.KARLA;
                    }else if(imgLicho.isTouched(x,y,camera,view)){
                        screenState = State.LICHO;
                    }else if(imgSada.isTouched(x,y,camera,view)){
                        screenState = State.SADA;
                    }else if(imgSteve.isTouched(x,y,camera,view)){
                        screenState = State.STEVE;
                    }else if(regresarMenu.isTouched(x,y,camera,view)){
                        game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.MENU,game));
                    }else if(btnHistorieta.isTouched(x,y,camera,view)){
                        game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.SECUENCIA,game,false));
                    }
                    return true;
                }else {
                    if(background.isTouched(x,y,camera,view)){
                        screenState = State.INIT;
                        return true;
                    }
                }
                return false;
            }
        });

    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        view.apply();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            dispose();
            game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.MENU, game));
        }
        background.render(batch);
        switch (screenState){
            case SADA:
                descSada.render(batch);
                break;
            case STEVE:
                descSteve.render(batch);
                break;
            case KARLA:
                descKarla.render(batch);
                break;
            case LICHO:
                descLicho.render(batch);
                break;
            case JOEL:
                descJoel.render(batch);
                break;
            case INIT:
                imgJoel.render(batch);
                imgSteve.render(batch);
                imgSada.render(batch);
                imgLicho.render(batch);
                imgKarla.render(batch);
                regresarMenu.render(batch);
                btnHistorieta.render(batch);
                break;
        }

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
        DonChito.assetManager.clear();
    }
    enum State{
        INIT,
        SADA,
        STEVE,
        KARLA,
        LICHO,
        JOEL
    }
}
