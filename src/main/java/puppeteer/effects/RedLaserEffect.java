package puppeteer.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import puppeteer.util.MathHelper;
import puppeteer.util.TextureLoader;

import static puppeteer.PuppeteerMod.makeEffectPath;
import static puppeteer.util.MathHelper.dist;

public class RedLaserEffect extends AbstractGameEffect {
    private static Texture laserTexture = TextureLoader.getTexture(makeEffectPath("laser.png"));

    private static final int WIDTH = 512;
    private static final int HEIGHT = 64;

    private static final float X_OFFSET = 0;
    private static float yOffset;

    private static final float DURATION = 0.3f;
    private static final float SHRINK_POINT = DURATION * (2.0f / 3.0f);
    private static final float GROW_STOP = DURATION / 2.0f;

    private float width;
    private float targetHeight;

    private float x;
    private float y;

    private Color color;

    private boolean playedSfx;

    public RedLaserEffect(float x, float y, float targetX, float targetY)
    {
        super();

        this.x = x;
        this.y = y;

        width = dist(x, targetX, y, targetY);
        targetHeight = HEIGHT * (width / WIDTH) * 0.5f;
        yOffset = targetHeight / 2.0f;

        this.rotation = MathHelper.angle(x, targetX, y, targetY);

        this.y -= yOffset;

        this.scale = 0.1f;

        playedSfx = false;


        this.color = Color.WHITE.cpy();

        this.duration = 0;
    }


    @Override
    public void update() {
        if (!this.playedSfx) {
            //AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(Color.SKY));
            this.playedSfx = true;
            CardCrawlGame.sound.play("ATTACK_MAGIC_BEAM");
            CardCrawlGame.screenShake.mildRumble(0.2F);
        }

        this.duration += Gdx.graphics.getDeltaTime();

        if (duration < GROW_STOP)
        {
            scale = MathUtils.lerp(0.1f, 1.0f, this.duration / GROW_STOP);
        }
        else if (duration < SHRINK_POINT)
        {
            scale = 1.0f;
        }
        else {
            scale = MathUtils.lerp(1.0f, 0.1f, (this.duration - SHRINK_POINT) / (DURATION - SHRINK_POINT));
            if (duration >= DURATION)
            {
                this.isDone = true;
                scale = 0.1f;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(laserTexture, x, y, X_OFFSET, yOffset, width, targetHeight, 1.0f, scale, rotation, 0, 0, WIDTH, HEIGHT, false, false);
    }

    @Override
    public void dispose() {

    }
}
