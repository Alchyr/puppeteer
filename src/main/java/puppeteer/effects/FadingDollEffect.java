package puppeteer.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import puppeteer.abstracts.AbstractDoll;

//render a doll with slowly decreasing alpha - for non-violent doll destruction
public class FadingDollEffect extends AbstractGameEffect
{
    private AbstractDoll d;

    public FadingDollEffect(AbstractDoll d)
    {
        this.d = d;
        this.color = Color.WHITE.cpy();

        this.startingDuration = this.duration = Settings.FAST_MODE ? 0.1f : 0.3f;
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();

        this.color.a = this.duration / this.startingDuration;

        if (this.duration < 0.0F) {
            this.isDone = true;
            this.color.a = 0.0F;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        d.render(sb, false, this.color);
    }

    @Override
    public void dispose() {
        d = null;
    }
}
