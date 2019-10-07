package puppeteer.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.ExhaustBlurEffect;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;

public class FakeExhaustCardEffect extends AbstractGameEffect {
    private AbstractCard c;
    private static final float DUR = 1.0F;

    public FakeExhaustCardEffect(AbstractCard c) {
        this.duration = DUR;
        this.c = c;
    }

    public void update() {
        if (this.duration == 1.0F) {
            CardCrawlGame.sound.play("CARD_EXHAUST", 0.2F);

            int i;
            for(i = 0; i < 90; ++i) {// 24
                AbstractDungeon.effectsQueue.add(new ExhaustBlurEffect(this.c.current_x, this.c.current_y));
            }

            for(i = 0; i < 50; ++i) {// 27
                AbstractDungeon.effectsQueue.add(new ExhaustEmberEffect(this.c.current_x, this.c.current_y));
            }
        }

        this.duration -= Gdx.graphics.getDeltaTime();

        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) { }

    public void dispose() { }
}