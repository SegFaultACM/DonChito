package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Esteban on 3/17/2016.
 */
public class romanRock extends SimpleAsset{
    private SimpleAsset simpelasset;
    private int direccionH; // 1 es derecha, 0 es izquierda

    public int getDireccionV() {
        return direccionV;
    }

    public int getDireccionH() {
        return direccionH;
    }

    private int direccionV; //1 es abajo, 0 es arriba
    private float velocidad;

    public float getEscala() {
        return escala;
    }

    private float escala;
    private static float ACELERACION = 0.1f;
    private static float MOVIMIENTOH = 2f;
    private static float VELOCIDADLIMITE = 10f;


    public romanRock(String strtexture, Vector2 vec,int direccionH, int direccionV,float escala) {
        super(strtexture, vec);
        this.direccionH = direccionH;
        this.direccionV = direccionV;
        this.velocidad = 0;
        this.escala = escala;
        this.getSprite().setScale(this.escala);

    }

    public void updateRock(){
        float movimientoX = 0;
        float movimientoY = 0;
        if(this.direccionV == 1){ //si se esta moviendo hacia abajo
            if(this.velocidad >= VELOCIDADLIMITE){
                this.velocidad = VELOCIDADLIMITE;
            }
            else{
                this.velocidad+= ACELERACION;
            }

            if(this.getSprite().getY()<=20){
                movimientoY = this.velocidad;
                this.direccionV = 0;
            }
            else {
                movimientoY = this.velocidad * -1;
            }
        }
        else{
            if(this.velocidad <=0){
                this.velocidad = 0;
                this.direccionV = 1;
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
                movimientoX = MOVIMIENTOH*-1;
                this.direccionH = 0;
            }
        }
        else{
            if(this.getSprite().getX()>10){
                movimientoX = MOVIMIENTOH*-1;
            }
            else{
                movimientoX = MOVIMIENTOH;
                this.direccionH = 1;
            }
        }
        this.setPosition(new Vector2(this.getSprite().getX()+movimientoX,this.getSprite().getY()+movimientoY));
    }

    public void changeVerticalMovement(){

    }

    @Override
    public void setPosition(Vector2 vec) {
        super.setPosition(vec);
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
}
