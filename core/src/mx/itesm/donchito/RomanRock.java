package mx.itesm.donchito;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Esteban on 3/17/2016.
 */
public class RomanRock extends SimpleAsset{
    private SimpleAsset simpelasset;
    private int direccionH; // 1 es derecha, 0 es izquierda

    public int getDireccionV() {
        return direccionV;
    }

    public int getDireccionH() {
        return direccionH;
    }

    private int direccionV; //1 es abajo, 0 es arriba cambiar a ENUM
    private float velocidad;

    public float getEscala() {
        return escala;
    }

    public float getVelocidad() {
        return velocidad;
    }

    private float escala;
    private static float ACELERACION = 0.08f;
    private static float MOVIMIENTOH = 2f;
    private float velocidadLimite;
    private float alturaMaxima;

    private float rockHeight;
    private float rockWidth;


    public RomanRock(String strtexture, float x, float y, int direccionH, int direccionV, float escala, float velocidad) {
        super(strtexture, x,y);
        this.direccionH = direccionH;
        this.direccionV = direccionV;
        this.velocidad = velocidad;
        this.escala = escala;
        this.alturaMaxima = 550f;
        this.velocidadLimite = 10F;
        this.rockHeight = this.getSprite().getHeight()*this.escala;
        this.rockWidth = this.getSprite().getWidth()*this.escala;
        this.getSprite().setScale(this.escala);

    }

    public void updateRock(){
        float movimientoX = 0;
        float movimientoY = 0;
        if(this.direccionV == 1){ //si se esta moviendo hacia abajo
            if(this.velocidad >= this.velocidadLimite){
                this.velocidad = this.velocidadLimite;
            }
            else{
                this.velocidad+= ACELERACION;
            }

            if(this.getSprite().getY()<=20){
                movimientoY = this.velocidad;
                this.direccionV = 0;
                this.velocidadLimite-=0.5f;
                this.alturaMaxima-=50f;
            }
            else {
                movimientoY = -this.velocidad;
            }
        }
        else{ //si se mueve hacia arriba
            if(this.velocidad <0){
                this.velocidad = 1;
                if(this.getSprite().getY()>this.alturaMaxima){
                    this.direccionV = 1;
                    this.alturaMaxima-=50f;
                    this.velocidadLimite-=0.5f;
                }
            }
            else{
                this.velocidad-= ACELERACION;
            }
            movimientoY = this.velocidad;
        }

        if(this.direccionH == 1){
            if(this.getSprite().getX()<1100){
                movimientoX = MOVIMIENTOH;
            }
            else{
                movimientoX = -MOVIMIENTOH;
                this.direccionH = 0;
            }
        }
        else{
            if(this.getSprite().getX()>10){
                movimientoX = -MOVIMIENTOH;
            }
            else{
                movimientoX = MOVIMIENTOH;
                this.direccionH = 1;
            }
        }
        //optimizar
        this.setPosition(this.getSprite().getX()+movimientoX,this.getSprite().getY()+movimientoY);
    }

    @Override
    public void setPosition(float x,float y) {
        super.setPosition(x,y);
    }
    @Override
    public void setRotation(float degree) {
        super.setRotation(degree);
    }
    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }
    @Override
    public Sprite getSprite() {
        return super.getSprite();
    }
    @Override
    public boolean isTouched(float x, float y, Camera camera) {
        return super.isTouched(x, y, camera);
    }

    public float getRockWidth(){
        return this.rockWidth;
    }
    public float getRockHeight(){
        return this.rockHeight;
    }

}
