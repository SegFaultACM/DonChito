package mx.itesm.donchito;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


public class SimpleAsset {
    private Texture texture;
    private Sprite sprite;
    private Vector2 vector2;
    private AssetManager assetManager;

    public SimpleAsset(String strtexture,Vector2 vec){
        assetManager = DonChito.getAssetManager();
        this.texture = assetManager.get(strtexture);
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

    public boolean isTouched(float x,float y,Camera camera){
        Vector3 temp = camera.unproject(new Vector3(x, y, 0));
        return this.getSprite().getBoundingRectangle().contains(temp.x,temp.y);
    }
}
