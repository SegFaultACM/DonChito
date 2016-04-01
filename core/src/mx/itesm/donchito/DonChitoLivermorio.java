package mx.itesm.donchito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created  on 3/27/2016.
 * @author Joel Lara
 */
public class DonChitoLivermorio {

    public static final double GRAVITY = 10.8;
    Vector2 position;

    Vector2 lastFramePosition;

    Vector2 velocity;

    Facing facing;
    JumpState jumpState;
    WalkState walkState;

    long walkStartTime;
    long jumpStartTime;


    public static final float PLAYER_STANCE_WIDTH = 21.0f;
    public static final float PLAYER_MOVE_SPEED = 250;

    public static final float JUMP_SPEED = 1.5f * 300;
    public static final float MAX_JUMP_DURATION = .1f;

    private Animation animacion;

    public DonChitoLivermorio() {
        position = new Vector2(400, 400);

        lastFramePosition = new Vector2(position);

        velocity = new Vector2();
        jumpState = JumpState.FALLING;
        facing = Facing.RIGHT;
        walkState = WalkState.STANDING;

        TextureRegion texturaCompleta = new TextureRegion(new Texture(Constants.PLAYER_TEXTURE));
        TextureRegion[][] texturaPersonaje = texturaCompleta.split(96,134);
        animacion = new Animation(.15f,texturaPersonaje[0][0],
                texturaPersonaje[0][1], texturaPersonaje[0][2]);
        // Animación infinita
        animacion.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void update(float delta, Array<SimpleAsset> platforms,LivermorioEscape.GameState gameState) {
        if(gameState == LivermorioEscape.GameState.PLAY){
            lastFramePosition.set(position);
            velocity.y -= GRAVITY;
            position.mulAdd(velocity, delta);

            if (jumpState != JumpState.JUMPING) {
                jumpState = JumpState.FALLING;

                if (position.y <= 0) {
                    jumpState = JumpState.GROUND;
                    position.y = -5;
                    velocity.y = 0;
                }

                for (SimpleAsset platform : platforms) {
                    if (landedOnPlatform(platform)) {
                        jumpState = JumpState.GROUND;
                        velocity.y = 0;
                        position.y = platform.getSprite().getY() + platform.getSprite().getHeight();
                    }
                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                moveLeft(delta);
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                moveRight(delta);
            } else {
                walkState = WalkState.STANDING;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                switch (jumpState) {
                    case GROUND:
                        startJump();
                        break;
                    case JUMPING:
                        continueJump();
                }
            } else {
                endJump();
            }
        }

    }



    boolean landedOnPlatform(SimpleAsset platform) {
        boolean leftFootIn = false;
        boolean rightFootIn = false;
        boolean straddle = false;


        if (lastFramePosition.y >= platform.getSprite().getY() + platform.getSprite().getHeight()  &&
                position.y < platform.getSprite().getY() + platform.getSprite().getHeight()) {

            float leftFoot = position.x - PLAYER_STANCE_WIDTH / 2;
            float rightFoot = position.x + PLAYER_STANCE_WIDTH / 2;

            leftFootIn = (platform.getSprite().getX()< leftFoot && platform.getSprite().getX() + platform.getSprite().getWidth() > leftFoot);
            rightFootIn = (platform.getSprite().getX()< rightFoot && platform.getSprite().getX() + platform.getSprite().getWidth() > rightFoot);

            straddle = (platform.getSprite().getX() > leftFoot && platform.getSprite().getX() + platform.getSprite().getWidth() < rightFoot);
        }

        return leftFootIn || rightFootIn || straddle;
    }


    private void moveLeft(float delta) {
        if (jumpState == JumpState.GROUND && walkState != WalkState.WALKING) {
            walkStartTime = TimeUtils.nanoTime();
        }
        walkState = WalkState.WALKING;
        facing = Facing.LEFT;
        position.x -= delta * PLAYER_MOVE_SPEED;
    }


    private void moveRight(float delta) {
        if (jumpState == JumpState.GROUND && walkState != WalkState.WALKING) {
            walkStartTime = TimeUtils.nanoTime();
        }
        walkState = WalkState.WALKING;
        facing = Facing.RIGHT;
        position.x += delta * PLAYER_MOVE_SPEED;
    }


    private void startJump() {
        jumpState = JumpState.JUMPING;
        jumpStartTime = TimeUtils.nanoTime();
        continueJump();
    }

    private void continueJump() {
        if (jumpState == JumpState.JUMPING) {
            float jumpDuration = MathUtils.nanoToSec * (TimeUtils.nanoTime() - jumpStartTime);
            if (jumpDuration < MAX_JUMP_DURATION) {
                velocity.y =JUMP_SPEED;
            } else {
                endJump();
            }
        }
    }

    private void endJump() {
        if (jumpState == JumpState.JUMPING) {
            jumpState = JumpState.FALLING;
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion region =  animacion.getKeyFrame(0); //DonChito.getAssetManager().get(Constants.PLAYER_STAND_RIGHT);

        if (facing == Facing.RIGHT && jumpState != JumpState.GROUND) {
            //region = DonChito.getAssetManager().get(Constants.PLAYER_JUMP_RIGHT);
            region = animacion.getKeyFrame(0);
            if (region.isFlipX()) {
                region.flip(true,false);
            }
        } else if (facing == Facing.RIGHT && walkState == WalkState.STANDING) {
            //region = DonChito.getAssetManager().get(Constants.PLAYER_STAND_RIGHT);
            region = animacion.getKeyFrame(0);
            if (region.isFlipX()) {
                region.flip(true,false);
            }
        } else if (facing == Facing.RIGHT && walkState == WalkState.WALKING) {
            float walkTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - walkStartTime);
            region = animacion.getKeyFrame(walkTimeSeconds);
            if (region.isFlipX()) {
                region.flip(true,false);
            }
        } else if (facing == Facing.LEFT && jumpState != JumpState.GROUND) {
            //region = DonChito.getAssetManager().get(Constants.PLAYER_JUMP_LEFT);
            region =  animacion.getKeyFrame(0);
            if (!region.isFlipX()) {
                region.flip(true,false);
            }
        } else if (facing == Facing.LEFT && walkState == WalkState.STANDING) {
            //region = DonChito.getAssetManager().get(Constants.PLAYER_STAND_LEFT);
            region =  animacion.getKeyFrame(0);
            if (!region.isFlipX()) {
                region.flip(true,false);
            }
        } else if (facing == Facing.LEFT && walkState == WalkState.WALKING) {
            float walkTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - walkStartTime);
            region = animacion.getKeyFrame(walkTimeSeconds);
            if (!region.isFlipX()) {
                region.flip(true,false);
            }
        }
        batch.draw(region, position.x, position.y);
    }

    public float getX(){
        return this.position.x;
    }
    public float getY(){
        return this.position.y;
    }
    enum JumpState {
        JUMPING,
        FALLING,
        GROUND
    }

    enum Facing {
        LEFT,
        RIGHT
    }

    enum WalkState {
        STANDING,
        WALKING
    }
}
