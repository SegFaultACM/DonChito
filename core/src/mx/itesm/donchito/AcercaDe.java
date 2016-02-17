package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class AcercaDe implements Screen {
    private OrthographicCamera camera;
    private final DonChito game;
    private Viewport view;

    private SpriteBatch batch;
    BitmapFont body = new BitmapFont();
    BitmapFont title = new BitmapFont();
    private SimpleAsset background;
    private String AcercaDeText;
    private String Title;

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

        // Assets and text declaration
        background = new SimpleAsset("Imagenes/PantallasConfig/Config.png",new Vector2(0,0));
        Title = "Primer entregable de Videojuegos.";
        AcercaDeText = "Don chito es un pueblerino ejemplar el \n" +
                "cual ha sido designado para proteger el mineral que \n" +
                "hace que el pueblo Laurencio destaque de los demás. \n" +
                "Siempre busca la honestidad,amabilidad,confianza,\n " +
                "siempre busca el bienestar del prójimo.\n" +
                " El será quien luche contra el enemigo para salvar al pueblo.\n\n" +
                "Daniel Sada Caraveo A01169735\n" +
                "Joel Lara Quintana A01374649\n" +
                "Esteban Gil Martínez A01375048\n" +
                "Luis Manuel Alcala Alcaraz   A01372538\n" +
                "Karla Aidee Gonz\u00e1lez Vega A01373938\n";
    }

    private void cargarAudio() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.render(batch);
        title.draw(batch, Title,350, 600);
        body.draw(batch, AcercaDeText,DonChito.ANCHO_MUNDO / 4 ,500);
        title.getData().setScale(2.5f);
        body.getData().setScale(1.7f);
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

    }
}
