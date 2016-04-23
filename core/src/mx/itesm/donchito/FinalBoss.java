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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.Random;

public class FinalBoss implements Screen {
    private OrthographicCamera camera;
    private final DonChito game;
    private Viewport view;

    private boolean movimientoInicialFlevorio = false;
    private boolean movimientoInicialDonChito = false;

    private GameText picoText;
    private GameText botasText;
    private GameText piedraText;
    private GameText resorteraText;
    private GameText donchitoDam;
    private GameText flevorioDam;
    private GameText accionText;
    private GameText donChitoNameText;
    private GameText flevorioNameText;

    private boolean danioInflinjido = false;
    private boolean accionFlevorioDefinida = false;
    private boolean curacionFlevorio = false;
    private boolean flevorioAturdido = false;
    private boolean todosLosItems = true;

    private String modoAtaque = "";

    private float tiempoEsperar = 3.5f;

    private SpriteBatch batch;

    private SimpleAsset fondo;
    private SimpleAsset flevorio;
    private SimpleAsset donChito;

    private SimpleAsset donChitoHealthBar;
    private SimpleAsset donchitoHealth;
    private SimpleAsset flevorioHealthBar;
    private SimpleAsset flevorioHealth;

    private SimpleAsset botonCurar;
    private SimpleAsset botonTribalera;
    private SimpleAsset botonResortera;
    private SimpleAsset botonPico;

    private SimpleAsset botonPausa;
    private SimpleAsset botonPlay;
    private SimpleAsset fondoPausa;
    private SimpleAsset botonSalirMenu;
    private SimpleAsset botonConfiguracion;

    private State estado = State.INTRO;
    private AtackState estadoAtaque = AtackState.IDLE;
    private AtackMade ataqueHecho = AtackMade.DONCHITOBOTAS;

    private float healthHeight;
    private float healthWidth;

    private int vidaDonChito = 700;
    private int vidaFlevorio = 700;
    private int damage = 0;
    private int turnoBloqueoBota = 2;
    Random randomIntGenerator = new Random();

    private Music musicaFondo;

    public FinalBoss(DonChito game) {
        this.game = game;
    }

