package puppeteer.abstracts;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public abstract class AbstractMagic {
    public static ShapeRenderer sr = new ShapeRenderer();

    protected float originX;
    protected float originY;

    protected float targetX;
    protected float targetY;

    public void setOrigin(float x, float y)
    {
        originX = x;
        originY = y;
    }
    public void setTarget(float x, float y)
    {
        targetX = x;
        targetY = y;
    }


    public void renderArea()
    {
        this.renderArea(originX, originY, targetX, targetY);
    }
    public abstract void renderArea(float startX, float startY, float targetX, float targetY);
    public abstract VFXAction getVfx();

    public abstract ArrayList<AbstractMonster> getTargets();

    public abstract AbstractMagic getCopy();
}
