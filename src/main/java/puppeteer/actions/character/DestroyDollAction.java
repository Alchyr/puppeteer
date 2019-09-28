package puppeteer.actions.character;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.SmokePuffEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.effects.FadingDollEffect;
import puppeteer.fields.DollFields;

public class DestroyDollAction extends AbstractGameAction {
    private AbstractDoll d;
    private boolean boom;

    public DestroyDollAction(AbstractDoll d, boolean explosive)
    {
        this.d = d;
        this.boom = explosive;

        this.startDuration = this.duration = Settings.FAST_MODE ? 0.1f : 0.2f;
    }
    public DestroyDollAction(AbstractDoll d)
    {
        this(d, false);
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            DollFields.dolls.get(AbstractDungeon.player).remove(d);

            if (boom) {
                AbstractDungeon.effectList.add(new ExplosionSmallEffect(d.x, d.y));
            }
            else {
                AbstractDungeon.effectList.add(new SmokePuffEffect(d.x, d.y));
            }
            AbstractDungeon.effectList.add(new FadingDollEffect(d));
        }
        this.tickDuration();
    }
}