    @Override
    public void show() {
        if (!(DonChito.preferences.getBoolean("FinalBoss", false))) {
            DonChito.preferences.putBoolean("FinalBoss", false);
            DonChito.preferences.flush();
        }

        camera = new OrthographicCamera(DonChito.ANCHO_MUNDO, DonChito.ALTO_MUNDO);
        camera.position.set(DonChito.ANCHO_MUNDO / 2, DonChito.ALTO_MUNDO / 2, 0);
        camera.update();
        view = new FitViewport(DonChito.ANCHO_MUNDO, DonChito.ALTO_MUNDO, camera);

        leerEntrada();
        cargarRecursos();

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        musicaFondo.setLooping(true);
        reproducirMusica(musicaFondo);
        fondo = new SimpleAsset(Constants.FINAL_BOSS_FONDO, 0, 0);
        donChito = new SimpleAsset(Constants.FINAL_BOSS_DONCHITO, -300, 250);
        flevorio = new SimpleAsset(Constants.FINAL_BOSS_FLEVORIO, 2500, 200);

        resorteraText = new GameText(140,80);
        botasText = new GameText(450,80);
        piedraText = new GameText(140,200);
        picoText = new GameText(450,200);
        donchitoDam = new GameText(410,300);
        flevorioDam = new GameText(830,370);
        accionText = new GameText(300,500);
        donChitoNameText = new GameText(150,670);
        flevorioNameText = new GameText(1000,670);

        donchitoHealth = new SimpleAsset(Constants.FINAL_BOSS_HEALTHBAR, 60, 600);
        donChitoHealthBar = new SimpleAsset(Constants.FINAL_BOSS_HEALTHBARB, 60, 600);
        flevorioHealth= new SimpleAsset(Constants.FINAL_BOSS_HEALTHBAR, 900, 600);
        flevorioHealthBar= new SimpleAsset(Constants.FINAL_BOSS_HEALTHBARB, 900, 600);

        fondoPausa = new SimpleAsset(Constants.GLOBAL_MENU_PAUSA_PNG,0,0);
        botonPlay = new SimpleAsset(Constants.GLOBAL_BOTON_PLAY_PNG,1050,10);
        botonConfiguracion = new SimpleAsset(Constants.GLOBAL_BOTON_CONFIGURACION_PNG,405,175);
        botonSalirMenu = new SimpleAsset(Constants.GLOBAL_BOTON_SALIRMENU_PNG,405,425);
        botonPausa = new SimpleAsset(Constants.GLOBAL_BOTON_PAUSA_PNG,1050,10);


        resorteraText.setColor(.323f,.4823f,.649f,1);
        botasText.setColor(.323f,.4823f,.649f,1);
        piedraText.setColor(.323f,.4823f,.649f,1);
        picoText.setColor(.323f,.4823f,.649f,1);
        accionText.setColor(0.618f,.111f,0.18f,1);
        donchitoDam.setColor(1f,.1f,.1f,1);
        flevorioDam.setColor(1f,.1f,.1f,1);
        flevorioNameText.setColor(0.618f,.111f,0.18f,1);
        donChitoNameText.setColor(0.618f,.111f,0.18f,1);

        botonResortera = new SimpleAsset(Constants.FINAL_BOSS_RESORTERA, -100, -50);
        botonResortera.getSprite().setScale(0.5f);

        // TODO QUITAR CUANDO SE TERMINE LIVERMORIO
        DonChito.preferences.putBoolean("Livermorio",true);
        DonChito.preferences.putBoolean("RomanStruggle",true);
        //

        if(!DonChito.preferences.getBoolean("RomanStruggle", false)){
            botonResortera.getSprite().setColor(Color.BLACK);
            todosLosItems = false;
        }
        botonTribalera = new SimpleAsset(Constants.FINAL_BOSS_BOTAS, 210, -50);
        botonTribalera.getSprite().setScale(0.5f);

        if(!DonChito.preferences.getBoolean("Livermorio", false)){
            botonTribalera.getSprite().setColor(Color.BLACK);
            todosLosItems = false;
        }
        botonCurar = new SimpleAsset(Constants.FINAL_BOSS_PIEDRA, -100, 70);
        botonCurar.getSprite().setScale(0.5f);
        botonPico = new SimpleAsset(Constants.FINAL_BOSS_PICO, 210, 70);
        botonPico.getSprite().setScale(0.5f);

        healthHeight = flevorioHealthBar.getSprite().getHeight();
        healthWidth = flevorioHealthBar.getSprite().getWidth();
    }


    private void cargarRecursos() {
        AssetManager assetManager = DonChito.assetManager;
        musicaFondo = assetManager.get(Constants.FINAL_BOSS_MUSICA);
        //efectoBoton = assetManager.get(Constants.FLEVORIO_SONIDOBOTON_WAV);
        //efectoGanar = assetManager.get(Constants.FLEVORIO_SONIDOVICTORY_WAV);
        //efectoPerder = assetManager.get(Constants.FLEVORIO_SONIDOFAIL_WAV);

        //musicaFondo = assetManager.get(Constants.FLEVORIO_MUSICAFONDO_WAV);
        //musicaIntro = assetManager.get(Constants.FLEVORIO_MUSICAINTRO_WAV);

    }
    private boolean flevorioAtaca() {
        if(!accionFlevorioDefinida) {
            if (vidaFlevorio <= 200) {
                curacionFlevorio = false;
            }
            else if(vidaFlevorio<=550){
                if (randomIntGenerator.nextInt(2) == 0) {
                    curacionFlevorio =  false;
                }
                else{
                    curacionFlevorio = true;
                }
            }
            else{
                curacionFlevorio = true;
            }
            accionFlevorioDefinida = true;
        }
        return curacionFlevorio;
    }

    public void stopMusic(){
        if(musicaFondo.isPlaying()){
            musicaFondo.stop();
        }
        /*
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
        */
    }

    private boolean esperar(float delta){
        if(tiempoEsperar <=0){
            tiempoEsperar = 3.5f;
            return true;
        }
        tiempoEsperar -= delta;
        return false;
    }

