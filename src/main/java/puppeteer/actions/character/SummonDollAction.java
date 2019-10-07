package puppeteer.actions.character;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.DarkSmokePuffEffect;
import com.megacrit.cardcrawl.vfx.SmokePuffEffect;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.fields.DollFields;

public class SummonDollAction extends AbstractGameAction {
    private static final float DURATION = 0.1f;

    private AbstractDoll summon;

    public SummonDollAction(AbstractDoll toSummon)
    {
        this.summon = toSummon;
        this.actionType = ActionType.SPECIAL;

        this.startDuration = this.duration = DURATION;
    }

    @Override
    public void update() {
        if (DollFields.dolls.get(AbstractDungeon.player).size() >= DollFields.maxDolls.get(AbstractDungeon.player))
        {
            //TODO: Display max doll message
            this.isDone = true;
            return;
        }
        if (this.duration == this.startDuration)
        {
            //do some effects
            AbstractDungeon.effectList.add(new SmokePuffEffect(summon.x, summon.y));
        }
        tickDuration();

        if (this.isDone)
        {
            //summon the doll
            DollFields.dolls.get(AbstractDungeon.player).add(summon);
            DollFields.dollsThisCombat.set(AbstractDungeon.player, DollFields.dollsThisCombat.get(AbstractDungeon.player) + 1);
        }
    }
}