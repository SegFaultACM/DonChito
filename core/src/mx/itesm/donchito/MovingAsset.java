package mx.itesm.donchito;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by danielsada on 3/11/16.
 */
public class MovingAsset extends SimpleAsset {
    public MovingAsset(String strtexture, Vector2 vec) {
        super(strtexture, vec);
    }
    public Vector2 getPosition(){
        return new Vector2(this.getSprite().getX(), this.getSprite().getY());
    }

    @Override
    public boolean isTouched(float x, float y, Camera camera) {
        return super.isTouched(x, y, camera);
    }

    @Override
    public Sprite getSprite() {
        return super.getSprite();
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }

    @Override
    public void setRotation(float degree) {
        super.setRotation(degree);
    }

    @Override
    public void setPosition(Vector2 vec) {
        super.setPosition(vec);
    }
}
