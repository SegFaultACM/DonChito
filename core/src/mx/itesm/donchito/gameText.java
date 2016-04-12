package mx.itesm.donchito;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Esteban on 4/7/2016.
 */

public class GameText
{
    private BitmapFont font;
    private float xcoord,ycoord;


    public GameText(float posicionx, float posiciony) {
        font = new BitmapFont(Gdx.files.internal("font/font.fnt"), Gdx.files.internal("font/font.png"), false);
        font.setColor(Color.CORAL);
        font.getData().scale(0.001f);
        this.xcoord = posicionx;
        this.ycoord = posiciony;
    }

    public void showMessage(SpriteBatch batch, String text) {
        GlyphLayout glyp = new GlyphLayout();
        glyp.setText(font, text);
        float anchoTexto = glyp.width;
        font.draw(batch,glyp,this.xcoord-anchoTexto/3,this.ycoord);
    }
    public void setPosition(float x,float y){
        this.xcoord = x;
        this.ycoord = y;
    }
    public void setColor (float r, float g, float b, float a) {
        font.setColor(r, g, b, a);
    }

    public float getX(){return this.xcoord;}
    public float getY(){return this.ycoord;}
}