    @Override
    public void render(float delta) {
        camera.update();
        view.apply();
        batch.begin();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        fondo.render(batch);
        if(estado == State.INTRO){
            movimientosIniciales(donChito,flevorio);
        }
        else if (estado == State.PLAY || estado == State.PAUSA){
            if(vidaDonChito == 0){
                //efectoGanar.play();
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.CUEVA,game));
            }
            else {
                botonPausa.render(batch);

                danioInflinjido = false;
                botonCurar.render(batch);
                botonTribalera.render(batch);
                botonResortera.render(batch);
                botonPico.render(batch);

                flevorioHealth.getSprite().setRegion(0, 0, (int) (vidaFlevorio * healthWidth / 700), (int) healthHeight);
                flevorioHealth.getSprite().setSize(vidaFlevorio * healthWidth / 700, healthHeight);
                donchitoHealth.getSprite().setRegion(0, 0, (int) vidaDonChito * healthWidth / 700, (int) healthHeight);
                donchitoHealth.getSprite().setSize(vidaDonChito * healthWidth / 700, healthHeight);
                flevorioHealthBar.render(batch);
                flevorioHealth.render(batch);
                donChitoHealthBar.render(batch);
                donchitoHealth.render(batch);
                donChitoNameText.showMessage(batch,"Don Chito: "+vidaDonChito);
                flevorioNameText.showMessage(batch,"Flevorio: "+vidaFlevorio);

                resorteraText.showMessage(batch, "Resortera");
                botasText.showMessage(batch, "Botas");
                piedraText.showMessage(batch, "Piedra");
                picoText.showMessage(batch, "Pico");
                accionText.showMessage(batch, "¡Ataca!");
            }

        }
        else if (estado == State.ACCION){
            if(modoAtaque == "curar"){
                accionText.showMessage(batch, "Curandose con la piedra magica");
                if (!danioInflinjido) {
                    damage = randomIntGenerator.nextInt(100);
                    damage += 50;
                    vidaDonChito += damage;
                    if(vidaDonChito>=700){
                        vidaDonChito = 700;
                    }
                    danioInflinjido = true;
                }
                if(esperar(delta)){
                    estado = State.TURNOFLEVORIO;
                    estadoAtaque = AtackState.FLEVORIOFORWARD;
                    botonTribalera.getSprite().setColor(Color.WHITE);
                    damage = 0;
                    danioInflinjido = false;
                }
                donchitoDam.showMessage(batch,""+damage);
                donchitoDam.setColor(0,1,0,1);

                botonCurar.render(batch);
                botonTribalera.render(batch);
                botonResortera.render(batch);
                botonPico.render(batch);


                flevorioHealth.getSprite().setRegion(0, 0, (int) (vidaFlevorio * healthWidth / 700), (int) healthHeight);
                flevorioHealth.getSprite().setSize(vidaFlevorio * healthWidth / 700, healthHeight);
                donchitoHealth.getSprite().setRegion(0, 0, (int) vidaDonChito * healthWidth / 700, (int) healthHeight);
                donchitoHealth.getSprite().setSize(vidaDonChito * healthWidth / 700, healthHeight);
                flevorioHealthBar.render(batch);
                flevorioHealth.render(batch);
                donChitoHealthBar.render(batch);
                donchitoHealth.render(batch);
                donChitoNameText.showMessage(batch,"Don Chito: "+vidaDonChito);
                flevorioNameText.showMessage(batch,"Flevorio: "+vidaFlevorio);
            }
            else {
                if(modoAtaque == "Botas Tribaleras" && damage>=150){
                    accionText.showMessage(batch, "Flevorio estaba aturdido \ngolpe critico con botas tribaleras");
                }
                else {
                    accionText.showMessage(batch, "¡Atacando con " + modoAtaque + "!");
                }
                if (estadoAtaque == AtackState.DONCHITOFORWARD || estadoAtaque == AtackState.DONCHITOBACKWARD) {
                    if (estadoAtaque == AtackState.DONCHITOBACKWARD) {
                        flevorioDam.setColor(1f, .1f, .1f, 1);
                        flevorioDam.showMessage(batch, "" + damage);
                        if (!danioInflinjido) {
                            vidaFlevorio -= damage;
                            if (vidaFlevorio <= 0) {
                                if(todosLosItems){
                                    vidaFlevorio = 0;
                                }
                                else {
                                    vidaFlevorio = 1;
                                }
                            }
                            danioInflinjido = true;
                        }
                    }
                    if (ataqueDonChito()) {
                        estado = State.TURNOFLEVORIO;
                        estadoAtaque = AtackState.FLEVORIOFORWARD;
                        damage = 0;
                        danioInflinjido = false;
                    }
                }
            }
            flevorioHealth.getSprite().setRegion(0, 0, (int) (vidaFlevorio * healthWidth / 700), (int) healthHeight);
            flevorioHealth.getSprite().setSize(vidaFlevorio * healthWidth / 700, healthHeight);
            donchitoHealth.getSprite().setRegion(0, 0, (int) vidaDonChito * healthWidth / 700, (int) healthHeight);
            donchitoHealth.getSprite().setSize(vidaDonChito * healthWidth / 700, healthHeight);
            flevorioHealthBar.render(batch);
            flevorioHealth.render(batch);
            donChitoHealthBar.render(batch);
            donchitoHealth.render(batch);
            donChitoNameText.showMessage(batch,"Don Chito: "+vidaDonChito);
            flevorioNameText.showMessage(batch,"Flevorio: "+vidaFlevorio);
        }
        else if (estado == State.TURNOFLEVORIO){
            if(vidaFlevorio == 0){
                //efectoGanar.play();
                game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.CUEVA,game));
            }
            else {
                donchitoDam.setColor(1f, .1f, .1f, 1);
                if (flevorioAtaca()){
                    ataqueHecho = AtackMade.FLEVORIO;
                    if (flevorioAturdido) {
                        accionText.showMessage(batch, "¡Aturdiste a Flevorio con la resortera, \nno puede curarse");
                    } else {
                        accionText.showMessage(batch, "¡Estas siendo atacado por Flevorio");
                    }
                    if (estadoAtaque == AtackState.FLEVORIOFORWARD || estadoAtaque == AtackState.FLEVORIOBACKWARD) {
                        if (estadoAtaque == AtackState.FLEVORIOBACKWARD) {
                            donchitoDam.showMessage(batch, "" + damage);
                            if (!danioInflinjido) {
                                damage = randomIntGenerator.nextInt(100);
                                vidaDonChito -= damage;
                                if (vidaDonChito <= 0) {
                                    vidaDonChito = 0;
                                }
                                danioInflinjido = true;
                            }
                        }
                        if (ataqueFlevorio()) {
                            estado = State.PLAY;
                            damage = 0;
                            accionFlevorioDefinida = false;
                        }
                    }
                } else {
                    accionText.showMessage(batch, "Flevorio regenero salud con livermorio");
                    if (!danioInflinjido) {
                        damage = randomIntGenerator.nextInt(200);
                        if (vidaFlevorio <= 200) {
                            damage += 50;
                        }
                        if(vidaFlevorio == 1){
                            damage += 300;
                        }
                        vidaFlevorio += damage;
                        if (vidaFlevorio >= 700) {
                            vidaFlevorio = 700;
                        }
                        danioInflinjido = true;
                        flevorioHealth.getSprite().setRegion(0, 0, (int) (vidaFlevorio * healthWidth / 700), (int) healthHeight);
                        flevorioHealth.getSprite().setSize(vidaFlevorio * healthWidth / 700, healthHeight);
                        donchitoHealth.getSprite().setRegion(0, 0, (int) vidaDonChito * healthWidth / 700, (int) healthHeight);
                        donchitoHealth.getSprite().setSize(vidaDonChito * healthWidth / 700, healthHeight);
                    }
                    if (esperar(delta)) {
                        estado = State.PLAY;
                        damage = 0;
                        accionFlevorioDefinida = false;
                    }
                    flevorioDam.showMessage(batch, "" + damage);
                    flevorioDam.setColor(0, 1, 0, 1);
                }

            }
            flevorioHealth.getSprite().setRegion(0, 0, (int) (vidaFlevorio * healthWidth / 700), (int) healthHeight);
            flevorioHealth.getSprite().setSize(vidaFlevorio * healthWidth / 700, healthHeight);
            donchitoHealth.getSprite().setRegion(0, 0, (int) vidaDonChito * healthWidth / 700, (int) healthHeight);
            donchitoHealth.getSprite().setSize(vidaDonChito * healthWidth / 700, healthHeight);
            flevorioHealthBar.render(batch);
            flevorioHealth.render(batch);
            donChitoHealthBar.render(batch);
            donchitoHealth.render(batch);
            donChitoNameText.showMessage(batch,"Don Chito: "+vidaDonChito);
            flevorioNameText.showMessage(batch,"Flevorio: "+vidaFlevorio);
        }

        flevorio.render(batch);
        donChito.render(batch);
        if (estado == State.PAUSA){
            botonPlay.render(batch);
            fondoPausa.render(batch);
            botonConfiguracion.render(batch);
            botonSalirMenu.render(batch);
        }
        batch.end();
    }

    private void movimientosIniciales(SimpleAsset donChito, SimpleAsset flevorio){
        if(!movimientoInicialFlevorio) {
            if (flevorio.getSprite().getX() > 800) {
                flevorio.setPosition(flevorio.getSprite().getX() - 10, flevorio.getSprite().getY());
            } else {
                flevorio.getSprite().setPosition(800, flevorio.getSprite().getY());
                movimientoInicialFlevorio = true;
            }
        }
        if(!movimientoInicialDonChito) {
            if (donChito.getSprite().getX() < 300) {
                donChito.setPosition(donChito.getSprite().getX() + 10, donChito.getSprite().getY());
            } else {
                donChito.getSprite().setPosition(300, donChito.getSprite().getY());
                movimientoInicialDonChito = true;
            }
        }
        if(movimientoInicialDonChito && movimientoInicialFlevorio){
            estado = State.PLAY;
        }
    }

    private void reproducirMusica(Music musica){
        musica.setVolume(1F);
        if(!DonChito.preferences.getBoolean(Constants.MENUPRINCIPAL_SOUND_PREF,true)){
            musica.setVolume(0f);
        }
        musica.play();
    }

    private boolean ataqueDonChito(){
        if(estadoAtaque == AtackState.DONCHITOFORWARD){
            donChito.getSprite().setPosition(donChito.getSprite().getX()+7f,donChito.getSprite().getY());
            if(donChito.getSprite().getX()>=750){
                donChito.getSprite().setPosition(750,250);
                estadoAtaque = AtackState.DONCHITOBACKWARD;
                return false;
            }
        }
        else if(estadoAtaque == AtackState.DONCHITOBACKWARD){
            donChito.getSprite().setPosition(donChito.getSprite().getX()-7f,donChito.getSprite().getY());
            if(donChito.getSprite().getX()<=300){
                donChito.getSprite().setPosition(300,250);
                estadoAtaque = AtackState.IDLE;
                return true;
            }
        }
        donChito.render(batch);
        return false;
    }
    private boolean ataqueFlevorio(){
        if(estadoAtaque == AtackState.FLEVORIOFORWARD){
            flevorio.getSprite().setPosition(flevorio.getSprite().getX()-7f,flevorio.getSprite().getY());
            if(flevorio.getSprite().getX()<=350 && flevorio.getSprite().getY()<=250){
                flevorio.getSprite().setPosition(350,200);
                estadoAtaque = AtackState.FLEVORIOBACKWARD;
                return false;
            }
        }
        else if(estadoAtaque == AtackState.FLEVORIOBACKWARD){
            flevorio.getSprite().setPosition(flevorio.getSprite().getX()+7f,flevorio.getSprite().getY());
            if(flevorio.getSprite().getX()>=800){
                flevorio.getSprite().setPosition(800,200);
                estadoAtaque = AtackState.IDLE;
                return true;
            }
        }
        flevorio.render(batch);
        return false;
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
                Gdx.app.log("Turnos esperando usar bota",""+turnoBloqueoBota);
                Vector3 temp = camera.unproject(new Vector3(x, y, 0),view.getScreenX(),view.getScreenY(),view.getScreenWidth(),view.getScreenHeight());
                if(estado == State.PLAY) {
                    if (botonPico.isTouched(x, y, camera, view)) {
                        estadoAtaque = AtackState.DONCHITOFORWARD;
                        estado = State.ACCION;
                        damage = randomIntGenerator.nextInt(100);
                        modoAtaque = "Pico";
                        ataqueHecho = AtackMade.DONCHITOPICO;
                        if(turnoBloqueoBota <2){
                            turnoBloqueoBota++;
                            if(turnoBloqueoBota == 2){
                                botonTribalera.getSprite().setColor(Color.WHITE);
                            }
                        }
                        if(flevorioAturdido){
                            flevorioAturdido = false;
                            botonResortera.getSprite().setColor(Color.WHITE);
                        }

                    } else if (botonCurar.isTouched(x, y, camera, view)) {
                        ataqueHecho = AtackMade.DONCHITOCURAR;
                        estado = State.ACCION;
                        modoAtaque = "curar";
                        if(turnoBloqueoBota <2){
                            turnoBloqueoBota++;
                            if(turnoBloqueoBota == 2){
                                botonTribalera.getSprite().setColor(Color.WHITE);
                            }
                        }
                        if(flevorioAturdido){
                            flevorioAturdido = false;
                            botonResortera.getSprite().setColor(Color.WHITE);
                        }

                    } else if (botonTribalera.isTouched(x, y, camera, view) && DonChito.preferences.getBoolean("Livermorio", false)) {
                        if(turnoBloqueoBota == 2) {
                            estadoAtaque = AtackState.DONCHITOFORWARD;
                            estado = State.ACCION;
                            modoAtaque = "Botas Tribaleras";
                            turnoBloqueoBota = 0;
                            ataqueHecho = AtackMade.DONCHITOBOTAS;
                            damage = randomIntGenerator.nextInt(100);
                            if (flevorioAturdido) {
                                flevorioAturdido = false;
                                damage += 150;
                                botonResortera.getSprite().setColor(Color.WHITE);
                            }
                            botonTribalera.getSprite().setColor(Color.GRAY);
                        }

                    } else if (botonResortera.isTouched(x, y, camera, view) && DonChito.preferences.getBoolean("RomanStruggle", false)) {
                        if(!flevorioAturdido) {
                            estadoAtaque = AtackState.DONCHITOFORWARD;
                            estado = State.ACCION;
                            modoAtaque = "Resortera";
                            damage = randomIntGenerator.nextInt(100);
                            ataqueHecho = AtackMade.DONCHITORESORTERA;
                            flevorioAturdido = true;
                            curacionFlevorio = true;
                            accionFlevorioDefinida = true;
                            botonResortera.getSprite().setColor(Color.GRAY);
                        }
                        if(turnoBloqueoBota <2){
                            turnoBloqueoBota++;
                            if(turnoBloqueoBota == 2){
                                botonTribalera.getSprite().setColor(Color.WHITE);
                            }
                        }
                    }
                    else if (botonPausa.isTouched(x,y,camera,view) || botonPlay.isTouched(x,y,camera,view)){
                        estado = State.PAUSA;
                    }
                }
                else if(estado == State.PAUSA){
                    if(botonPlay.isTouched(x,y,camera,view)){
                        estado = State.PLAY;
                    }
                    else if(botonSalirMenu.isTouched(x,y,camera,view)){
                        stopMusic();
                        game.setScreen(new LoadingScreen(LoadingScreen.ScreenSel.MENU,game));
                    }
                }

                return true; // return true to indicate the event was handled
            }

            @Override
            public boolean touchUp (int x, int y, int pointer, int button) {
                Vector3 temp = camera.unproject(new Vector3(x, y, 0),view.getScreenX(),view.getScreenY(),view.getScreenWidth(),view.getScreenHeight());


                return true; // return true to indicate the event was handled
            }
        });
    }
    public enum State
    {
        PAUSA,
        PLAY,
        ACCION,
        TURNOFLEVORIO,
        INTRO
    }
    public enum AtackState
    {
        DONCHITOFORWARD,
        DONCHITOBACKWARD,
        FLEVORIOFORWARD,
        FLEVORIOBACKWARD,
        IDLE
    }
    public enum AtackMade
    {
        DONCHITOPICO,
        DONCHITOCURAR,
        DONCHITOBOTAS,
        DONCHITORESORTERA,
        FLEVORIO
    }
}
