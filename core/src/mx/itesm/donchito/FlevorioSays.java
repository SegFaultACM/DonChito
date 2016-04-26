package mx.itesm.donchito;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
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
    private boolean salirMenu = false;
    private boolean instruccionesLeidas = false;
    private boolean jugando = true;

    private GameText instructionsTxt;
    private GameText levelTxt;

    private SimpleAsset botonPausa;
    private SimpleAsset botonPlay;
    private SimpleAsset botonSalirMenu;
    private SimpleAsset botonConfiguracion;

    private SimpleAsset fondoPantalla;
    private SimpleAsset fondoPausa;


    private State estado = State.PLAY;

    private float tiempoEsperar = 1f;
    private boolean reseted = false;

    private int indiceInstruccion = 0;
    private int shadowedAndUp = 0;
    private int lastRockPressed = 0;
    private int nivel = 0;
    private int indiceSecuencia = 0;

    private SpriteBatch batch;

    private SimpleAsset fondo;

    private Array<SimpleAsset> rocas;



    private int[] combinaciones = new int[]{0,0,0,0,0,0,0,0,0,0};
    private boolean[] combinacionesPR = new boolean[]{false,false,false,false,false,false,false,false,false,false};
    private SimpleAsset[] instrucciones = new SimpleAsset[15];

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
        if(!(DonChito.preferences.getBoolean("FlevorioSays",false))){
            DonChito.preferences.putBoolean("FlevorioSays",false);
            DonChito.preferences.flush();
        }


        init();
        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();

        view = new FitViewport(DonChito.ANCHO_MUNDO,DonChito.ALTO_MUNDO,camera);

        leerEntrada();
        cargarRecursos();


        musicaFondo.setLooping(true);
        reproducirMusica(musicaIntro);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        fondoPantalla = new SimpleAsset(Constants.FLEVORIO_FONDOPANTALLA_PNG,0,0);
        fondo = new SimpleAsset(Constants.FLEVORIO_FONDO_PNG,0,0);
        fondo.getSprite().scale(1);
        fondo.getSprite().setScale(0.1f);

        fondoPausa = new SimpleAsset(Constants.GLOBAL_MENU_PAUSA_PNG,0,0);
        botonPlay = new SimpleAsset(Constants.GLOBAL_BOTON_PLAY_PNG,1050,10);
        botonConfiguracion = new SimpleAsset(Constants.GLOBAL_BOTON_CONFIGURACION_PNG,405,175);
        botonSalirMenu = new SimpleAsset(Constants.GLOBAL_BOTON_SALIRMENU_PNG,405,425);

        instructionsTxt = new GameText(150,700);
        levelTxt = new GameText(1000,700);

        botonPausa = new SimpleAsset(Constants.GLOBAL_BOTON_PAUSA_PNG,1050,10);

        instrucciones[0] = new SimpleAsset(Constants.FLEVORIO_INSTRUCCION1,0,0);
        instrucciones[1] = new SimpleAsset(Constants.FLEVORIO_INSTRUCCION2,0,0);
        instrucciones[2] = new SimpleAsset(Constants.FLEVORIO_INSTRUCCION3,0,0);
        instrucciones[3] = new SimpleAsset(Constants.FLEVORIO_INSTRUCCION4,0,0);
        instrucciones[4] = new SimpleAsset(Constants.FLEVORIO_INSTRUCCION5,0,0);
        instrucciones[5] = new SimpleAsset(Constants.FLEVORIO_INSTRUCCION6,0,0);
        instrucciones[6] = new SimpleAsset(Constants.FLEVORIO_INSTRUCCION7,0,0);
        instrucciones[7] = new SimpleAsset(Constants.FLEVORIO_INSTRUCCION8,0,0);
        instrucciones[8] = new SimpleAsset(Constants.FLEVORIO_INSTRUCCION9,0,0);
        instrucciones[9] = new SimpleAsset(Constants.FLEVORIO_INSTRUCCION10,0,0);
        instrucciones[10] = new SimpleAsset(Constants.FLEVORIO_INSTRUCCION11,0,0);
        instrucciones[11] = new SimpleAsset(Constants.FLEVORIO_INSTRUCCION12,0,0);
        instrucciones[12] = new SimpleAsset(Constants.FLEVORIO_INSTRUCCION13,0,0);
        instrucciones[13] = new SimpleAsset(Constants.FLEVORIO_INSTRUCCION14,0,0);
        instrucciones[14] = new SimpleAsset(Constants.FLEVORIO_INSTRUCCION15,0,0);

    }
    private void init() {
        nivel = 0;
        indiceSecuencia = 0;
    }

    private void cargarRecursos() {
        AssetManager assetManager = DonChito.assetManager;
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

        nuevo = new SimpleAsset(Constants.FLEVORIO_BOTONCENTRAL_PNG,505,250);
        nuevo.getSprite().setScale(1.1f);
        rocas.add(nuevo);

        nuevo = new SimpleAsset(Constants.FLEVORIO_BOTON2_PNG,370,365);
        rocas.add(nuevo);
        nuevo = new SimpleAsset(Constants.FLEVORIO_BOTON2_PNG,370,135);
        nuevo.setRotation(180f);
        rocas.add(nuevo);

        nuevo = new SimpleAsset(Constants.FLEVORIO_BOTON3_PNG,253,370);
        nuevo.getSprite().setScale(0.98f);
        rocas.add(nuevo);
        nuevo = new SimpleAsset(Constants.FLEVORIO_BOTON3_PNG,253,18);
        nuevo.setRotation(180f);
        nuevo.getSprite().setScale(0.98f);
        rocas.add(nuevo);
    }
    public void stopMusic(){
        if(musicaFondo.isPlaying()){
            musicaFondo.stop();
        }
        if(musicaIntro.isPlaying()){
            musicaIntro.stop();
        }
        if(efectoBoton.isPlaying()){
            efectoBoton.stop();
        }
        if(efectoGanar.isPlaying()){
            efectoGanar.stop();
        }
        if(efectoPerder.isPlaying()){
            efectoPerder.stop();
        }
    }

    @Override
    public void render(float delta) {
        camera.update();
        view.apply();
        batch.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(indiceSecuencia == nivel*2){
            //correr animacion de SUCCEES!! NEXT LVL...
            lastRockPressed = 0;
            reseted = false;
            nivel++;
            if(nivel == 2){ //cambiar a 4
                DonChito.preferences.putBoolean("FlevorioSays",true);
                DonChito.preferences.flush();
                stopMusic();
                jugando = false;
                dispose();
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.FINALBOSS,game));
            }
            crearCombinacion(nivel);
            indiceSecuencia = 0;
            if(nivel != 1 && !perdio){
                efectoBoton.stop();
                reproducirMusica(efectoGanar);
                perdio = false;
            }
        }
        fondoPantalla.render(batch);
        fondo.render(batch);
        if(!instruccionesLeidas){
            leerInstrucciones(delta);
        }
        if(nivel != 4 && instruccionesLeidas && jugando) {
            if (fondo.getSprite().getScaleX() >= 1f) {
                if (!rocasCreadas) {
                    // Sonido que se reproduce al abrir el proyector
                    reproducirMusica(efectoBoton);
                    crearRoca();
                } else {
                    if (!musicaIntro.isPlaying()) {
                        levelTxt.showMessage(batch,"Nivel : "+nivel);
                        if (!musicaFondo.isPlaying()) {
                            reproducirMusica(musicaFondo);
                        }
                        for (SimpleAsset roca : rocas) {
                            roca.render(batch);
                        }
                        if (!efectoGanar.isPlaying() && !efectoPerder.isPlaying()) {
                            if(!reseted) {
                                for (SimpleAsset roca : rocas) {
                                    roca.getSprite().setColor(Color.WHITE);
                                    roca.render(batch);
                                }
                                reseted = true;
                            }
                            for (int i = 0; i < nivel * 2; i++) {
                                if(brillando){instructionsTxt.showMessage(batch,"PENSANDO");}
                                if (!combinacionesPR[i]) {
                                    if (!efectoBoton.isPlaying()) {
                                        reproducirMusica(efectoBoton);
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
                            if(!brillando){
                                instructionsTxt.showMessage(batch,"JUEGA");
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
                    else {
                        instructionsTxt.showMessage(batch,"ESPERA");
                    }
                }

            } else {
                fondo.getSprite().setScale(MathUtils.clamp(delta * INCREMENTO + fondo.getSprite().getScaleX(), .1f, 1f));
            }
        }
        if(estado == State.PAUSA){
            fondoPausa.render(batch);
            botonPlay.render(batch);
            botonConfiguracion.render(batch);
            botonSalirMenu.render(batch);
            if(salirMenu) {
                stopMusic();
                dispose();
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.MENU,game));
            }
        }
        else {
            if(instruccionesLeidas) {
                botonPausa.render(batch);
            }
            else{
                botonPlay.render(batch);
            }
        }
        batch.end();
    }

    private void leerInstrucciones(float delta) {
        if(indiceInstruccion >=15){
            instruccionesLeidas = true;
            return;
        }
        instrucciones[indiceInstruccion].render(batch);
        if(esperar(delta)){
            indiceInstruccion++;
        }
    }
    private void reproducirMusica(Music musica){
        musica.setVolume(1F);
        if(!DonChito.preferences.getBoolean(Constants.MENUPRINCIPAL_SOUND_PREF,true)){
            musica.setVolume(0f);
        }
        musica.play();
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
        DonChito.assetManager.clear();
    }
    private void leerEntrada() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int x, int y, int pointexr, int button) {
                Vector3 temp = camera.unproject(new Vector3(x, y, 0),view.getScreenX(),view.getScreenY(),view.getScreenWidth(),view.getScreenHeight());
                int xt =(int) temp.x;
                int yt = (int) temp.y;
                //Gdx.app.log("Coordenadas x,y","x: "+xt+"  y: "+yt);
                if(!brillando){
                    int roca = 0;
                    float distancia = (float) Math.sqrt(Math.pow(625-xt,2)+Math.pow(348-yt,2));
                    if(distancia>=0 && distancia<= 164){
                        roca = 1;
                    }
                    if(distancia>=165 && distancia<= 269){
                        if(yt<358){
                            roca = 3;
                        }
                        else{
                            roca = 2;
                        }
                    }
                    if(distancia>=270 && distancia<= 396){
                        if(yt<358){
                            roca = 5;
                        }
                        else{
                            roca = 4;
                        }
                    }
                    if(roca !=0 && estado == State.PLAY){
                        rocas.get(roca-1).getSprite().setColor(Color.GRAY);
                    }
                    shadowedAndUp = roca;
                }
                if(estado == State.PAUSA){
                    //detectar los botones en el menu de pausa.
                    if(botonSalirMenu.isTouched(x,y,camera,view)){
                        init();
                        musicaFondo.setLooping(false);
                        if(musicaFondo.isPlaying()) {
                            musicaFondo.stop();
                        }
                        salirMenu = true;
                    }
                }
                return true; // return true to indicate the event was handled
            }

            @Override
            public boolean touchUp (int x, int y, int pointer, int button) {
                Vector3 temp = camera.unproject(new Vector3(x, y, 0),view.getScreenX(),view.getScreenY(),view.getScreenWidth(),view.getScreenHeight());
                int xt =(int) temp.x;
                int yt = (int) temp.y;
                if(!instruccionesLeidas){
                    if(botonPlay.isTouched(x,y,camera,view)){
                        if(musicaIntro.isPlaying()){
                            musicaIntro.stop();
                        }
                        indiceInstruccion = 15;
                        tiempoEsperar = 1f;
                        instruccionesLeidas = true;
                    }
                }
                if(!brillando){
                    for (SimpleAsset roca : rocas) {
                        roca.getSprite().setColor(Color.WHITE);
                    }
                    int roca = 0;
                    float distancia = (float) Math.sqrt(Math.pow(625-xt,2)+Math.pow(348-yt,2));
                    if(distancia>=0 && distancia<= 164){
                        roca = 1;
                    }
                    if(distancia>=165 && distancia<= 269){
                        if(yt<358){
                            roca = 3;
                        }
                        else{
                            roca = 2;
                        }
                    }
                    if(distancia>=270 && distancia<= 396){
                        if(yt<358){
                            roca = 5;
                        }
                        else{
                            roca = 4;
                        }
                    }
                    if(roca !=0 && estado == State.PLAY && shadowedAndUp == roca){
                        efectoBoton.stop();
                        reproducirMusica(efectoBoton);
                        if(roca == combinaciones[indiceSecuencia]){
                            if(lastRockPressed != 0){
                                rocas.get(lastRockPressed-1).getSprite().setColor(Color.WHITE);
                            }
                            rocas.get(roca-1).getSprite().setColor(103, 128, 150, 1);
                            indiceSecuencia++;
                            perdio = false;
                            lastRockPressed = roca;
                        }
                        else{
                            rocas.get(roca-1).getSprite().setColor(Color.RED);
                            nivel --;
                            perdio = true;
                            indiceSecuencia = nivel*2;
                            brillando = true;
                            efectoBoton.stop();
                            reproducirMusica(efectoPerder);
                            reseted = false;
                        }
                    }
                    else{
                        if(botonPausa.isTouched(x,y,camera,view)||botonPlay.isTouched(x,y,camera,view)){
                            if(estado == State.PLAY){
                                estado = State.PAUSA;
                            }
                            else{
                                estado = State.PLAY;
                            }
                        }
                    }
                }
                if(estado == State.PAUSA){
                    //detectar los botones en el menu de pausa.
                    if(botonSalirMenu.isTouched(x,y,camera,view)){
                        init();
                        musicaFondo.setLooping(false);
                        if(musicaFondo.isPlaying()) {
                            musicaFondo.stop();
                        }
                        salirMenu = true;
                    }
                }
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