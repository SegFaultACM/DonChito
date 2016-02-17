package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by joell on 2/16/2016.
 */
public class SimpleAsset {
    private Texture texture;
    private Sprite sprite;
    private Vector2 vector2;


    public SimpleAsset(String strtexture,Vector2 vec){
        this.texture = new Texture(Gdx.files.internal(strtexture));
        this.sprite = new Sprite(texture);
        this.vector2 = vec;
        this.setPosition(this.vector2);

    }
    public void setPosition(Vector2 vec){
        this.sprite.setPosition(vec.x, vec.y);
    }

    public void setRotation(float degree){
        this.sprite.setRotation(degree);
    }
    public void render(SpriteBatch batch){
        this.sprite.draw(batch);
    }

    public Sprite getSprite(){
        return this.sprite;
    }

    public void dispose(){
        this.texture.dispose();
    }
    public boolean isTouched(float x,float y){
        return this.getSprite().getBoundingRectangle().contains(x,y);
    }

}
